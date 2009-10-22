package net.tasktrck.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CustomDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 6114125999303071428L;
	private JPanel myPanel = null;
	private JButton yesButton = null;
	private JButton noButton = null;
	private boolean answer = false;

	public boolean getAnswer()
	{
		return answer;
	}

	public CustomDialog(JFrame frame, boolean modal, String myMessage)
	{
		super(frame, modal);
		myPanel = new JPanel();
		getContentPane().add(myPanel);
		myPanel.add(new JLabel(myMessage));
		yesButton = new JButton("Yes");
		yesButton.addActionListener(this);
		myPanel.add(yesButton);
		noButton = new JButton("No");
		noButton.addActionListener(this);
		myPanel.add(noButton);
		pack();
		setLocationRelativeTo(frame);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (yesButton == e.getSource())
		{
			System.err.println("User chose yes.");
			answer = true;
			setVisible(false);
		} else if (noButton == e.getSource())
		{
			System.err.println("User chose no.");
			answer = false;
			setVisible(false);
		}
	}

}