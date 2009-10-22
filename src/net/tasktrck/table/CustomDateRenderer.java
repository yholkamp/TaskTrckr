package net.tasktrck.table;

import java.text.SimpleDateFormat;

import javax.swing.table.DefaultTableCellRenderer;

public class CustomDateRenderer extends DefaultTableCellRenderer
{
	private static final long serialVersionUID = 6083348764476470794L;
	SimpleDateFormat formatter;

	public CustomDateRenderer()
	{
		super();
	}

	@Override
	public void setValue(Object value)
	{
		if (formatter == null)
		{
			formatter = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
		}
		setText((value == null) ? "" : formatter.format(value));
	}
}
