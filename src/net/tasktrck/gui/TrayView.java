package net.tasktrck.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.tasktrck.core.Controller;

@Deprecated
public class TrayView
{
	static TrayIcon trayIcon = null;
	private static Controller c;
	private static SystemTray tray;

	public TrayView(Controller c)
	{
		TrayView.c = c;
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				createTray();
			}
		});
	}

	private static void createTray()
	{
		// Check the SystemTray support
		if (!SystemTray.isSupported())
		{
			System.out.println("SystemTray is not supported");
			return;
		}

		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex)
		{
			ex.printStackTrace();
		} catch (IllegalAccessException ex)
		{
			ex.printStackTrace();
		} catch (InstantiationException ex)
		{
			ex.printStackTrace();
		} catch (ClassNotFoundException ex)
		{
			ex.printStackTrace();
		}

		final PopupMenu popup = new PopupMenu();
		final TrayIcon trayIcon = new TrayIcon(createImage("clockico.png", "tray icon"));
		tray = SystemTray.getSystemTray();

		// Create a popup menu components
		MenuItem overviewItem = new MenuItem("Show Overview");
		MenuItem startItem = new MenuItem("Start Task");
		MenuItem stopItem = new MenuItem("Stop Task");
		MenuItem exitItem = new MenuItem("Exit");

		// Add components to popup menu
		popup.add(overviewItem);
		popup.addSeparator();
		popup.add(startItem);
		popup.add(stopItem);
		popup.addSeparator();
		popup.add(exitItem);

		trayIcon.setPopupMenu(popup);

		try
		{
			tray.add(trayIcon);
		} catch (AWTException e)
		{
			System.out.println("TrayIcon could not be added.");
			return;
		}

		overviewItem.addActionListener(c);
		startItem.addActionListener(c);
		stopItem.addActionListener(c);
		exitItem.addActionListener(c);
		System.out.println("Tray added");
	}

	// Obtain the image URL
	protected static Image createImage(String path, String description)
	{
		URL imageURL = TrayView.class.getResource(path);

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
