/**
 * This program ...
 * 
 * 
 * @author Gastón Scilingo
 */

package Concurrency.TP1;

import tp1Ex2.Client;
import tp1Ex2.Controller;
import tp1Ex2.Fail2ban;

public class MainExercise2 {

	public static void main(String[] args) throws InterruptedException{

		int clientsThreadsNumber = 2000; // equals to valid addresses too
		int controllersThreadsNumber = 100;
		Thread [] clientsThreads = new Thread [clientsThreadsNumber];
		Thread [] controllersThreads = new Thread [controllersThreadsNumber];
		int operations = 400; // random operations per controller
		int attempts = 100000; // connection attempts per thread
		int initialBanned = 0;
		int initialExceptions = 0;
		int timeout = 1000;
		Fail2ban<Integer> system = new Fail2ban<Integer>(timeout);


		// setup system
		for (int i = 0; i < initialBanned; i++) {
			system.addBanned(i);
		}
		for (int i = initialBanned; i < initialBanned + initialExceptions; i++) {
			system.addExceptions(i);
		}
		system.report();
		// build clients 
		for (int i = 0; i < clientsThreadsNumber; i++) {
			clientsThreads[i] = new Thread(new Client<Integer>(system, i, attempts));
		}
		// build controllers
		for (int i = 0; i < controllersThreadsNumber; i++) {
			controllersThreads[i] = new Thread(new Controller<Integer>(system,clientsThreadsNumber,operations));
		}
		// start threads
		for (Thread t : controllersThreads) {
			t.start();
		}
		for (Thread t : clientsThreads) {
			t.start();
		}
		int totalConnectionsAttempts = clientsThreadsNumber*attempts;
		while (system.connections()+system.refused() < totalConnectionsAttempts) {
			System.out.println("Current threads: "+Thread.activeCount()+"\nTotal attempts: "+(system.connections()+system.refused()));
			System.out.println(system.report());			
			Thread.sleep(1000);// wait for cleaners
		}
		// join for threads
		for (Thread t : clientsThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (Thread t : controllersThreads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("\n\n\n End connections attempts");
		System.out.println("Cleaner threads: "+Thread.activeCount());
		System.out.println("Total client attempts: "+(system.connections()+system.refused())+"\n\n\n");
		
		System.out.println("Wait for end of timeouts threads");
		while (system.banned().size() > 0) {
			if (Thread.activeCount()<=1) {
				break;
			}
			System.out.println("Number of threads: "+Thread.activeCount());
			System.out.println(system.report());
			Thread.sleep(1000);// wait for cleaners
		}
		System.out.println("Number of threads: "+Thread.activeCount());
		System.out.println(system.report());
	}
	
}
