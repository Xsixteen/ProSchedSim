package fps;

public class processControlBlock {

	private int pid;
	private int state;
	private int size;
	private int cpuBurst;
	private int initialBurst;
	private int lastBurst;
	private boolean debug = true;
	
	//States
	public int state_new = 1;
	public int state_running = 2;
	public int state_waiting = 3;
	public int state_ready = 4;
	public int state_terminated = 5;
	
	//constructor
	processControlBlock (int arg_pid, int burst) {
		pid = arg_pid;
		initialBurst = burst;
		cpuBurst = burst;
		if(debug) {
			System.out.println("Process Created with pid: " + pid + " and cpu burst of: " + cpuBurst);
		}
	}
	
	int getState() { return state; }
	
	void setState(int s) {
		state = s;
	}
	
	int getpid() { return pid; }
	
	int getBurst() { return cpuBurst; }
	
	void subtractBurst(int b) {
		cpuBurst = cpuBurst - b;
	}
	
	void setLastBurst( int b) { lastBurst = b; }
	
	int getWait(int curBurst) {
		return curBurst - lastBurst;
	}
	
	int getInitialBursts() {
		return initialBurst;
	}
	
	void reset() {
		cpuBurst = initialBurst;
	}
	
}
