/*
 * Created on Sat Apr 25 01:24:27 CEST 2009
 */

package net.tasktrck.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import net.tasktrck.core.Controller;
import net.tasktrck.io.Excel;
import net.tasktrck.model.Project;
import net.tasktrck.model.Task;
import net.tasktrck.model.TaskEntry;
import net.tasktrck.table.AfterDateFilter;
import net.tasktrck.table.BeforeDateFilter;
import net.tasktrck.table.CustomDateRenderer;
import net.tasktrck.table.TaskEntryRenderer;
import net.tasktrck.table.TaskTableModel;

import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.Filter;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternFilter;

import com.jgoodies.forms.factories.Borders;

/**
 * @author LB
 */
public class TaskList extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -907515873626609491L;
	private TaskTableModel model;
	private Set<Project> projects = null;
	private Controller c = null;

	public TaskList(Controller c, Set<Project> p)
	{
		this.c = c;
		projects = p;
		model = new TaskTableModel(p);
		initComponents();
		init();
	}

	public void actionPerformed(ActionEvent e)
	{
		if ("Add".equals(e.getActionCommand()))
		{
			c.addTask();
			reset();
		} else if ("Edit".equals(e.getActionCommand()))
		{
			int sel = taskTable.getSelectedRow();
			//System.out.println(model.getValueAt(sel, 0) + ", " + model.getValueAt(sel, 1) + ", " + model.getValueAt(sel, 2));

			if (sel != -1)
				c.editTask((Project) model.getValueAt(sel, 0), (Task) model.getValueAt(sel, 1), (TaskEntry) model.getValueAt(sel, 2));

			reset();
		} else if ("Remove".equals(e.getActionCommand()))
		{
			int[] sel = taskTable.getSelectedRows();
			if (sel != null && sel.length > 0)
			{
				for (int s : sel)
				{
					Project p = (Project) model.getValueAt(s, 0);
					Task t = (Task) model.getValueAt(s, 1);
					TaskEntry te = (TaskEntry) model.getValueAt(s, 2);

					t.removeEntry(te);
					if (t.getEntryCount() == 0)
						p.removeTask(t);
					if (p.getTaskCount() == 0)
						projects.remove(p);
				}

				// Rebuild our model and save.
				reset();
				c.save();
			}
		} else if ("Reset".equals(e.getActionCommand()))
			resetFilters();
		else if ("Export".equals(e.getActionCommand()))
		{
			final JFileChooser fc = new JFileChooser();
			//fc.setDialogTitle("Export");
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = fc.getSelectedFile();
				Excel.export(file, taskTable.getSelectedRows(), taskTable);
			}
		} else if (e.getSource().equals(projectBox))
			setFilters();
		else if (e.getSource().equals(taskBox))
			setFilters();
		else if (e.getSource().equals(endDatePicker))
			setFilters();
		else if (e.getSource().equals(startDatePicker))
			setFilters();
	}

	private void setFilters()
	{
		PatternFilter taskFilter;
		if (taskBox.getSelectedItem() == null || taskBox.getSelectedItem().toString() == "")
			taskFilter = new PatternFilter();
		else
			taskFilter = new PatternFilter(taskBox.getSelectedItem().toString() + ".*", 0, 1);

		PatternFilter projectFilter;
		if (projectBox.getSelectedItem() == null || projectBox.getSelectedItem().toString() == "")
			projectFilter = new PatternFilter();
		else
			projectFilter = new PatternFilter(projectBox.getSelectedItem().toString() + ".*", 0, 0);

		Filter start;
		if (startDatePicker.getDate() == null)
			start = new PatternFilter();
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDatePicker.getDate());
			start = new AfterDateFilter(cal, 3);
		}

		Filter end;
		if (endDatePicker.getDate() == null)
			end = new PatternFilter();
		else
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDatePicker.getDate());
			end = new BeforeDateFilter(cal, 4);
		}

		taskTable.setFilters(new FilterPipeline(new Filter[] { taskFilter, projectFilter, start, end }));
	}

	/**
	 * Reset the filters.
	 */
	private void resetFilters()
	{
		taskTable.setFilters(new FilterPipeline());

		projectBox.setSelectedIndex(-1);
		taskBox.setSelectedIndex(-1);
		startDatePicker.setDate(null);
		endDatePicker.setDate(null);
	}

	/**
	 * Reset the models for JTable and comboboxes to check for changes.
	 */
	public void reset()
	{
		//taskTable.setFilters(new FilterPipeline());
		model = new TaskTableModel(projects);
		taskTable.setModel(model);
		resetComboxes();
	}

	/**
	 * Sets the models of the comboboxes according to the current projects and restore the previous selection.
	 */
	private void resetComboxes()
	{
		int prevPsel = projectBox.getSelectedIndex();
		int prevTsel = taskBox.getSelectedIndex();

		//Set the projectBox with all arrays
		Object[] o = projects.toArray();
		Arrays.sort(o);
		projectBox.setModel(new DefaultComboBoxModel(o));
		projectBox.setSelectedIndex(prevPsel);

		//Set the taskbox with all possible tasks.
		HashSet<Task> tasks = new HashSet<Task>();
		for (Object ob : o)
		{
			tasks.addAll(((Project) ob).getTaskList());
		}
		Object[] taskO = tasks.toArray();
		Arrays.sort(taskO);
		taskBox.setModel(new DefaultComboBoxModel(taskO));
		taskBox.setSelectedIndex(prevTsel);
	}

	private void init()
	{
		taskTable.setModel(model);
		taskTable.setDefaultRenderer(Date.class, new CustomDateRenderer());
		taskTable.setDefaultRenderer(TaskEntry.class, new TaskEntryRenderer());
		addButton.addActionListener(this);
		addButton.setActionCommand("Add");
		editButton.addActionListener(this);
		editButton.setActionCommand("Edit");
		resetButton.addActionListener(this);
		resetButton.setActionCommand("Reset");
		removeButton.addActionListener(this);
		removeButton.setActionCommand("Remove");
		exportButton.addActionListener(this);
		exportButton.setActionCommand("Export");
		projectBox.addActionListener(this);
		taskBox.addActionListener(this);
		startDatePicker.addActionListener(this);
		endDatePicker.addActionListener(this);

		resetComboxes();
		projectBox.setSelectedIndex(-1);
		taskBox.setSelectedIndex(-1);

		taskTable.setHighlighters(HighlighterFactory.createSimpleStriping());
		setVisible(true);
	}

	// Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	private JPanel filterPanel;
	private JLabel projectFilterLabel;
	private JComboBox projectBox;
	private JLabel taskFilterLabel;
	private JComboBox taskBox;
	private JLabel fromDateLabel;
	private JXDatePicker startDatePicker;
	private JLabel toDateLabel;
	private JXDatePicker endDatePicker;
	private JButton resetButton;
	private JPanel tablePanel;
	private JScrollPane scrollPane2;
	private JXTable taskTable;
	private JButton exportButton;
	private JButton addButton;
	private JButton editButton;
	private JButton removeButton;

	// End of variables declaration //GEN-END:variables

	private void initComponents()
	{
		// Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		filterPanel = new JPanel();
		projectFilterLabel = new JLabel();
		projectBox = new JComboBox();
		taskFilterLabel = new JLabel();
		taskBox = new JComboBox();
		fromDateLabel = new JLabel();
		startDatePicker = new JXDatePicker();
		toDateLabel = new JLabel();
		endDatePicker = new JXDatePicker();
		resetButton = new JButton();
		tablePanel = new JPanel();
		scrollPane2 = new JScrollPane();
		taskTable = new JXTable();
		exportButton = new JButton();
		addButton = new JButton();
		editButton = new JButton();
		removeButton = new JButton();

		//======== this ========
		setMinimumSize(new Dimension(760, 350));
		setTitle("Task Overview");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridBagLayout());
		((GridBagLayout) contentPane.getLayout()).columnWidths = new int[] { 80, 50, 0 };
		((GridBagLayout) contentPane.getLayout()).rowHeights = new int[] { 0, 0 };
		((GridBagLayout) contentPane.getLayout()).columnWeights = new double[] { 0.0, 1.0, 1.0E-4 };
		((GridBagLayout) contentPane.getLayout()).rowWeights = new double[] { 1.0, 1.0E-4 };

		//======== filterPanel ========
		{
			filterPanel.setBorder(new CompoundBorder(new TitledBorder("Filters"), Borders.DLU2_BORDER));
			filterPanel.setLayout(new GridBagLayout());
			((GridBagLayout) filterPanel.getLayout()).columnWidths = new int[] { 150, 0 };
			((GridBagLayout) filterPanel.getLayout()).rowHeights = new int[] { 0, 0, 0, 0, 25, 0, 0, 0, 0, 0, 0, 0, 0 };
			((GridBagLayout) filterPanel.getLayout()).columnWeights = new double[] { 1.0, 1.0E-4 };
			((GridBagLayout) filterPanel.getLayout()).rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4 };

			//---- projectFilterLabel ----
			projectFilterLabel.setText("Project:");
			filterPanel.add(projectFilterLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

			//---- projectBox ----
			projectBox.setEditable(true);
			filterPanel.add(projectBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
					0, 0, 5, 0), 0, 0));

			//---- taskFilterLabel ----
			taskFilterLabel.setText("Task Filter:");
			filterPanel.add(taskFilterLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

			//---- taskBox ----
			taskBox.setEditable(true);
			filterPanel.add(taskBox, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
					0, 5, 0), 0, 0));

			//---- fromDateLabel ----
			fromDateLabel.setText("From date:");
			filterPanel.add(fromDateLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));
			filterPanel.add(startDatePicker, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

			//---- toDateLabel ----
			toDateLabel.setText("To date:");
			filterPanel.add(toDateLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
					0, 0, 5, 0), 0, 0));
			filterPanel.add(endDatePicker, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 5, 0), 0, 0));

			//---- resetButton ----
			resetButton.setText("Reset Filters");
			filterPanel.add(resetButton, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
					new Insets(0, 0, 0, 0), 0, 0));
		}
		contentPane.add(filterPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
				0, 0, 5), 0, 0));

		//======== tablePanel ========
		{
			tablePanel.setBorder(new TitledBorder("Tasks"));
			tablePanel.setLayout(new GridBagLayout());
			((GridBagLayout) tablePanel.getLayout()).columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
			((GridBagLayout) tablePanel.getLayout()).rowHeights = new int[] { 0, 0, 0 };
			((GridBagLayout) tablePanel.getLayout()).columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4 };
			((GridBagLayout) tablePanel.getLayout()).rowWeights = new double[] { 1.0, 0.0, 1.0E-4 };

			//======== scrollPane2 ========
			{

				//---- taskTable ----
				taskTable.setAutoCreateRowSorter(true);
				taskTable.setFilters(null);
				scrollPane2.setViewportView(taskTable);
			}
			tablePanel.add(scrollPane2, new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
					0, 0, 5, 0), 0, 0));

			//---- exportButton ----
			exportButton.setText("Export Selection");
			tablePanel.add(exportButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
					0, 0, 0, 5), 0, 0));

			//---- addButton ----
			addButton.setText("Add Task");
			tablePanel.add(addButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
					0, 0, 5), 0, 0));

			//---- editButton ----
			editButton.setText("Edit Task");
			tablePanel.add(editButton, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
					0, 0, 5), 0, 0));

			//---- removeButton ----
			removeButton.setText("Remove Task");
			tablePanel.add(removeButton, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(
					0, 0, 0, 0), 0, 0));
		}
		contentPane.add(tablePanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0,
				0, 0), 0, 0));
		pack();
		setLocationRelativeTo(null);
		// End of component initialization //GEN-END:initComponents
	}

}
