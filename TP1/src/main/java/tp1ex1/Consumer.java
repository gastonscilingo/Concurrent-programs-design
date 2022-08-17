package tp1ex1;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer<T> implements Runnable {

	private Pool<T> pool;
	private int amount;
	private AtomicInteger consumed;
	
	public Consumer(Pool<T> pool, int amount, AtomicInteger consumed) {
		this.pool = pool;
		this.amount = amount;
		this.consumed = consumed;
	}	
	
	public void run() {
		while (consumed.get()<amount){
			T item = pool.get();
			if (item != null) {
				consumed.incrementAndGet();
			}
		}
	}

}