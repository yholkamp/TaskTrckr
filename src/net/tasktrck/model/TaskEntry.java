package net.tasktrck.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskEntry implements Serializable
{
	private static final long serialVersionUID = -2925327484660150107L;
	private long start;
	private long end;

	/**
	 * @param start
	 */
	public TaskEntry(long start)
	{
		this.start = start;
	}

	/**
	 * @param start
	 * @param end
	 */
	public TaskEntry(long start, long end)
	{
		this.start = start;
		this.end = end;
	}

	@Override
	public boolean equals(Object o)
	{
		return (o instanceof TaskEntry) && ((TaskEntry) o).hashCode() == this.hashCode();
	}

	/**
	 * @return Returns a string representation of the item for debugging.
	 */
	public String fullToString()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM HH:mm:ss");
		return "Entry: " + sdf.format(start) + " => " + sdf.format(end);
	}

	public long getEnd()
	{
		return end;
	}

	public long getDuration()
	{
		return end - start;
	}

	public Date getEndDate()
	{
		return new Date(end);
	}

	public long getStart()
	{
		return start;
	}

	public Date getStartDate()
	{
		return new Date(start);
	}

	@Override
	public int hashCode()
	{
		return new Long(start).hashCode();
	}

	public void setEnd(long end)
	{
		this.end = end;
	}

	public void setStart(long start)
	{
		this.start = start;
	}

	@Override
	public String toString()
	{
		return String.valueOf(end - start);
	}
}
