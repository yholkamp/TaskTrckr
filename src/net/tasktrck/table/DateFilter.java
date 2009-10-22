package net.tasktrck.table;

import java.util.ArrayList;
import java.util.Calendar;

import org.jdesktop.swingx.decorator.Filter;

/**
 * This filter is used as an abstract super class for date filters. Hours, minutes, and seconds are ignored. By default this filter is inclusive,
 * however this can be set by the user.
 * <p />
 * Project Meditra_NTS_Viewer<br />
 * DateFilter.java created Nov 19, 2007
 * <p />
 * 
 * @author <a href="mailto:borsje@semlab.nl">J.A. Borsje</a>
 * @version $Revision: 1.1 $, $Date: 2007/11/20 13:39:36 $
 */
public abstract class DateFilter extends Filter
{
	private ArrayList<Integer> toPrevious;
	protected boolean inclusive;
	protected Calendar date;

	/**
	 * Create a new instance of the date filter.
	 * 
	 * @param col
	 *            The column to which apply the filter.
	 */
	public DateFilter(int col)
	{
		this(null, col);
	}

	/**
	 * Create a new instance of the date filter.
	 * 
	 * @param date
	 *            The date to be used in the filter.
	 * @param col
	 *            The column to which apply the filter.
	 */
	public DateFilter(Calendar date, int col)
	{
		super(col);
		inclusive = true;
		setDate(date);
	}

	/**
	 * Set the date used in this filter.
	 * 
	 * @param date
	 *            The date on which we are currently filtering.
	 */
	public void setDate(Calendar date)
	{
		if (date != null)
		{
			normalize(date);
		}
		this.date = date;
		refresh();
	}

	/**
	 * Check if this filter is inclusive.
	 * 
	 * @return A boolean which indicates if this filter should be inclusive.
	 */
	public boolean isInclusive()
	{
		return inclusive;
	}

	/**
	 * Set if this filter should be inclusive or not. E.g. date <= coldate (inclusive), or date < coldate (exclusive).
	 * 
	 * @param inclusive
	 *            A boolean which indicates if this filter should be inclusive.
	 */
	public void setInclusive(boolean inclusive)
	{
		this.inclusive = inclusive;
	}

	/**
	 * Get the date used in this filter.
	 * 
	 * @return The date on which we are currently filtering.
	 */
	public Calendar getDate()
	{
		return date;
	}

	/**
	 * Test if the current row should be displayed.
	 * 
	 * @param row
	 *            The index of the row to be tested.
	 * @return A boolean which indicates if we should show this row.
	 */
	protected abstract boolean test(int row);

	/**
	 * @see org.jdesktop.swingx.decorator.Filter#filter()
	 */
	@Override
	protected void filter()
	{
		int inputSize = getInputSize();
		int current = 0;
		for (int i = 0; i < inputSize; i++)
		{
			if (test(i))
			{
				toPrevious.add(new Integer(i));
				fromPrevious[i] = current++;
			}
		}
	}

	/**
	 * @see org.jdesktop.swingx.decorator.Filter#getSize()
	 */
	@Override
	public int getSize()
	{
		return toPrevious.size();
	}

	/**
	 * @see org.jdesktop.swingx.decorator.Filter#init()
	 */
	@Override
	protected void init()
	{
		toPrevious = new ArrayList<Integer>();
	}

	/**
	 * @see org.jdesktop.swingx.decorator.Filter#mapTowardModel(int)
	 */
	@Override
	protected int mapTowardModel(int row)
	{
		return (toPrevious.get(row)).intValue();
	}

	/**
	 * @see org.jdesktop.swingx.decorator.Filter#reset()
	 */
	@Override
	protected void reset()
	{
		toPrevious.clear();
		int inputSize = getInputSize();
		fromPrevious = new int[inputSize];
		for (int i = 0; i < inputSize; i++)
		{
			fromPrevious[i] = -1;
		}
	}

	/**
	 * Make sure we ignore minutes, seconds, etc. Only compare on year, month, and day.
	 * 
	 * @param cal
	 *            The calendar to normalize.
	 */
	protected void normalize(Calendar cal)
	{
		cal.clear(Calendar.AM_PM);
		cal.clear(Calendar.HOUR_OF_DAY);
		cal.clear(Calendar.HOUR);
		cal.clear(Calendar.MINUTE);
		cal.clear(Calendar.SECOND);
		cal.clear(Calendar.MILLISECOND);
	}
}