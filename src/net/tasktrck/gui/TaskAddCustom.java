package net.tasktrck.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.MaskFormatter;

import net.tasktrck.model.Project;
import net.tasktrck.model.Task;
import net.tasktrck.model.TaskEntry;

import org.jdesktop.swingx.JXDatePicker;

public class TaskAddCustom extends JDialog implements ActionListener
{
	private static final long serialVersionUID = -3756066133135033556L;
	private static TaskAddCustom dialog;
	private static Object[] value;
	private static MaskFormatter mf = null;

	/**
	 * Used to add new task
	 * 
	 * @param projects
	 */
	public TaskAddCustom(HashSet<Project> projects)
	{
		super(new JFrame(), "Start Task", true);
		try
		{
			mf = new MaskFormatter("##:##:##");
		} catch (ParseException e)
		{
		}

		//Set the value to null in case the user closes the window
		super.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent ev)
			{
				TaskAddCustom.value = null;
			}
		});
		initComponents();
		init(projects);
	}

	/**
	 * Used to edit existing task
	 * 
	 * @param projects
	 * @param p
	 * @param t
	 * @param te
	 */
	public TaskAddCustom(HashSet<Project> projects, Project p, Task t, TaskEntry te)
	{
		super(new JFrame(), "Edit Task", true);
		try
		{
			mf = new MaskFormatter("##:##:##");
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		initComponents();
		init(projects);

		// Set current values
		projectBox.setSelectedItem(p);
		taskBox.setSelectedItem(t);

		if (te != null)
		{
			Date start = new Date(te.getStart());
			Date stop = new Date(te.getEnd());
			startDatePicker.setDate(start);
			stopDatePicker.setDate(stop);

			SimpleDateFormat st = new SimpleDateFormat("HH:mm:ss");
			startTimeField.setText(st.format(start));
			stopTimeField.setText(st.format(stop));
		}

		notesArea.setText(t.getNotes());
	}

	private void init(HashSet<Project> projects)
	{
		Object[] o = projects.toArray();
		Arrays.sort(o);
		projectBox.setModel(new DefaultComboBoxModel(o));

		okButton.addActionListener(this);
		projectBox.addActionListener(this);
		actionPerformed(new ActionEvent(projectBox, 42, "Start"));
		setAlwaysOnTop(true);
	}

	/**
	 * @param projects
	 * @param labels
	 * @return Returns an Object[] {projectName, taskName, notes, long startDate, long stopDate} or null if user cancelled input.
	 */
	public static Object[] showAddDialog(HashSet<Project> projects)
	{
		dialog = new TaskAddCustom(projects);
		dialog.setVisible(true);
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource().equals(okButton))
		{
			SimpleDateFormat st = new SimpleDateFormat("HH:mm:ss");
			Date start = null, end = null;
			boolean error = false;
			try
			{
				start = st.parse(startTimeField.getText());
				end = st.parse(stopTimeField.getText());
			} catch (ParseException e1)
			{
			}
			// TODO: Give decent errormessages if no useful date has been entered.
			if (start == null || startDatePicker.getDate() == null)
			{
				startTimeLabel.setForeground(Color.red);
				startLabel.setForeground(Color.red);
				error = true;
			}
			if (end == null || stopDatePicker.getDate() == null
					|| stopDatePicker.getDate().getTime() + end.getTime() < startDatePicker.getDate().getTime() + start.getTime())
			{
				stopTimeLabel.setForeground(Color.red);
				stopLabel.setForeground(Color.red);
				error = true;
			}

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

			Date startd = start;
			startd.setDate(startDatePicker.getDate().getDate());
			startd.setMonth(startDatePicker.getDate().getMonth());
			startd.setYear(startDatePicker.getDate().getYear());

			Date stopd = end;
			stopd.setDate(stopDatePicker.getDate().getDate());
			stopd.setMonth(stopDatePicker.getDate().getMonth());
			stopd.setYear(stopDatePicker.getDate().getYear());

			//System.out.println(start.getTime() + ", " + startDatePicker.getDate().getTime());
			TaskAddCustom.value = new Object[] { projectBox.getSelectedItem(), taskBox.getSelectedItem(), notesArea.getText(), startd.getTime(),
					stopd.getTime() };
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
			{
				// New project
				taskBox.setModel(new DefaultComboBoxModel());
			}
		} else
		{
			System.out.println("Misc button");
			// User probably hit the close button
			TaskAddCustom.value = null;
			dialog.setVisible(false);
		}
	}

	/**
	 * @param projects
	 * @param p
	 * @param t
	 * @param te
	 * @return Object{Project, Task, String notes, long start, long end}
	 */
	public static Object[] showEditDialog(HashSet<Project> projects, Project p, Task t, TaskEntry te)
	{
		dialog = new TaskAddCustom(projects, p, t, te);
		dialog.setVisible(true);
		return value;
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		projectLabel = new JLabel();
		projectBox = new JComboBox();
		taskLabel = new JLabel();
		taskBox = new JComboBox();
		startLabel = new JLabel();
		startDatePicker = new JXDatePicker();
		startTimeLabel = new JLabel();
		startTimeField = new JFormattedTextField(mf);
		stopLabel = new JLabel();
		stopDatePicker = new JXDatePicker();
		stopTimeLabel = new JLabel();
		stopTimeField = new JFormattedTextField(mf);
		notesLabel = new JLabel();
		notesScrollPane = new JScrollPane();
		notesArea = new JTextArea();
		okButton = new JButton();

		// ======== this ========
		setTitle("Add Task");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 15, 80, 115, 80, 85, 95, 10, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 15, 0, 0, 0, 0, 55, 35, 0, 10, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4 };

		// ---- projectLabel ----
		projectLabel.setText("Project:");
		contentPane.add(projectLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
				0, 5, 5), 0, 0));

		// ---- projectBox ----
		projectBox.setEditable(true);
		contentPane.add(projectBox, new GridBagConstraints(2, 1, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));

		// ---- taskLabel ----
		taskLabel.setText("Task name:");
		contentPane.add(taskLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));

		// ---- taskBox ----
		taskBox.setEditable(true);
		contentPane.add(taskBox, new GridBagConstraints(2, 2, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 5,
				5), 0, 0));

		// ---- startLabel ----
		startLabel.setText("Date started:");
		contentPane.add(startLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));
		contentPane.add(startDatePicker, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 5, 5), 0, 0));

		// ---- startTimeLabel ----
		startTimeLabel.setText("Time started:");
		contentPane.add(startTimeLabel, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 5, 5), 0, 0));

		// ---- startTimeField ----
		startTimeField.setText("00:00:00");
		contentPane.add(startTimeField, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 5, 5), 0, 0));

		// ---- stopLabel ----
		stopLabel.setText("Date stopped:");
		contentPane.add(stopLabel, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));
		contentPane.add(stopDatePicker, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 5, 5), 0, 0));

		// ---- stopTimeLabel ----
		stopTimeLabel.setText("Time stopped:");
		contentPane.add(stopTimeLabel, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
				0, 5, 5), 0, 0));

		// ---- stopTimeField ----
		stopTimeField.setText("00:00:00");
		contentPane.add(stopTimeField, new GridBagConstraints(4, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
				0, 5, 5), 0, 0));

		// ---- notesLabel ----
		notesLabel.setText("Notes:");
		contentPane.add(notesLabel, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));

		// ======== notesScrollPane ========
		{
			notesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			notesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			notesScrollPane.setViewportView(notesArea);
		}
		contentPane.add(notesScrollPane, new GridBagConstraints(2, 5, 4, 2, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
				0, 0, 5, 5), 0, 0));

		// ---- okButton ----
		okButton.setText("Ok");
		contentPane.add(okButton, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				5, 5), 0, 0));
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	private JLabel projectLabel;
	private JComboBox projectBox;
	private JLabel taskLabel;
	private JComboBox taskBox;
	private JLabel startLabel;
	private JXDatePicker startDatePicker;
	private JLabel startTimeLabel;
	private JFormattedTextField startTimeField;
	private JLabel stopLabel;
	private JXDatePicker stopDatePicker;
	private JLabel stopTimeLabel;
	private JFormattedTextField stopTimeField;
	private JLabel notesLabel;
	private JScrollPane notesScrollPane;
	private JTextArea notesArea;
	private JButton okButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables

}
