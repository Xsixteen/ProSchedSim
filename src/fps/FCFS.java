package fps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class FCFS implements Scheduler {
	
	private ArrayList<processControlBlock> queue = new ArrayList<processControlBlock>();
	private String output = "";
	private int elapsedBursts = 0;
	private int processComp = 0;
	private int contextSwitch = 0;
	private int avgWait = 0;
	private boolean real_time;
	private Timer t;
	private int pos=0;
	private boolean run;
	private String sched_name = "First Come First Serve";


	public FCFS(boolean rt) {
		output = "First Come First Serve Scheduler Initalized";

		real_time = rt;

	}
	
	@Override
	public void runScheduler(processControlBlock p) {
		elapsedBursts += p.getBurst();
		processComp++;
	}

	public void dispatch(processControlBlock p) {
		System.out.println("Preparing to Run Pid: " + p.getpid() + " which has remaining burst of: " + p.getBurst() + " Has been waiting: " + elapsedBursts);
		output += "Preparing to Run Pid: " + p.getpid() + " which has remaining burst of: " + p.getBurst() + " Has been waiting: " + elapsedBursts + "\n";
		avgWait += elapsedBursts;
		this.runScheduler(p);
		contextSwitch++;
	}
	
	@Override
	public void dispatch() {
		
		run = true;
		if(real_time) {
			t = new Timer(10, new ActionListener() {
					 public void actionPerformed(ActionEvent evt) { 
						if(pos < queue.size()) {
							processControlBlock p = queue.get(pos);
							pos++;
							t.setDelay(p.getBurst());
							dispatch(p);
						} else {
							run = false;
						}
		
					 }
				});
			t.start();
		} else {
		// TODO Auto-generated method stub
			for(processControlBlock pcb : queue) {
				System.out.println("Preparing to Run Pid: " + pcb.getpid() + " which has remaining burst of: " + pcb.getBurst() + " Has been waiting: " + pcb.getWait(elapsedBursts));
				output += "Preparing to Run Pid: " + pcb.getpid() + " which has remaining burst of: " + pcb.getBurst() + " Has been waiting: " + elapsedBursts + "\n";
				avgWait += elapsedBursts;
				this.runScheduler(pcb);
				contextSwitch++;
			}
		}
	}

	@Override
	public void addToQueue(ArrayList<processControlBlock> r) {
		// TODO Auto-generated method stub
		queue.addAll(r);

	}

	@Override
	public int getCompletedProcesses() {
		// TODO Auto-generated method stub
		return processComp;
	}

	@Override
	public int getCPUBursts() {
		// TODO Auto-generated method stub
		return elapsedBursts;
	}

	@Override
	public int getContextSwitch() {
		// TODO Auto-generated method stub
		return contextSwitch;
	}

	@Override
	public String textOutput() {
		// TODO Auto-generated method stub
		return output;
	}

	@Override
	public boolean isRunning() {
		// TODO Auto-generated method stub
		return run;
	}

	@Override
	public int avgWaitingTime() {
		// TODO Auto-generated method stub
		return Math.round(avgWait/contextSwitch);
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return sched_name;
	}

}
