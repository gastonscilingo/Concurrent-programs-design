package tp1ex1.tests;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tp1ex1.Pool;

public class PoolTest {
	
	int total;
	Pool<Integer> pool;
	AtomicInteger count;
	Thread [] threads;
	int producerThreadsNumber;

	@Before
	public void setUp() throws Exception {
		pool = new Pool<Integer>();
		count = new AtomicInteger(0);
	}

	@After
	public void tearDown() throws Exception {
		count = new AtomicInteger(0);
	}

	@Test
	public void test() {
		int amount = 1000;
		total = 4000;
		producerThreadsNumber = 4;
		threads = new Thread [producerThreadsNumber*2];
		// 
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread (new Producer<Integer>(pool,amount));
			i++;
			threads[i] = new Thread (new Consumer<Integer>(pool,count,total)); 
		}
		// start threads
		for (Thread t : threads) {
			t.start();
		}
		// wait to finish 
		try {
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(pool.repOk());
		assertEquals(0,pool.size());
		assertTrue(count.get() == total );
	}
	
	@Test
	public void test2() {
		
		int amount = 3000000;
		total = 12000000;
		producerThreadsNumber = 4;
		threads = new Thread [producerThreadsNumber*2];

		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread (new Producer<Integer>(pool,amount));
			i++;
			threads[i] = new Thread (new Consumer<Integer>(pool,count,total)); 
		}
		// start threads
		for (Thread t : threads) {
			t.start();
		}
		// wait to finish 
		try {
			while(count.get()<total){
				System.out.println("Pool size: "+pool.size());
				System.out.println("Consumed : "+count.get());
				Thread.sleep(2000);
			}
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Pool size: "+pool.size());
		System.out.println("Consumed : "+count.get());
		assertTrue(pool.repOk());
		assertEquals(0,pool.size());
		assertTrue(count.get() == total);
	}
	
	
	public class Producer<T> implements Runnable {

		Pool<T> pool;
		int amount;
		
		public Producer(Pool<T> pool, int amount) {
			this.pool = pool;
			this.amount = amount;
		}	
		
		public void run() {
			for (int i =  0; i<amount; i++){
				pool.put((T) new Integer(i));
			}
		}
	}
	
	
	public class Consumer<T> implements Runnable {
		
		Pool<T> pool;
		AtomicInteger count;
		int total;
		
		public Consumer(Pool<T> pool, AtomicInteger count,int total) {
			this.pool = pool;
			this.count = count;
			this.total = total;
		}	
		
		public void run() {
			while (count.get()<total){
				T item = pool.get();
				if(item!=null){
						count.incrementAndGet();
				}			
			}
		}


	}
	
	
	
	

}
