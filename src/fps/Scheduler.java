package fps;

import java.util.ArrayList;

public interface Scheduler {
	void runScheduler(processControlBlock p);
	void dispatch();
	void addToQueue(ArrayList<processControlBlock> ready_queue);
	final boolean debug = true;
	public int getCompletedProcesses();
	public int getCPUBursts();
	public int getContextSwitch();
	public int avgWaitingTime();
	public String textOutput();
	public boolean isRunning();
	public String getType();
}
