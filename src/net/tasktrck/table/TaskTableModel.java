package net.tasktrck.table;

import java.util.Date;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import net.tasktrck.model.Project;
import net.tasktrck.model.Task;
import net.tasktrck.model.TaskEntry;

/**
 * "Project", "Task", "Duration", "Start", "End"
 * 
 * @author Gebruiker
 */
public class TaskTableModel extends DefaultTableModel
{
	private final String[] columns = new String[] { "Project", "Task", "Duration", "Start", "End" };
	private final Class<?>[] columnType = new Class[] { Project.class, Task.class, TaskEntry.class, Date.class, Date.class };
	private static final long serialVersionUID = -8269821285759383173L;

	/**
	 * @param p
	 */
	public TaskTableModel(Set<Project> p)
	{
		super.setColumnIdentifiers(columns);
		addToModel(p);
	}

	@Override
	public int getColumnCount()
	{
		return columns.length;
	}

	@Override
	public String getColumnName(int column)
	{
		return columns[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Object result = null;

		switch (columnIndex) {
		/*case 2:
			result = (super.getValueAt(rowIndex, 2) == null) ? "" : tf.format(new Date(((TaskEntry) super.getValueAt(rowIndex, 2)).getDuration()));
			break;*/
		case 3:
			//result = (super.getValueAt(rowIndex, 2) == null) ? "" : df.format(((TaskEntry) super.getValueAt(rowIndex, 2)).getStartDate());
			result = (super.getValueAt(rowIndex, 2) == null) ? "FAAL" : ((TaskEntry) super.getValueAt(rowIndex, 2)).getStartDate();
			break;
		case 4:
			//result = (super.getValueAt(rowIndex, 2) == null) ? "" : df.format(((TaskEntry) super.getValueAt(rowIndex, 2)).getEndDate());
			result = (super.getValueAt(rowIndex, 2) == null) ? "FAAL" : ((TaskEntry) super.getValueAt(rowIndex, 2)).getEndDate();
			break;
		default:
			result = super.getValueAt(rowIndex, columnIndex);
		}

		return result;
	}

	/**
	 * Add the given set to the model.
	 * 
	 * @param projs
	 */
	public void addToModel(Set<Project> projs)
	{
		// Add the projects to the model.
		for (Project p : projs)
		{
			if (p.getTaskList().size() < 1)
			{
				addRow(new Object[] { p });
			} else
				for (Task t : p.getTaskList())
				{
					if (t.getEntries().size() < 1)
					{
						addRow(new Object[] { p, t });
					} else
						for (TaskEntry te : t.getEntries())
						{
							addRow(new Object[] { p, t, te });
						}
				}
		}
	}

	@Override
	public Class<?> getColumnClass(int column)
	{
		return columnType[column];
	}

	/**
	 * Return true in case the cell is editable, currently disabled for all cells.
	 */
	@Override
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
