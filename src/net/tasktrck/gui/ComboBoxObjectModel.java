package net.tasktrck.gui;

// http://www.java2s.com/Code/Java/Swing-JFC/AfancyexampleofJComboBoxwithacustomrendererandeditor.htm
import java.util.Set;

import javax.swing.DefaultComboBoxModel;

public class ComboBoxObjectModel extends DefaultComboBoxModel
{
	private static final long serialVersionUID = 5572442246373287060L;
	private final Set<Object> s;

	public ComboBoxObjectModel(Set<Object> set)
	{
		s = set;
	}

	@Override
	public Object getSelectedItem()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectedItem(Object anItem)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object getElementAt(int arg0)
	{
		// TODO Auto-generated method stub
		return (this.getSize() > arg0) ? s.toArray()[arg0] : null;
	}

	@Override
	public int getSize()
	{
		return s.size();
	}
}