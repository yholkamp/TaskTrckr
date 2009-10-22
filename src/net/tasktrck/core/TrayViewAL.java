package net.tasktrck.core;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Listens to the input given through the tray version of the app.
 * 
 * @author Leftblank
 */
public class TrayViewAL implements ActionListener
{
	private final Controller c;

	public TrayViewAL(Controller controller)
	{
		this.c = controller;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// MenuItem item = (MenuItem) e.getSource();
		// System.out.println(item.getLabel());
		if ("Open".equals(e.getActionCommand()))
		{
			c.openOverview();
		} else if ("Toggle".equals(e.getActionCommand()))
		{
			c.toggleTask();
		} else if ("Exit".equals(e.getActionCommand()))
		{
			c.exit();
		} else if ("About".equals(e.getActionCommand()))
		{
			c.about();
		}
	}
}