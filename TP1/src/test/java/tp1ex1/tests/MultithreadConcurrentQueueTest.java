/**
 * 
 */
package tp1ex1.tests;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tp1ex1.ConcurrentQueue;

/**
 * @author gaston
 *
 */
public class MultithreadConcurrentQueueTest {
	
	ConcurrentQueue<Integer> queue;
	Thread [] threads;
	int producerThreadsNumber;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		queue = new ConcurrentQueue<Integer>();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMultiInsertAndDelete() {
		int amount = 30000000;
		producerThreadsNumber = 4;
		int total = amount * producerThreadsNumber;
		AtomicInteger count = new AtomicInteger(0);
		AtomicInteger produced = new AtomicInteger(0);

		
		threads = new Thread [producerThreadsNumber * 2];
		// 
		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread (new Producer<Integer>(queue,amount,produced));
			i++;
			threads[i] = new Thread (new Consumer<Integer>(queue,count,total)); 
		}
		// start threads
		for (Thread t : threads) {
			t.start();
		}
		try {
			while(count.get()<total){
				System.out.println("queue size: "+queue.size());
				System.out.println("produced : "+produced.get());
				System.out.println("consumed : "+count.get());
				Thread.sleep(2000);
			}
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("queue size: "+queue.size());
		System.out.println("produced : "+produced.get());
		System.out.println("consumed : "+count.get());
		assertEquals(produced.get(),count.get());
		assertTrue(count.get() == total);
		assertEquals(0,queue.size());
	}
	
	
	
	public class Producer<T> implements Runnable {

		private ConcurrentQueue<T> queue;
		int amount;
		private AtomicInteger produced;
		
		public Producer(ConcurrentQueue<T> queue, int amount, AtomicInteger produced) {
			this.queue = queue;
			this.amount = amount;
			this.produced = produced;
		}	
		
		public void run() {
			for (int i =  0; i<amount; i++){
				produced.incrementAndGet();
				queue.insert((T)new Integer(i));
			}
		}
	}
	
	
	public class Consumer<T> implements Runnable {
		
		private ConcurrentQueue<T> queue;
		private AtomicInteger count;
		int total;
		
		public Consumer(ConcurrentQueue<T> queue, AtomicInteger count,int total) {
			this.queue = queue;
			this.count = count;
			this.total = total;
		}	
		
		public void run() {
			//int countNulls = 0;
			while (count.get()<total){
				T item = queue.poll();
				if(item!=null){
						count.incrementAndGet();
				}
				else{
					//Thread.yield();
					//System.out.println("thread saco null nº: "+(++countNulls));
				}
			}
		}


	}
	

}
