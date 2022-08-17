/**
 * This program uses a Pool implemented with two lock-free queues.
 * Four threads produce and put items on pool, and other four consume them.
 * 
 * @author Gastón Scilingo
 */

package Concurrency.TP1;

import java.util.concurrent.atomic.AtomicInteger;

import tp1ex1.Consumer;
import tp1ex1.Pool;
import tp1ex1.Producer;

public class MainExercise1 {

	public static void main(String[] args) {

		Pool<Integer> pool = new Pool<Integer>();
		int amountToProduce = 30000000;
		int producerThreadsNumber = 4;
		int totalToConsume = amountToProduce * producerThreadsNumber; 
		Thread [] threads = new Thread [producerThreadsNumber*2];
		AtomicInteger consumed = new AtomicInteger(0);
		AtomicInteger produced = new AtomicInteger(0);

		for(int i = 0; i < threads.length; i++) {
			threads[i] = new Thread (new Producer<Integer>(pool,amountToProduce,produced));
			i++;
			threads[i] = new Thread (new Consumer<Integer>(pool,totalToConsume,consumed)); 
		}
		// start threads
		for (Thread t : threads) {
			t.start();
		}
			
		try {
			while(consumed.get()<totalToConsume){
				System.out.println("Pool size: "+pool.size());
				System.out.println("Produced : "+produced.get());
				System.out.println("Consumed : "+consumed.get());
				Thread.sleep(1000);
			}
			for (Thread t : threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// final report
		System.out.println("Pool size: "+pool.size());
		System.out.println("Produced : "+produced.get());
		System.out.println("Consumed : "+consumed.get());
	}
}
