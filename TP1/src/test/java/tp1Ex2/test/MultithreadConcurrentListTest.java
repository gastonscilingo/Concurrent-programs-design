/**
 * 
 */
package tp1Ex2.test;

import static org.junit.Assert.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tp1Ex2.ConcurrentList;

/**
 * @author gaston
 *
 */
public class MultithreadConcurrentListTest {
	
	ConcurrentList<Integer> list;
	Object limit;
	int amount;
	int threadsNumber;
	int total;
	AtomicInteger count;
	AtomicInteger produced; 
	
	Thread [] threads;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		list = new ConcurrentList<Integer>();
		limit = new Object();
		amount = /*30000; 300000;*/  3000000; // 30000000;
		threadsNumber = 4;
		total = amount * threadsNumber;
		count = new AtomicInteger(0);
		produced = new AtomicInteger(0);
		threads = new Thread [threadsNumber*2];
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		list = null;
	}

	@Test
	public void testMultiInsertAndDelete() {
		
		// create threads	
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread (new Producer<Integer>(list,amount,produced));
			i++;
			threads[i] = new Thread (new Consumer<Integer>(list,count,amount)); 
		}
		// start threads
		for (Thread t : threads) {
			t.start();
		}

		try {
			while(count.get()<total){
				System.out.println("list size: "+list.size());
				System.out.println("produced : "+produced.get());
				System.out.println("consumed : "+count.get());
				Thread.sleep(2000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("produced : "+produced.get());
		System.out.println("consumed : "+count.get());	
		
		assertTrue(count.get() == total && list.size() == 0  && list.repOk());
	}
	
	
	
	public class Producer<T> implements Runnable {

		private ConcurrentList<Integer> list;
		private int amount;
		private AtomicInteger produced;
		
		public Producer(ConcurrentList<Integer> list, int amount, AtomicInteger produced) {
			this.list = list;
			this.amount = amount;
			this.produced = produced;
		}	
		
		@Override
		public void run() {
			for (int i =  0; i<amount; i++){
				produced.incrementAndGet();
				list.add(new Integer(i));
//				if (list.size() > 20000000) { search to remove becomes slow when grow up beyond 
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				}
			}
		}
	}
	
	
	public class Consumer<T> implements Runnable {
		
		private ConcurrentList<Integer> list;
		private AtomicInteger count;
		private int totalToConsume;
		
		public Consumer(ConcurrentList<Integer> list, AtomicInteger count, int toConsume) {
			this.list = list;
			this.count = count;
			this.totalToConsume = toConsume;
		}	
		
		@Override
		public void run() {
			int i = 0;
			while (i < totalToConsume){
				if (list.remove(new Integer(i))) {
					count.incrementAndGet();
					i++;
				}
			}
		}
		
	}
	

}
