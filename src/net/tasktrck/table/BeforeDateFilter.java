package net.tasktrck.table;

import java.util.Calendar;
import java.util.Date;

/**
 * This filter is used to show everything before a certain date. Hours, minutes, and seconds are ignored.
 * <p />
 * Project Meditra_NTS_Viewer<br />
 * BeforeDateFilter.java created Nov 19, 2007
 * <p />
 * 
 * @author <a href="mailto:borsje@semlab.nl">J.A. Borsje</a>
 * @version $Revision: 1.1 $, $Date: 2007/11/20 13:39:36 $
 */
public class BeforeDateFilter extends DateFilter
{
	/**
	 * Create a new BeforeDateFilter for the given column.
	 * 
	 * @param col
	 *            The column to which apply the filter.
	 */
	public BeforeDateFilter(int col)
	{
		this(null, col);
	}

	/**
	 * Create a new BeforeDateFilter for the given column, and the given date.
	 * 
	 * @param date
	 *            The date to be used in the filter.
	 * @param col
	 *            The column to which apply the filter.
	 */
	public BeforeDateFilter(Calendar date, int col)
	{
		super(date, col);
	}

	/**
	 * Test if the current row should be displayed.
	 * 
	 * @param row
	 *            The index of the row to be tested.
	 * @return A boolean which indicates if we should show this row.
	 * @see nl.semlab.meditra.nts.viewer.calltable.swing.datefilter.DateFilter#test(int)
	 */
	@Override
	protected boolean test(int row)
	{
		// If there is no date this row is shown.
		if (date == null)
		{
			return true;
		}

		// If the column is not testable the row is shown.
		if (!adapter.isTestable(getColumnIndex()))
		{
			return true;
		}

		// Get the value of the column.
		Object value = getInputValue(row, getColumnIndex());

		// If the value of the column is null the row is not shown.
		if (value == null)
		{
			return false;
		}

		// If the value of the column is not a date, it is not shown.
		if (!(value instanceof Date))
		{
			return false;
		}

		Calendar testCal = Calendar.getInstance();
		testCal.setTime((Date) value);
		normalize(testCal);

		return testCal.before(date) || (inclusive ? date.compareTo(testCal) == 0 : false);
	}
}