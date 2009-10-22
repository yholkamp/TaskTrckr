package net.tasktrck.table;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

import net.tasktrck.model.TaskEntry;

public class TaskEntryRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 6083348764476470794L;

	public TaskEntryRenderer()
	{
		super();
	}

	@Override
	public void setValue(Object value)
	{
		String output = null;
		if (value != null)
		{
			DecimalFormat df = new DecimalFormat("00");
			SimpleDateFormat tf = new SimpleDateFormat("mm:ss");
			Date d = new Date(((TaskEntry) value).getDuration());
			output = df.format(d.getTime() / (60 * 60 * 1000)) + ":" + tf.format(d);
		}
		setText(output);
	}
}
