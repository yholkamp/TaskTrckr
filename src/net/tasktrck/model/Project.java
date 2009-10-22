package net.tasktrck.model;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Gebruiker
 */
public class Project implements Comparable<Object>, Serializable
{
	private static final long serialVersionUID = 6910426712626562689L;
	private String projectName;
	private String clientName;
	private HashSet<Task> taskList;

	/**
	 * @param projectN
	 */
	public Project(String projectN)
	{
		// labelList = new HashSet<TaskLabel>();
		taskList = new HashSet<Task>();
		projectName = projectN;
	}

	/**
	 * @param clientName
	 * @param jobName
	 */
	public Project(String jobName, String clientName)
	{
		this.clientName = clientName;
		this.projectName = jobName;
		this.taskList = new HashSet<Task>();
	}

	/**
	 * @param t
	 */
	public boolean addTask(Task t)
	{
		return taskList.add(t);
	}

	public int compareTo(Object arg0)
	{
		if (arg0 instanceof Project)
		{
			return projectName.compareTo(((Project) arg0).getProjectName());
		} else
		{
			return -1;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		return (o instanceof Project) && ((Project) o).hashCode() == this.hashCode();
	}

	/**
	 * @return Full string representation.
	 */
	public String fullToString()
	{
		String taskString = "";
		if (taskList != null && taskList.size() > 0)
			for (Task t : taskList)
			{
				taskString += "\n" + t.fullToString();
			}
		return "[{Project: " + projectName + " " + clientName + " | Tasks:" + taskString + "}]\n";
	}

	public String getClientName()
	{
		return clientName;
	}

	public String getProjectName()
	{
		return projectName;
	}

	public HashSet<Task> getTaskList()
	{
		return taskList;
	}

	public int getTaskCount()
	{
		return taskList.size();
	}

	@Override
	public int hashCode()
	{
		return projectName.hashCode();
	}

	public boolean removeTask(Task t)
	{
		return taskList.remove(t);
	}

	public void setClientName(String clientName)
	{
		this.clientName = clientName;
	}

	public void setProjectName(String jobName)
	{
		this.projectName = jobName;
	}

	/**
	 * @param taskList
	 *            HashSet<Task>
	 */
	public void setTaskList(HashSet<Task> taskList)
	{
		this.taskList = taskList;
	}

	@Override
	public String toString()
	{
		return projectName;
	}
}
