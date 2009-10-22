/*
 * Created on Sat Apr 25 19:43:50 CEST 2009
 */

package net.tasktrck.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.tasktrck.model.Project;
import net.tasktrck.model.Task;

/**
 * @author LB
 */
/**
 * @author Gebruiker
 */
public class TaskEnd extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -9006012589307368460L;
	private static Object[] value = null;
	private static TaskEnd dialog;

	public TaskEnd(Project p, Task t, long start)
	{
		super(new JFrame(), "Start Task", true);
		initComponents();

		projectField.setText(p.toString());
		notesArea.setText(t.getNotes());

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
		dateField.setText(sdf.format(new Date(start)));

		taskBox.setModel(new DefaultComboBoxModel(p.getTaskList().toArray()));
		taskBox.setSelectedItem(t);

		endButton.setActionCommand("End");
		endButton.addActionListener(this);
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(this);
		setAlwaysOnTop(true);
	}

	/**
	 * @param p
	 * @param t
	 * @param start
	 * @return Object[] { Bool finished, task, notes }
	 */
	public static Object[] showDialog(Project p, Task t, long start)
	{
		dialog = new TaskEnd(p, t, start);
		dialog.setVisible(true);
		return value;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if ("End".equals(e.getActionCommand()))
		{
			TaskEnd.value = new Object[] { true, taskBox.getSelectedItem(), notesArea.getText() };
		} else if ("Cancel".equals(e.getActionCommand()))
		{
			TaskEnd.value = new Object[] { false };
		}
		dialog.setVisible(false);
	}

	private void initComponents()
	{
		// Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		projectLabel = new JLabel();
		projectField = new JTextField();
		startLabel = new JLabel();
		dateField = new JTextField();
		taskLabel = new JLabel();
		taskBox = new JComboBox();
		notesLabel = new JLabel();
		notesScrollPane = new JScrollPane();
		notesArea = new JTextArea();
		endButton = new JButton();
		cancelButton = new JButton();

		// ======== this ========
		setTitle("End Task");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 15, 80, 115, 26, 115,
				95, 10, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 15, 0, 0, 0, 55, 35, 0,
				10, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 0.0, 0.0, 0.0,
				0.0, 0.0, 1.0, 0.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 1.0E-4 };

		// ---- projectLabel ----
		projectLabel.setText("Project:");
		contentPane.add(projectLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- projectField ----
		projectField.setEditable(false);
		contentPane.add(projectField, new GridBagConstraints(2, 1, 4, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- startLabel ----
		startLabel.setText("Started on:");
		contentPane.add(startLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- dateField ----
		dateField.setEditable(false);
		contentPane.add(dateField, new GridBagConstraints(2, 2, 4, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- taskLabel ----
		taskLabel.setText("Label:");
		contentPane.add(taskLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- taskBox ----
		taskBox.setEditable(true);
		contentPane.add(taskBox, new GridBagConstraints(2, 3, 4, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- notesLabel ----
		notesLabel.setText("Notes:");
		contentPane.add(notesLabel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ======== notesScrollPane ========
		{
			notesScrollPane
					.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			notesScrollPane
					.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			notesScrollPane.setViewportView(notesArea);
		}
		contentPane.add(notesScrollPane, new GridBagConstraints(2, 4, 4, 2, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- endButton ----
		endButton.setText("End Task");
		contentPane.add(endButton, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));

		// ---- cancelButton ----
		cancelButton.setText("Cancel Task");
		contentPane.add(cancelButton, new GridBagConstraints(4, 6, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5, 5), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// End of component initialization //GEN-END:initComponents
	}

	// Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	private JLabel projectLabel;
	private JTextField projectField;
	private JLabel startLabel;
	private JTextField dateField;
	private JLabel taskLabel;
	private JComboBox taskBox;
	private JLabel notesLabel;
	private JScrollPane notesScrollPane;
	private JTextArea notesArea;
	private JButton endButton;
	private JButton cancelButton;
	// End of variables declaration //GEN-END:variables

}
