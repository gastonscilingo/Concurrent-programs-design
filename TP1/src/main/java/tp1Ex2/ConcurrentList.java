package tp1Ex2;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentList<T> {
	
	private AtomicInteger size; 
	private AtomicReference<Node<T>> head;
	private AtomicReference<Node<T>> last;
	AtomicInteger countRemove = new AtomicInteger(0);

	/**
	 * Build a empty list 
	 */
	public ConcurrentList() {
		head = new AtomicReference<Node<T>>();
		last = new AtomicReference<Node<T>>(head.get());
		size = new AtomicInteger(0);
	}

	/**
	 * Build a list with one item
	 * @param item to put in
	 */
	public ConcurrentList(T item) {
		head = new AtomicReference<Node<T>>();
		head.set(new Node<T>(item));
		last = new AtomicReference<Node<T>>(head.get());
		size = new AtomicInteger(1);
	}
	
	/**
	 * @return the head item or null if empty
	 */
	public T getHead() {
		synchronized(this){
		Node<T> toReturn = head.get();
			if(toReturn != null)
				return toReturn.item;
		}
		return null;
	}

	/**
	 * @return the last item or null if empty
	 */
	public T getLast() {
		synchronized(this) {
			Node<T> toReturn = last.get();
			if (toReturn != null) {
				return toReturn.item;
			}
		}
		return null;
	}

	/**
	 * @param item to add
	 */
	public void add(T item){
		if (item == null) throw new IllegalArgumentException();
		Node<T> newLast = new Node<T>(item);
		Node<T> oldLast;
		do {
			oldLast = last.get();
			synchronized(this){
			if(last.compareAndSet(oldLast, newLast)){
					if (head.get() == null){	// special case, head and oldLast are the same
						head.set(newLast);
						size.incrementAndGet();
						return;
					}
					oldLast.next = newLast;
					size.incrementAndGet();
					return;
				}
			}
		} while(true);
	}
	
	public boolean remove(T o) {
		return remove1(o); // concurrency maximized
//		return remove2(2); // method synchronized, comparative purpose
	}
	
	/**
	 * @param o element to be removed from this list, if present
	 * @return true iff remove element o
	 */
	private boolean remove1(T o) {
		do {
			Node<T>	prev = null;
			Node<T> current = head.get();
			while (current != null && (!current.item.equals(o) || current.toRemove) ) {
				prev = current;
				current = current.next;
			}
			if (current == null) {
				return false;
			}
			// mark to remove 
			synchronized(this) {
				if (current.toRemove) {
					return false;// it guarantee that current won't behind head  
				}
				current.toRemove = true;
			}
			synchronized(this) {
				if (head.compareAndSet(current, current.next)) {
					last.compareAndSet(current, current.next); // ¿oldhead == last? then update last to
					size.decrementAndGet();
					return true;
				}
				if (current == last.get()) {
					last.set(prev);
					prev.next = current.next;
					size.decrementAndGet();
					return true;
				}
				if (!prev.toRemove) {
					prev.next = current.next;
					size.decrementAndGet();
					return true;
				}
				// if prev was mark, maybe prev was remove, try again  
			}
			current.toRemove = false;	
		}while(true);
		
	}
	/**
	 * @param o element to be removed from this list, if present
	 * @return true iff remove element o 
	 */
	private synchronized boolean remove2(T o) {
			Node<T> current = head.get();
			Node<T> prev = null;
			if (current != null && current.item.equals(o)) {
				if (head.get() == last.get()) {
					last.set(head.get().next);
				}
				head.set(head.get().next);
				size.decrementAndGet();
				return true;
			}
			while (current != null && !current.item.equals(o)) {
				prev = current;
				current = current.next;
			}
			if (current != null) {
				if (current == last.get()) {
					last.set(prev);
				}
				prev.next = current.next;
				size.decrementAndGet();
				return true;
			}
			return false;
	}	

	/**
	 * @return the number of elements
	 */
	public int size(){
		return size.get();
	}
	
	/**
	 * param o element whose presence in this list is to be tested
	 */
	public synchronized boolean contains(T o) {
		if (head.get() == null) {
			return false;
		}
		Node<T> current = head.get();
		while (current != null) {
			if (current.item.equals(o)) {
				return true;
			}
			current = current.next;
		}
		return false;
	}
	
	public synchronized String toString(){
		if (head.get() == null)
			return "[]";
		String output = "head -> ";
		Node<T> current = head.get();
		while (current != null) {
			output+="["+current.item.toString()+"] -> ";
			current = current.next;
		}
		return output+last.get().next;
	}
	
	public synchronized boolean repOk(){
		int nodes = 0;
		Node<T> current = head.get();
		while (current != null){
			nodes++;
			current = current.next;
		}
		if (nodes == size.get())
			return true;
		System.out.println("repOk nodes: "+nodes);
		return false;
	}
	

	public class Node<G> {
		
        public boolean toRemove;
		protected final G item;
        public Node<G> next;
        
        public Node(G item) {
            this.item = item;
            this.next = null;
            this.toRemove = false;
        }
    }

	
}

