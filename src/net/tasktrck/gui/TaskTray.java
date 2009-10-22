package net.tasktrck.gui;

import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * Construct a new trayIcon + menu.
 * 
 * @author Leftblank
 */
public class TaskTray extends JPopupMenu
{
	private static final long serialVersionUID = 4939329341005397226L;

	public TaskTray(ActionListener a)
	{
		initComponents();

		openItem.addActionListener(a);
		openItem.setActionCommand("Open");
		toggleItem.addActionListener(a);
		toggleItem.setActionCommand("Toggle");
		closeItem.addActionListener(a);
		closeItem.setActionCommand("Exit");
		aboutItem.addActionListener(a);
		aboutItem.setActionCommand("About");
	}

	public void setRunning(boolean b)
	{
		if (b)
			toggleItem.setText("Stop Task");
		else
			toggleItem.setText("Start Task");
	}

	private void initComponents()
	{
		// JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents
		openItem = new JMenuItem();
		toggleItem = new JMenuItem();
		aboutItem = new JMenuItem();
		closeItem = new JMenuItem();

		//======== this ========

		//---- openItem ----
		openItem.setText("Open TaskTrckr");
		add(openItem);
		addSeparator();

		//---- toggleItem ----
		toggleItem.setText("Start Task");
		add(toggleItem);
		addSeparator();

		//---- aboutItem ----
		aboutItem.setText("About TaskTrckr");
		add(aboutItem);
		addSeparator();

		//---- closeItem ----
		closeItem.setText("Close TaskTrckr");
		add(closeItem);
		// JFormDesigner - End of component initialization //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables
	private JMenuItem openItem;
	private JMenuItem toggleItem;
	private JMenuItem aboutItem;
	private JMenuItem closeItem;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
