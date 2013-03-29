package edu.asupoly.aspira.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class AdministratorView extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5485014204648698376L;

	public  AdministratorView(){
		super(new GridLayout(1,1));
		JTabbedPane tp = new JTabbedPane();
		JButton changeReadingsButton = new JButton("Change");
		JButton savePatientInfo = new JButton("Save");
		JButton cancelPatientInfo = new JButton("Cancel");
		JRadioButton medicationYes = new JRadioButton("Yes");
		JRadioButton medicationNo = new JRadioButton("No");
		JScrollPane readTimeArea = new JScrollPane();
		JScrollPane medTimeArea = new JScrollPane();
		JLabel pidLabel = new JLabel("Patient id: ");
		JLabel readNumLabel = new JLabel("Readings per day: ");
		JLabel medCheckLabel = new JLabel("Medication Reminder");
		JTextField pidField = new JTextField();
		JTextField readNumField = new JTextField("3");
		JPanel pidPanel = new JPanel();
		JPanel readPanel = new JPanel();
		JPanel medPanel = new JPanel();
		JPanel finishedPanel = new JPanel();
		JPanel patientPanel = new JPanel();
		JPanel logPanel = new JPanel();
		JPanel configPanel = new JPanel();
		patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS));
		
		ButtonGroup YesNogroup = new ButtonGroup();
		YesNogroup.add(medicationYes);
		YesNogroup.add(medicationNo);
		
		pidPanel.setLayout(new FlowLayout());
		pidPanel.add(pidLabel);
		pidPanel.add(pidField);
		
		readPanel.setLayout(new FlowLayout());
		readPanel.add(readNumLabel);
		readPanel.add(readNumField);
		readPanel.add(changeReadingsButton);
		
		medPanel.setLayout(new FlowLayout());
		medPanel.add(medCheckLabel);
		medPanel.add(medicationYes);
		medPanel.add(medicationNo);
		
		finishedPanel.setLayout(new FlowLayout());
		finishedPanel.add(savePatientInfo);
		finishedPanel.add(cancelPatientInfo);
		
		patientPanel.add(pidPanel);
		patientPanel.add(readPanel);
		patientPanel.add(readTimeArea);
		patientPanel.add(medPanel);
		patientPanel.add(medTimeArea);
		patientPanel.add(finishedPanel);
		
		tp.addTab("Patient", patientPanel);
		tp.addTab("App config", configPanel);
		tp.addTab("Logs", logPanel);
		
		add(tp);
	}
	
	private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Administrative settings");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add content to the window.
        frame.add(new AdministratorView(), BorderLayout.CENTER);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
	
	public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
    }
    

}
