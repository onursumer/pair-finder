package pairfinder.controller;

import pairfinder.gui.MainFrame;
import pairfinder.gui.ToolBar;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ToolListener implements ActionListener
{
	private static ToolListener singleton = new ToolListener();
	
	private ToolListener()
	{
		super();
	}
	
	public static ToolListener getInstance()
	{
		return singleton;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		JButton button = (JButton)event.getSource();
		Commander commander = Commander.getInstance();
		Controller controller = Controller.getInstance();
		MainFrame frame = MainFrame.getInstance();
		
		if (button.getActionCommand() == ToolBar.PAUSE)
		{
			synchronized (controller.mutex)
			{
				if(controller.isPaused())
				{
					controller.setPaused(false);
					frame.getToolbar().switchPause(false);
				}
				else
				{
					controller.setPaused(true);
					frame.getToolbar().switchPause(true);
				}
				
				controller.mutex.notifyAll();
			}
			
		}
		else if (button.getActionCommand() == ToolBar.SET_INTERVAL)
		{
			int interval = frame.getToolbar().getInterval();
			controller.setAnimInterval(interval);
			frame.setInfo("Animation interval updated to " + interval + " ms.");
		
		}
		else
			commander.addCommand(button.getActionCommand());	
	}
}
