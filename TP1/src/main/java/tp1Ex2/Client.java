package tp1Ex2;

public class Client<T> implements Runnable {

	private Fail2ban<T> system;
	private T address;
	private int attempts;

	public Client(Fail2ban<T> f2b, T address, int connectionAttempt) {
		this.system = f2b;
		this.address = address;
		this.attempts = connectionAttempt;
	}

	@Override
	public void run() {
		for (int i=0; i < attempts; i++){
			if (!system.connect(address)) {
				//System.out.println("connection refuse");
			}
		}
	}

}