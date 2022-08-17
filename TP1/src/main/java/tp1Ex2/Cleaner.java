package tp1Ex2;

public class Cleaner<T> implements Runnable {
	
	private ConcurrentList<T> list;
	private int timeout;
	private T address;

	public Cleaner(ConcurrentList<T> banned, T address, int timeout) {
		this.list = banned;
		this.address = address;
		this.timeout = timeout;
	}


	@Override
	public void run() {
		try {
			Thread.sleep(timeout);
			list.remove(address);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
