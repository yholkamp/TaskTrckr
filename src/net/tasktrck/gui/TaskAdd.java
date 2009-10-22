/*
 * Created on Sat Apr 25 01:21:26 CEST 2009
 */

package net.tasktrck.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import net.tasktrck.model.Project;
import net.tasktrck.model.Task;

/**
 * @author LB
 */
public class TaskAdd extends JDialog implements ActionListener, Serializable
{
	private static final long serialVersionUID = 8136786172100452093L;
	private static Object[] value = null;
	private static TaskAdd dialog;

	// private HashMap<Project, ComboBoxModel> projectModels;

	/**
	 * @param projects
	 */
	public TaskAdd(HashSet<Project> projects)
	{
		super(new JFrame(), "Start Task", true);
		initComponents();

		Object[] o = projects.toArray();
		Arrays.sort(o);
		projectBox.setModel(new DefaultComboBoxModel(o));

		okButton.addActionListener(this);
		projectBox.addActionListener(this);
		taskBox.addActionListener(this);
		actionPerformed(new ActionEvent(projectBox, 42, "Start"));
		setAlwaysOnTop(true);
	}

	/**
	 * @param projects
	 * @param labels
	 * @return Returns an Object[] {projectName, taskName, notes} or null if user cancelled input.
	 */
	public static Object[] showDialog(HashSet<Project> projects)
	{
		dialog = new TaskAdd(projects);
		dialog.setVisible(true);
		return value;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// System.out.println(e.getActionCommand());
		if (e.getSource().equals(okButton))
		{
			System.out.println("okButton");

			boolean error = false;
			if (projectBox.getSelectedItem() == null || taskBox.getSelectedItem() == "")
			{
				projectLabel.setForeground(Color.red);
				error = true;
			}
			if (taskBox.getSelectedItem() == null || taskBox.getSelectedItem() == "")
			{
				taskLabel.setForeground(Color.red);
				error = true;
			}
			if (error)
				return;

			TaskAdd.value = new Object[] { projectBox.getSelectedItem(), taskBox.getSelectedItem(), notesArea.getText() };
			dialog.setVisible(false);
		} else if (e.getSource().equals(projectBox))
		{
			// Render labelbox.
			if (projectBox.getSelectedItem() instanceof Project)
			{
				// Existing project, so build the model based on current tasks starting with "".
				Object[] o = ((Project) projectBox.getSelectedItem()).getTaskList().toArray();
				Arrays.sort(o);
				taskBox.setModel(new DefaultComboBoxModel(o));
			} else
			{ // New project
				taskBox.setModel(new DefaultComboBoxModel());
			}
		} else if (e.getSource().equals(taskBox))
		{
			if (taskBox.getSelectedItem() instanceof Task)
				notesArea.setText(((Task) taskBox.getSelectedItem()).getNotes());
			else
				notesArea.setText("");
		} else
		{
			System.out.println("Misc button");
			dialog.setVisible(false);
		}
	}

	private void initComponents()
	{
		// Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		projectLabel = new JLabel();
		projectBox = new JComboBox();
		taskLabel = new JLabel();
		taskBox = new JComboBox();
		notesLabel = new JLabel();
		notesScrollPane = new JScrollPane();
		notesArea = new JTextArea();
		okButton = new JButton();

		// ======== this ========
		setTitle("Start Task");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 15, 80, 115, 95, 10, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 15, 0, 0, 55, 35, 0, 10, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4 };

		// ---- projectLabel ----
		projectLabel.setText("Project:");
		contentPane.add(projectLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
				0, 5, 5), 0, 0));

		// ---- projectBox ----
		projectBox.setEditable(true);
		contentPane.add(projectBox, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));

		// ---- taskLabel ----
		taskLabel.setText("Task name:");
		contentPane.add(taskLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));

		// ---- taskBox ----
		taskBox.setEditable(true);
		contentPane.add(taskBox, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5,
				5), 0, 0));

		// ---- notesLabel ----
		notesLabel.setText("Notes:");
		contentPane.add(notesLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));

		// ======== notesScrollPane ========
		{
			notesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			notesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			notesScrollPane.setViewportView(notesArea);
		}
		contentPane.add(notesScrollPane, new GridBagConstraints(2, 3, 2, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 5, 5), 0, 0));

		// ---- okButton ----
		okButton.setText("Ok");
		contentPane.add(okButton, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// End of component initialization //GEN-END:initComponents
	}

	// Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	private JLabel projectLabel;
	private JComboBox projectBox;
	private JLabel taskLabel;
	private JComboBox taskBox;
	private JLabel notesLabel;
	private JScrollPane notesScrollPane;
	private JTextArea notesArea;
	private JButton okButton;
	// End of variables declaration //GEN-END:variables

}
