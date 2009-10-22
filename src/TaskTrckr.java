import net.tasktrck.core.Controller;

public class TaskTrckr
{
	public static void main(String[] args)
	{
		if (args != null && args.length > 2)
			new Controller(args[0]);
		else
			new Controller("projects.dat");
	}

}