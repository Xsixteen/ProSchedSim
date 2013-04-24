package fps;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;
import javax.swing.JList;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class processStat extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private Scheduler sch;
	private JLabel label = new JLabel("0");
	private JLabel label_1 = new JLabel("0");
	private JLabel label_2 = new JLabel("0");
	private JLabel label_3 = new JLabel("0");
	private JLabel label_4 = new JLabel("New label");
	private JTextPane textPane = new JTextPane();
	private final Timer timer;



	private processStat ref;

	/**
	 * Create the dialog.
	 */
	public processStat(Scheduler s, ArrayList<processControlBlock> a) {
		sch = s;
		ref = this;
		
		label_4.setText(s.getType()); 
		DefaultListModel listModel;

		
		listModel = new DefaultListModel();
		timer = new Timer(15, this);
		timer.start();
		setBounds(100, 100, 647, 509);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(26, 118, 301, 139);
		contentPanel.add(panel);
		panel.setLayout(null);
		{
			JLabel lblProcessCompleted = new JLabel("Process Completed:");
			lblProcessCompleted.setBounds(26, 48, 124, 16);
			panel.add(lblProcessCompleted);
		}
		{
			JLabel lblCpuBursts = new JLabel("CPU Bursts:");
			lblCpuBursts.setBounds(78, 76, 72, 16);
			panel.add(lblCpuBursts);
		}
		label_1.setBounds(164, 76, 99, 16);
		panel.add(label_1);
		label.setBounds(164, 48, 61, 16);
		panel.add(label);
		
		JLabel lblContextSwitch = new JLabel("Context Switches:");
		lblContextSwitch.setBounds(37, 20, 113, 16);
		panel.add(lblContextSwitch);
		
		label_2.setBounds(164, 20, 61, 16);
		panel.add(label_2);
		
		JLabel lblAvgWaitingTime = new JLabel(" Avg. Waiting Time:");
		lblAvgWaitingTime.setBounds(26, 99, 124, 16);
		panel.add(lblAvgWaitingTime);
		

		label_3.setBounds(164, 99, 61, 16);
		panel.add(label_3);
		
		for(processControlBlock p : a) {
			listModel.addElement("Process: " + p.getpid() + " Burst Size:" + p.getInitialBursts());
		}
		
		JLabel lblProcessList = new JLabel("Original Process List");
		lblProcessList.setBounds(419, 91, 160, 16);
		contentPanel.add(lblProcessList);
		
		JLabel lblStatistics = new JLabel("Statistics");
		lblStatistics.setBounds(129, 91, 79, 16);
		contentPanel.add(lblStatistics);
		
		JLabel lblSimulationResults = new JLabel("Simulation Results");
		lblSimulationResults.setFont(new Font("Papyrus", Font.PLAIN, 28));
		lblSimulationResults.setBounds(186, 6, 265, 44);
		contentPanel.add(lblSimulationResults);
		
		JList list = new JList(listModel);
		list.setBounds(361, 88, 256, 141);
		contentPanel.add(list);
		
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(363, 116, 256, 143);
		contentPanel.add(scrollPane);
		
		JLabel lblSimulationOutput = new JLabel("Simulation Output");
		lblSimulationOutput.setBounds(262, 271, 147, 16);
		contentPanel.add(lblSimulationOutput);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setBounds(26, 297, 593, 145);
		contentPanel.add(scrollPane_1);
		
		scrollPane_1.setViewportView(textPane);
		
	
		label_4.setBounds(196, 46, 213, 16);
		contentPanel.add(label_4);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						timer.stop();
						ref.setVisible(false);
						ref.dispose();
					
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		label.setText(Integer.toString(sch.getCompletedProcesses()));
		label_1.setText(Integer.toString(sch.getCPUBursts()));
		label_2.setText(Integer.toString(sch.getContextSwitch()));
		label_3.setText(Integer.toString(sch.avgWaitingTime()));
		textPane.setText(sch.textOutput());
		if(!sch.isRunning()) {
			timer.stop();
		}
	}
}
