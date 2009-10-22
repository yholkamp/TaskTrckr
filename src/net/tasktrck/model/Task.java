package net.tasktrck.model;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Gebruiker
 */
public class Task implements Comparable<Object>, Serializable
{
	private static final long serialVersionUID = 4834751923466144272L;
	private String label;
	private HashSet<TaskEntry> entries;
	private String notes;

	/**
	 * @param taskLabel
	 * @param taskId
	 * @param entries
	 * @param notes
	 */
	public Task(String taskLabel, HashSet<TaskEntry> entries, String notes)
	{
		this.label = taskLabel;
		this.entries = entries;
		this.notes = notes;
	}

	/**
	 * @param taskLabel
	 * @param notes
	 * @param entry
	 */
	public Task(String taskLabel, String notes)
	{
		this.label = taskLabel;
		this.notes = notes;
		this.entries = new HashSet<TaskEntry>();
	}

	public boolean addEntry(TaskEntry t)
	{
		return entries.add(t);
	}

	@Override
	public int compareTo(Object arg0)
	{
		if (arg0 instanceof Task)
		{
			return label.compareTo((((Task) arg0).getTaskLabel()));
		} else
		{
			return -1;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		return (o instanceof Task) && ((Task) o).hashCode() == this.hashCode();
	}

	public String fullToString()
	{
		String entryS = "";
		if (entries != null && entries.size() > 0)
			for (TaskEntry t : entries)
				entryS += "\n" + t.toString();
		return "[" + label + ", entries: " + ((entryS == "") ? "None" : entryS) + "]";
	}

	public HashSet<TaskEntry> getEntries()
	{
		return entries;
	}

	public int getEntryCount()
	{
		return entries.size();
	}

	public String getNotes()
	{
		return notes;
	}

	public String getTaskLabel()
	{
		return label;
	}

	@Override
	public int hashCode()
	{
		return label.hashCode();
	}

	public void setEntries(HashSet<TaskEntry> entries)
	{
		this.entries = entries;
	}

	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	public void setTaskLabel(String taskLabel)
	{
		this.label = taskLabel;
	}

	@Override
	public String toString()
	{
		return label;
	}

	public boolean removeEntry(TaskEntry taskentry)
	{
		return entries.remove(taskentry);
	}
}