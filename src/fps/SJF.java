package fps;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.Timer;

public class SJF implements Scheduler {
	
	private ArrayList<processControlBlock> queue = new ArrayList<processControlBlock>();
	private int quantum;
	private int elapsedBursts = 0;
	private int processComp = 0;
	private int contextSwitch = 0;
	private String output = "";
	private boolean run;
	private int avgWait = 0;
	private int pos=0;
	private boolean real_time;
	private Timer t;
	private String sched_name = "Shortest Job First";

	public SJF (boolean rt) {
		output = "Shortest Job First Scheduler Initalized";


		real_time = rt;


	}


	public void sort() {
		Collections.sort(queue,  new Comparator<processControlBlock>() {
		    public int compare(processControlBlock p1, processControlBlock p2) {
		    	if(p1.getBurst() > p2.getBurst()) {
		    		return 1;
		    	} else {
		    		return 0;
		    	}
		    }
		});
	}
	
	public ArrayList<processControlBlock> getList() {
		return queue;
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
		
			for(processControlBlock pcb : queue) {
				System.out.println("Preparing to Run Pid: " + pcb.getpid() + " which has remaining burst of: " + pcb.getBurst() + " Has been waiting: " + elapsedBursts);
				output += "Preparing to Run Pid: " + pcb.getpid() + " which has remaining burst of: " + pcb.getBurst() + " Has been waiting: " + elapsedBursts + "\n";
				avgWait += elapsedBursts;
				this.runScheduler(pcb);
				contextSwitch++;
			}
		}
	}

	@Override
	public void addToQueue(ArrayList<processControlBlock> ready_queue) {
		queue.addAll(ready_queue);
		sort();

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
	public int avgWaitingTime() {
		// TODO Auto-generated method stub
		return Math.round(avgWait/contextSwitch);
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
	public String getType() {
		// TODO Auto-generated method stub
		return sched_name;
	}
	
	

}

