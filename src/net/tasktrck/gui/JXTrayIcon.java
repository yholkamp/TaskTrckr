package net.tasktrck.gui;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import net.tasktrck.core.Controller;
import net.tasktrck.core.TrayViewAL;

public class JXTrayIcon extends TrayIcon
{
	private TaskTray menu;
	private static JDialog dialog;
	static
	{
		dialog = new JDialog((Frame) null);
		dialog.setUndecorated(true);
		dialog.setAlwaysOnTop(true);
	}

	private static PopupMenuListener popupListener = new PopupMenuListener() {
		public void popupMenuWillBecomeVisible(PopupMenuEvent e)
		{
		}

		public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
		{
			dialog.setVisible(false);
		}

		public void popupMenuCanceled(PopupMenuEvent e)
		{
			dialog.setVisible(false);
		}
	};

	/**
	 * Swaps the 'Start/Stop' Task options according to the current status.
	 * 
	 * @param b
	 */
	public void setRunning(boolean b)
	{
		menu.setRunning(b);
		if (b)
			this.setImage(createImage("clockico-busy.png", "Task Running - TaskTrckr"));
		else
			this.setImage(createImage("clockico.png", "TaskTrckr"));
	}

	/**
	 * Build a new JXTrayIcon with default clockicon.
	 * 
	 * @param c
	 */
	public JXTrayIcon(final Controller c)
	{
		super(JXTrayIcon.createImage("clockico.png", "TaskTrckr"));
		setJPopupMenu(new TaskTray(new TrayViewAL(c)));
		try
		{
			SystemTray.getSystemTray().add(this);
		} catch (AWTException e)
		{
			e.printStackTrace();
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e)
			{
				showJPopupMenu(e);
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				showJPopupMenu(e);
			}
		});

		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				c.toggleTask();
			}
		});
	}

	protected void showJPopupMenu(MouseEvent e)
	{
		if (e.isPopupTrigger() && menu != null)
		{
			Dimension size = menu.getPreferredSize();
			showJPopupMenu(e.getX(), e.getY() - size.height);
		}
	}

	protected void showJPopupMenu(int x, int y)
	{
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		menu.show(dialog.getContentPane(), 0, 0);
		// popup works only for focused windows
		dialog.toFront();
	}

	public JPopupMenu getJPopupMenu()
	{
		return menu;
	}

	public void setJPopupMenu(TaskTray menu)
	{
		if (this.menu != null)
		{
			this.menu.removePopupMenuListener(popupListener);
		}
		this.menu = menu;
		menu.addPopupMenuListener(popupListener);
	}

	protected static Image createImage(String path, String description)
	{
		URL imageURL = JXTrayIcon.class.getResource(path);

		if (imageURL == null)
		{
			System.err.println("Resource not found: " + path);
			return null;
		} else
		{
			return (new ImageIcon(imageURL, description)).getImage();
		}
	}
}