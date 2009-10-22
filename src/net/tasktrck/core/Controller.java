package net.tasktrck.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import net.tasktrck.gui.JXTrayIcon;
import net.tasktrck.gui.TaskAdd;
import net.tasktrck.gui.TaskAddCustom;
import net.tasktrck.gui.TaskEnd;
import net.tasktrck.gui.TaskList;
import net.tasktrck.io.ProjectDAO;
import net.tasktrck.model.Project;
import net.tasktrck.model.Task;
import net.tasktrck.model.TaskEntry;

public class Controller implements ActionListener
{
	private final List<ActionListener> mActionListeners = new ArrayList<ActionListener>();
	private ListIterator<ActionListener> iterator = mActionListeners.listIterator();

	private JXTrayIcon tray = null;
	// Real data
	private HashSet<Project> projects;

	private final String filename;
	// Temporary storage
	private Task runningTask = null;
	private TaskEntry runningEntry = null;
	private Project runningProject = null;

	private TaskList currentWindow = null;

	public Controller(String file)
	{
		filename = file;
		projects = (HashSet<Project>) ProjectDAO.load("projects.dat");
		if (projects == null)
			projects = new HashSet<Project>();

		/*Set<Project> projects2 = (Set) projects.clone();
		for (Project p2 : projects2)
			if (p2.getTaskListCount() == 0)
				projects.remove(p2);*/

		// enterDemoData();
		setLookAndFeel();
		// addListener(new TaskListAL(this));
		//openOverview();
		//startTray();
		tray = new JXTrayIcon(this);
		// startTask();
		// addCustomTask();
	}

	public void actionPerformed(ActionEvent e)
	{
		iterator = mActionListeners.listIterator();
		while (iterator.hasNext())
		{
			ActionListener actionListener = iterator.next();
			actionListener.actionPerformed(e);
		}
	}

	/**
	 * @param listener
	 *            ActionListener to be added to the current list.
	 */
	public void addListener(ActionListener listener)
	{
		iterator.add(listener);
	}

	/**
	 * Ask the user to enter a task at a custom moment.
	 */
	public void addTask()
	{
		Object[] input = TaskAddCustom.showAddDialog(projects);
		if (input != null)
		{
			Project currentProject;
			if (input[0] instanceof Project)// User picked an unedited thing.
				currentProject = (Project) input[0];
			else
				currentProject = new Project((String) input[0]);

			Task currentTask;
			if (input[1] instanceof Task)
			{
				currentTask = (Task) input[1];
				currentTask.setNotes((String) input[2]);
			} else
			{
				currentTask = new Task((String) input[1], (String) input[2]);
			}
			TaskEntry currentEntry = new TaskEntry((Long) input[3], (Long) input[4]);
			currentTask.addEntry(currentEntry);
			currentProject.addTask(currentTask);
			projects.add(currentProject);

			save();
		}
	}

	public void editTask(Project p, Task t, TaskEntry te)
	{
		Object[] input = TaskAddCustom.showEditDialog(projects, p, t, te);
		if (input != null)
		{
			TaskEntry currentEntry = new TaskEntry((Long) input[3], (Long) input[4]);

			Task currentTask;
			if (input[1] instanceof Task) // Check what user picked as Task.
			{
				currentTask = (Task) input[1];
				currentTask.setNotes((String) input[2]);
			} else
			{
				currentTask = new Task((String) input[1], (String) input[2]);
			}

			Project currentProject;
			if (input[0] instanceof Project) // Check for project input
				currentProject = (Project) input[0];
			else
			{
				currentProject = new Project((String) input[0]);
			}

			//Renew the used TaskEntry
			t.removeEntry(te);
			currentTask.addEntry(currentEntry);
			if (currentTask != t && currentProject != p)
			{
				// Task and Project changed
				currentProject.addTask(currentTask);
				projects.add(currentProject);
			} else if (currentProject != p)
			{
				// Only Project changed, remove the old task from p
				p.removeTask(t);
				currentProject.addTask(currentTask);
				projects.add(currentProject);
			} else if (currentTask != t)
			{
				// Only Task changed, add task to the new project
				currentProject.addTask(currentTask);
			}

			//Check for lonely parents
			if (t.getEntryCount() == 0)
			{
				p.removeTask(t);
				if (p.getTaskCount() == 0)
					projects.remove(p);
			}

			//Cleanup
			currentTask = null;
			currentProject = null;
			currentEntry = null;
			save();
		}
	}

	/**
	 * Attempt to save and close the app.
	 */
	public void exit()
	{
		if (runningEntry != null)
			if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(new JFrame(),
					"There is still one task running, this task will not be saved.\nAre you sure you want to exit?", "Confirm Exit",
					JOptionPane.YES_NO_OPTION))
				return;
		save();
		System.exit(0);
	}

	public void openOverview()
	{
		if (currentWindow == null)
		{
			currentWindow = new TaskList(this, projects);
		} else
		{
			currentWindow.reset();
			currentWindow.setVisible(true);
		}
	}

	/**
	 * @param listener
	 *            ActionListener to be removed from the current list.
	 */
	public void removeListener(ActionListener listener)
	{
		mActionListeners.remove(listener);
	}

	/**
	 * Attempts to save the current projects to hd in the default dir.
	 */
	public void save()
	{
		ProjectDAO.save(filename, projects);
	}

	/**
	 * Ask user for input on starting a new task, if user agrees to start changes to the tray are made as well.
	 */
	private void startTask()
	{
		Object[] input = TaskAdd.showDialog(projects);
		if (input != null)
		{
			runningEntry = new TaskEntry(System.currentTimeMillis());

			if (input[0] instanceof Project)// User picked an unedited thing.
				runningProject = (Project) input[0];
			else
				runningProject = new Project((String) input[0]);

			if (input[1] instanceof Task)
			{
				runningTask = (Task) input[1];
				runningTask.setNotes((String) input[2]);
			} else
			{
				runningTask = new Task((String) input[1], (String) input[2]);
			}
			tray.setRunning(true);
			//System.out.println("Starting task");
		}
	}

	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			//UIManager.setLookAndFeel(new WindowsLookAndFeel());
		} catch (Exception e)
		{
		}
	}

	public void stopTask()
	{
		Object[] input = TaskEnd.showDialog(runningProject, runningTask, runningEntry.getStart());
		if (input != null)
		{
			if ((Boolean) input[0] == true)
			{ // User choose finish
				//System.out.println("Finished task");
				if (input[1] instanceof Task)
				{
					runningTask = (Task) input[1];
					runningTask.setNotes((String) input[2]);
				} else
				{
					//System.out.println("input");
					runningTask = new Task((String) input[1], (String) input[2]);
				}

				runningEntry.setEnd(System.currentTimeMillis());
				runningTask.addEntry(runningEntry);
				runningProject.addTask(runningTask);
				projects.add(runningProject);
				if (currentWindow != null)
					currentWindow.reset();
			}
			// Cleanup
			runningTask = null;
			runningProject = null;
			runningEntry = null;
			tray.setRunning(false);
			save();
			if (currentWindow != null)
				currentWindow.reset();
		}
	}

	/**
	 * Toggle starting or stopping a task.
	 */
	public void toggleTask()
	{
		if (runningTask == null)
		{
			startTask();
		} else
		{
			stopTask();
		}
	}

	public void about()
	{
		String about = "TaskTrckr - Developed by NextPulse.net\n" + "Version 1.0b10\n\n"
				+ "This product includes libraries developed by the Apache Software Foundation (http://poi.apache.org/)\n"
				+ "as well as libraries developed by SwingLabs (http://swinglabs.org/).\n\n"
				+ "Redistribution of TaskTrckr is forbidden and will be punished by a fee of €42 for each violation.";
		JOptionPane.showMessageDialog(new JFrame(), about, "About TaskTrckr", JOptionPane.PLAIN_MESSAGE);
	}
}