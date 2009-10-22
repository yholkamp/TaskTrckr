package net.tasktrck.core;

@Deprecated
public class TaskLabel
{
	private String label;
	private int taskLabelId = 0;

	public TaskLabel(String label)
	{
		super();
		this.label = label;
	}

	public TaskLabel(String label, int taskLabelId)
	{
		this.label = label;
		this.taskLabelId = taskLabelId;
	}

	@Override
	public boolean equals(Object o)
	{
		return (o instanceof TaskLabel) && ((TaskLabel) o).hashCode() == this.hashCode();
	}

	public String getLabel()
	{
		return label;
	}

	public int getTaskLabelId()
	{
		return taskLabelId;
	}

	@Override
	public int hashCode()
	{
		return label.hashCode();
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public void setTaskLabelId(int taskLabelId)
	{
		this.taskLabelId = taskLabelId;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
