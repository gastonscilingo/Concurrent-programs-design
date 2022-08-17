package tp1ex1;

import java.util.Random;

public class Pool<T> {

	private ConcurrentQueue<T> queue1;
	private ConcurrentQueue<T> queue2;
	private Random random;

	public Pool() {
		super();
		this.queue1 = new ConcurrentQueue<T>();
		this.queue2 = new ConcurrentQueue<T>();
		this.random = new Random();
	}

	public void put(T item){
		if(random.nextBoolean()){
			queue1.insert(item);
		}else{
			queue2.insert(item);
		}
	}
	
	public T get(){
		T item;
		if(random.nextBoolean()){
			if(queue1.size()>0)
				item = queue1.poll();
			else
				item = queue2.poll();
		}else{
			if(queue2.size()>0)
				item = queue2.poll();
			else
				item = queue1.poll();
		}
		return item;		
	}
	
	public int size(){
		return queue1.size()+queue2.size();
	}
	
	public boolean repOk(){
		return queue1.repOk() & queue2.repOk();
	}

}
