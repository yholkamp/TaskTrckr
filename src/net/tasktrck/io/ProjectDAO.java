package net.tasktrck.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.tasktrck.model.Project;

public class ProjectDAO
{
	@SuppressWarnings("unchecked")
	public static Set<Project> load(String filename)
	{
		Object obj = null;
		try
		{
			FileInputStream f_in = new FileInputStream(filename);

			// Read object using ObjectInputStream.
			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			// Read an object.
			obj = obj_in.readObject();
		} catch (FileNotFoundException e)
		{
			//First run
		} catch (IOException e)
		{
			//e.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), e.getMessage(), "TaskTrckr Error", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e)
		{
			//Failed to deserialize
			//e.printStackTrace();
		}
		if (obj != null && obj instanceof Set)
			return ((Set<Project>) obj);
		else
			return null;
	}

	public static boolean save(String filename, Set<Project> ps)
	{
		try
		{
			// Use a FileOutputStream to send data to a file
			// called myobject.data.
			FileOutputStream f_out = new FileOutputStream(filename);
			// Use an ObjectOutputStream to send object data to the
			// FileOutputStream for writing to disk.
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
			// Pass our object to the ObjectOutputStream's
			// writeObject() method to cause it to be written out
			// to disk.
			obj_out.writeObject(ps);
		} catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "An error occurred with the following message:\n\n" + e.getMessage(), "TaskTrckr Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
