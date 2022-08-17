package tp1Ex2;

import java.util.concurrent.atomic.AtomicInteger;

public class Fail2ban<T> {

	private ConcurrentList<T> banned;
	private ConcurrentList<T> allowed;
	private Integer timeout;
	private AtomicInteger connections;
	private AtomicInteger  refuse;
	private Object semaExcep;

	public Fail2ban(int timeout) {
		super();
		this.banned = new ConcurrentList<T>();
		this.allowed = new ConcurrentList<T>();
		this.timeout = timeout;
		this.connections = new AtomicInteger(0);
		this.refuse = new AtomicInteger(0);
		this.semaExcep = new Object();
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public void addBanned(T address) {
		synchronized(semaExcep) { // must block allowed until add in banned to prevent inconsistency
			if (!allowed.contains(address) && !banned.contains(address)) {
				banned.add(address);
				// launch a thread to remove by timeout
				Thread cleaner = new Thread (new Cleaner<T>(banned,address,timeout));
				cleaner.start();
			}
		}// release allowed
	}
	
	public void addExceptions(T address) {
		synchronized (semaExcep) {
			if (!allowed.contains(address)) {
				allowed.add(address);		
			}
		}
		banned.remove(address);
		
	}
	
	public boolean removeExeption(T address) {
		return allowed.remove(address);
	}
	
	public boolean isBanned(T address) {
		return banned.contains(address);
	}
	
	public boolean isException(T address) {
		return allowed.contains(address);
	}
	
	public int size(){
		return banned.size()+allowed.size();
	}
	
	public boolean repOk(){
		return banned.repOk() && allowed.repOk();
	}

	public boolean connect(T addresFrom) {
		if(isException(addresFrom)) {
			connections.incrementAndGet();
			return true; // simulate connection success
		}
		if (isBanned(addresFrom)) {
			refuse.incrementAndGet();
			return false; // simulate connection denied
		}
		connections.incrementAndGet();
		return true;
	}

	public int connections() {
		return connections.get();
	}
	
	public int refused() {
		return refuse.get();
	}
	
	public ConcurrentList<T> banned() {
		return banned;
	}
	
	public String report() {
		return "___REPORT___\n Banned: "+banned.size()+" / Exceptions: "+allowed.size()+"\n Connections: "+connections()+" / Refused: "+refused()+"\n ____________";
	}
	
	
}
