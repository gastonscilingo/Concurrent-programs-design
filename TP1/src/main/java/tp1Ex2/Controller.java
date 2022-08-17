package tp1Ex2;

import java.util.Random;

public class Controller<T> implements Runnable {

	private Fail2ban<Integer> system;
	private int operations;
	private Random random;
	private int[] addressesToWork;

	public Controller(Fail2ban<Integer> f2b, int addresses, int operations) {
		this.system = f2b;
		this.operations = operations;
		this.random = new Random();
		this.addressesToWork = new int[operations];
		
		for (int i = 0; i < addressesToWork.length; i++) {
			addressesToWork[i] = random.nextInt(addresses); 
		}
		
	}

	@Override
	public void run() {
		for (int i = 0; i < operations; i++) {
			if(random.nextBoolean()) { // banned operation
				system.addBanned(addressesToWork[i]);
			}else{ // exception operations 
				if (random.nextBoolean()) {
					system.addExceptions(addressesToWork[i]);
				}else {
					system.removeExeption(addressesToWork[i]);
				}
			}
		}
	}

}