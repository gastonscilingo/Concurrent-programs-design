package tp1ex1;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ConcurrentQueue<T> {
	
	private AtomicInteger size; 
	private AtomicReference<Node<T>> head;
	private AtomicReference<Node<T>> last;
	
	public ConcurrentQueue() {
		head = new AtomicReference<Node<T>>();
		last = new AtomicReference<Node<T>>(head.get());
		size = new AtomicInteger(0);
	}

	/**
	 * 
	 */
	public ConcurrentQueue(T item) {
		head = new AtomicReference<Node<T>>();
		head.set(new Node<T>(item));
		last = new AtomicReference<Node<T>>(head.get());
		size = new AtomicInteger(1);
	}
	
	/**
	 * @return the head, an item or null if an empty queue
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
	 * @return the last, an item or null if an empty queue
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
	 * @param item to insert
	 */
	public void insert(T item){
		if (item == null) throw new IllegalArgumentException();
		Node<T> newLast = new Node<T>(item);
		Node<T> oldLast;
		do {
			oldLast = last.get();
			synchronized(this){
			if(last.compareAndSet(oldLast, newLast)){
					if (head.get()==null){
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

	/**
	 * @return the head of this queue
	 */
	public T poll(){
		boolean success = false;
		Node<T> oldHead;
		do{
			oldHead = head.get();
			synchronized(this){ // head can be null just before compareAndSet
				if (head.get() == null){
					return null;
				}
				success = head.compareAndSet(oldHead, head.get().next);
			}
		}while(!success);
		size.decrementAndGet();
		return oldHead.item;
	}	

	/**
	 * @return the number of elements
	 */
	public int size(){
		return size.get();
	}
	
	public synchronized String toString(){
		if (size.get() == 0)
			return "[]";
		String output = "head -> ";
		Node<T> current = head.get();
		for (int i = 0; i < size.get(); i++){
			output+="["+current.item.toString()+"] -> ";
			current = current.next;
		}
		return output+last.get().next;
	}
	
	public synchronized boolean repOk(){
//		To efficiency reason this property doesn't hold 
//		if (size.get() == 0) {
//			return head.get() == null ? (head.get() == last.get()) : false;
//		}
		int nodes = 0;
		Node<T> current = head.get();
		while (current!=null){
			nodes++;
			current = current.next;
		}
		if (nodes==size.get())
			return true;
		return false;
	}
	

	private class Node<G> {
		
        protected final G item;
        public Node<G> next;
        
        public Node(G item) {
            this.item = item;
            this.next = null;
        }
    }

	
}

