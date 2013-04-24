package fps;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;

public class FPMain {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JCheckBox chckbxNewCheckBox;
	private JCheckBox chckbxRealTimeSimulation = new JCheckBox("Real Time Simulation");
	private Scheduler sched;
	private ArrayList<processControlBlock> ready_queue = new ArrayList<processControlBlock>();
	private int pid = 0;
	private boolean debug = true;
	private JTextField textField_3;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FPMain window = new FPMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FPMain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 462, 417);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(222, 113, 58, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
 
		JLabel lblNumberOfProcesses = new JLabel("Number of Processes:\n");
		lblNumberOfProcesses.setBounds(79, 119, 141, 16);
		frame.getContentPane().add(lblNumberOfProcesses);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Round Robin", "First Come First Serve", "SJF"}));
		comboBox.setBounds(222, 74, 130, 27);
		frame.getContentPane().add(comboBox);
		
		JLabel lblSchedulerType = new JLabel("Scheduler Type:");
		lblSchedulerType.setBounds(115, 78, 105, 16);
		frame.getContentPane().add(lblSchedulerType);
		
		JLabel lblCpuBurstRange = new JLabel("CPU Burst Range:");
		lblCpuBurstRange.setBounds(113, 154, 107, 16);
		frame.getContentPane().add(lblCpuBurstRange);
		
		textField_1 = new JTextField();
		textField_1.setBounds(222, 148, 46, 28);
		frame.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setBounds(280, 154, 18, 16);
		frame.getContentPane().add(lblTo);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(306, 148, 46, 28);
		frame.getContentPane().add(textField_2);
		
		JLabel lblMin = new JLabel("Min");
		lblMin.setBounds(232, 175, 33, 16);
		frame.getContentPane().add(lblMin);
		
		JLabel lblMax = new JLabel("Max");
		lblMax.setBounds(316, 175, 33, 16);
		frame.getContentPane().add(lblMax);
		
		final JButton btnSimulate = new JButton("Simulate");
		btnSimulate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getSource() == btnSimulate) {
					
					//Check to make sure that the # of processes and range is filled out.
					if(textField_1.getText().trim().equals("") || textField_2.getText().trim().equals("") || textField.getText().trim().equals("")) {
						JOptionPane.showMessageDialog(frame, "Please specify number of processes and process range.");   
					}	else	{
						processControlBlock[] p = new processControlBlock[Integer.parseInt(textField.getText().toString())];
						//Random # gen with random seed
						Random r = new Random( 19527 );
						
						if(chckbxNewCheckBox.isSelected()) {
							//Reset the initial vs remaining bursts
							for(processControlBlock pcb : ready_queue) {
									pcb.reset();
							}
							
						} else {
							chckbxNewCheckBox.setEnabled(true);
							//Clean the Queue!
							ready_queue.clear();
							for(int i = 0; i < Integer.parseInt(textField.getText().toString()); i ++ ) {
							//First create PCB's and put them in the ready queue
								int burst = r.nextInt(Integer.parseInt(textField_2.getText().toString())) + (Integer.parseInt(textField_1.getText().toString()));
								p[i]  = new processControlBlock(pid, burst);
								ready_queue.add(p[i]);
								pid++;
							}
						}
						
						String val = (String) comboBox.getSelectedItem();
					
						if(val.equals("Round Robin"))
						{
							if(textField_3.getText().trim().equals("")) {
								JOptionPane.showMessageDialog(frame, "Please specify a quantum for round robin scheduler.");   
							} else {
								//RR with 250 ms quantum
								sched = new Roundrobin(Integer.parseInt(textField_3.getText()),chckbxRealTimeSimulation.isSelected());
								sched.addToQueue(ready_queue);
								sched.dispatch();
								processStat ps = new processStat(sched, ready_queue);
								ps.setVisible(true);
							}
								
						} else if (val.equals("First Come First Serve")) {
							sched = new FCFS(chckbxRealTimeSimulation.isSelected());
							sched.addToQueue(ready_queue);
							sched.dispatch();
							processStat ps = new processStat(sched, ready_queue);
							ps.setVisible(true);
							
						} else if (val.equals("SJF")) {
							sched = new SJF(chckbxRealTimeSimulation.isSelected());
							sched.addToQueue(ready_queue);
							sched.dispatch();
							processStat ps = new processStat(sched,((SJF) sched).getList());
							ps.setVisible(true); 
						} 
					
					}
				}
			
			}
		});
		btnSimulate.setBounds(40, 360, 117, 29);
		frame.getContentPane().add(btnSimulate);
		
		JLabel lblProcessSchedulingSimulator = new JLabel("Process Scheduling Simulator");
		lblProcessSchedulingSimulator.setFont(new Font("Papyrus", Font.PLAIN, 25));
		lblProcessSchedulingSimulator.setBounds(67, 23, 336, 39);
		frame.getContentPane().add(lblProcessSchedulingSimulator);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(40, 102, 363, 148);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblRoundRobinQuantum = new JLabel("Round Robin Quantum:");
		lblRoundRobinQuantum.setBounds(29, 104, 153, 16);
		panel.add(lblRoundRobinQuantum);
		
		textField_3 = new JTextField();
		textField_3.setText("25");
		textField_3.setBounds(183, 98, 50, 28);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnClose.setBounds(286, 360, 117, 29);
		frame.getContentPane().add(btnClose);
		
		JButton btnSimulateAllAlgorithms = new JButton("Simulate All");
		btnSimulateAllAlgorithms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textField_1.getText().trim().equals("") || textField_2.getText().trim().equals("") || textField.getText().trim().equals("")) {
					JOptionPane.showMessageDialog(frame, "Please specify number of processes and process range.");   
				}	else	{
					processControlBlock[] p = new processControlBlock[Integer.parseInt(textField.getText().toString())];
					//Random # gen with random seed
					Random r = new Random( 19527 );
					//Clean the Queue!
					ready_queue.clear();
					
					for(int i = 0; i < Integer.parseInt(textField.getText().toString()); i ++ ) {
					//First create PCB's and put them in the ready queue
						int burst = r.nextInt(Integer.parseInt(textField_2.getText().toString())) + (Integer.parseInt(textField_1.getText().toString()));
						p[i]  = new processControlBlock(pid, burst);
						ready_queue.add(p[i]);
						pid++;
					}
					
		
						
						sched = new Roundrobin(Integer.parseInt(textField_3.getText()), chckbxRealTimeSimulation.isSelected());
						sched.addToQueue(ready_queue);
						sched.dispatch();
						processStat ps = new processStat(sched, ready_queue);
						ps.setVisible(true);
							
						sched = new FCFS(chckbxRealTimeSimulation.isSelected());
						sched.addToQueue(ready_queue);
						sched.dispatch();
					    ps = new processStat(sched, ready_queue);
						ps.setVisible(true);
						
						sched = new SJF(chckbxRealTimeSimulation.isSelected());
						sched.addToQueue(ready_queue);
						sched.dispatch();
						ps = new processStat(sched,((SJF) sched).getList());
						ps.setVisible(true); 
				}
			}
		
		});
		btnSimulateAllAlgorithms.setBounds(163, 360, 117, 29);
		frame.getContentPane().add(btnSimulateAllAlgorithms);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(40, 262, 363, 59);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		chckbxNewCheckBox = new JCheckBox("Do not Generate new Processes");
		chckbxNewCheckBox.setBounds(19, 6, 229, 23);
		panel_1.add(chckbxNewCheckBox);
		
		chckbxNewCheckBox.setEnabled(false);
		chckbxRealTimeSimulation.setSelected(true);
		
		
		chckbxRealTimeSimulation.setBounds(19, 27, 219, 23);
		panel_1.add(chckbxRealTimeSimulation);
		

	}
}
