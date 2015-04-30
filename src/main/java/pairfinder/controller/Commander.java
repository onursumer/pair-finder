package pairfinder.controller;

import pairfinder.gui.MainFrame;
import pairfinder.gui.MainMenu;
import pairfinder.gui.ToolBar;

import java.util.LinkedList;

import javax.swing.JFileChooser;

public class Commander
{
	private static Commander singleton = new Commander();
	
	private LinkedList<String> commandList;
	
	public static Commander getInstance()
	{
		return singleton;
	}
	
	public Commander()
	{
		commandList = new LinkedList<String>();
		new ConsumerThread().start();
	}
	
	private void processCommand(String command)
	{
		Controller controller = Controller.getInstance();
		MainFrame frame = MainFrame.getInstance();
		
		if (command == ToolBar.OPEN || command == MainMenu.OPEN)
		{
			int result = frame.showOpenDialog();
			
			if (result == JFileChooser.APPROVE_OPTION)
			{
				controller.load(frame.getSelectedFile());
				controller.updatePoints();
			}
		}
		else if (command == ToolBar.SAVE || command == MainMenu.SAVE)
		{
			int result = frame.showSaveDialog();
			
			if (result == JFileChooser.APPROVE_OPTION)
			{
				controller.save(frame.getSelectedFile());
			}
		}
		else if (command == ToolBar.RANDOM_SET)
		{
			controller.clearResults(); // clear previous results
			controller.randomPPPList();
			controller.updatePoints();
			frame.setInfo("Random point set is succesfully generated.");
		}
		else if (command == ToolBar.RANDOM_POINT)
		{
			controller.clearResults(); // clear previous results
			controller.randomPoint();
			controller.updatePoints();
		}
		else if (command == ToolBar.CLEAR)
		{
			controller.clearPointSet();
			controller.clearCanvas();
		}
		else if (command == ToolBar.FIND_CLOSEST)
		{
			controller.clearResults(); // clear previous results
			controller.fcp();
		}
		else if (command == ToolBar.FIND_FARTHEST)
		{
			controller.clearResults(); // clear previous results
			controller.ffp();
		}
		else if (command == ToolBar.BF_FIND_CLOSEST)
		{
			controller.bfcp();
		}
		else if (command == ToolBar.BF_FIND_FARTHEST)
		{
			controller.bffp();
		}
		else if (command == ToolBar.ADD_POINT)
		{
			controller.clearResults(); // clear previous results
			controller.addPoint();
			controller.updatePoints();
		}
		else if (command == ToolBar.SET_SIZE)
		{
			int size = frame.getToolbar().getRandSize();
			controller.setRandomSize(size);
			frame.setInfo("Random set size updated to " + size + ".");
		}
		else if (command == MainMenu.EXIT)
		{
			System.exit(0);
		}
		else if (command == MainMenu.HOWTO)
		{
			
		}
		else if (command == MainMenu.ABOUT)
		{
			
		}
	}
	
	public void addCommand(String command)
	{
		synchronized (commandList)
		{
			commandList.add(command);
			commandList.notifyAll();
		}
	}
	
	/**
	 * Inner ComsumerThread which forwards commands added to the command
	 * list.
	 * 
	 * @author Selcuk Onur Sumer
	 *
	 */
	private class ConsumerThread extends Thread
	{	
		public void run()
		{
			String command;
			
			while(true)
			{
				synchronized (commandList)
				{
					
					while (commandList.size() == 0)
					{
						try {
				              commandList.wait();
						} catch (InterruptedException ex) {
				              
						}
					}

					command = commandList.remove();
					commandList.notifyAll();
				}
				
				processCommand(command);
			}
		}
	}
}
