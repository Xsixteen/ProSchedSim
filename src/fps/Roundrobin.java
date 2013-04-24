package fps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Roundrobin implements Scheduler {

	private ArrayList<processControlBlock> queue = new ArrayList<processControlBlock>();
	private ArrayList<processControlBlock> ready_queue = new ArrayList<processControlBlock>();
	private int quantum;
	private int elapsedBursts = 0;
	private int processComp = 0;
	private int contextSwitch = 0;
	private String output = "";
	private boolean run;
	private int avgWait = 0;
	private String sched_name = "Round Robin";
	private boolean real_time;
	private Timer t;
	private int pos=0;


	
	//q is RR quantum
	public Roundrobin(int q, boolean rt) {
		System.out.println("Round Robin Scheduler Initialized with Time Quantum: " + q);
		output += "Round Robin Scheduler Initialized with Time Quantum: " + q + "\n";
		quantum = q;
		real_time = rt;
	}
	
	public void runScheduler(processControlBlock p) {
		//if(p.getBurst() )
		if(p.getBurst() > quantum) {
			p.subtractBurst(quantum);
			elapsedBursts += quantum;
			p.setLastBurst(elapsedBursts);
			ready_queue.add(p);
		} else {
			elapsedBursts += p.getBurst();
			p.setLastBurst(elapsedBursts);
			processComp++;
		}

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
							t.setDelay(quantum);
							dispatch(p);
							
						} else {
							//clear 
							queue.clear();
							t.stop();
							pos = 0;
							if(ready_queue.isEmpty()) {
								//if there is no more processes exit
								System.out.println("Completed Run!  No more Processes");
								run = false;
								output += "Completed Run! No more Processes\n";
								System.out.println("The Scheduler has completed: " + processComp + " processes. " + elapsedBursts + " bursts have occured." );
								output += "The Scheduler has completed: " + processComp + " processes. " + elapsedBursts + " bursts have occured.\n";
							} else {
								//re-add all valid pcb's to the queue
								queue.addAll(ready_queue);
								//Clear the ready queue so it can repopulated
								ready_queue.clear();
								t.start();
							}
						}
		
					 }
				});
			t.start();
		} else {
				System.out.println("The Scheduler has completed: " + processComp + " processes. " + elapsedBursts + " bursts have occured." );
				output += "\n\nThe Scheduler has completed: " + processComp + " processes. " + elapsedBursts + " bursts have occured.\n";
			
			// TODO Auto-generated method stub
			for(processControlBlock pcb : queue) {
				System.out.println("Preparing to Run Pid: " + pcb.getpid() + " which has remaining burst of: " + pcb.getBurst() + " Has been waiting: " + pcb.getWait(elapsedBursts));
				output += "Preparing to Run Pid: " + pcb.getpid() + " which has remaining burst of: " + pcb.getBurst() + " Has been waiting: " + pcb.getWait(elapsedBursts) + "\n";
				avgWait += pcb.getWait(elapsedBursts);
				this.runScheduler(pcb);
				contextSwitch++;
			}
			
			//clear 
			queue.clear();
			if(ready_queue.isEmpty()) {
				//if there is no more processes exit
				System.out.println("Completed Run!  No more Processes");
				run = false;
				output += "Completed Run! No more Processes\n";
				System.out.println("The Scheduler has completed: " + processComp + " processes. " + elapsedBursts + " bursts have occured." );
				output += "The Scheduler has completed: " + processComp + " processes. " + elapsedBursts + " bursts have occured.\n";
				
			} else {
				//re-add all valid pcb's to the queue
				queue.addAll(ready_queue);
				//Clear the ready queue so it can repopulated
				ready_queue.clear();
				this.dispatch();
			}
		}
	}
	public int getCompletedProcesses() {
		return processComp;
	}
	public void addToQueue(ArrayList<processControlBlock> q) {
		queue.addAll(q);
	}

	@Override
	public int getCPUBursts() {
		return elapsedBursts;
	}

	@Override
	public String textOutput() {
		// TODO Auto-generated method stub
		return output;
	}
	
	public boolean isRunning() {
		return run;
	}

	@Override
	public int getContextSwitch() {
		// TODO Auto-generated method stub
		return contextSwitch;
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




