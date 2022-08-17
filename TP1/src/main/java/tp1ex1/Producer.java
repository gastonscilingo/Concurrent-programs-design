package tp1ex1;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer<T> implements Runnable {
	
	private int amount;
	private AtomicInteger produced;
	private Pool<T> pool;
	
	public Producer(Pool<T> pool, int amount, AtomicInteger produced) {
		this.pool = pool;
		this.amount = amount;
		this.produced = produced;
	}

	public void run() {
		for (int i=0; i<amount; i++){
			Integer item = produced.get();
			pool.put((T)item);
			produced.incrementAndGet();
		}
	}

}