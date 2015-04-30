package pairfinder.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;


public class MenuListener implements ActionListener
{
	private static MenuListener singleton = new MenuListener();
	
	private MenuListener()
	{
		super();
	}
	
	public static MenuListener getInstance()
	{
		return singleton;
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		JMenuItem menuItem = (JMenuItem)event.getSource();
		Commander commander = Commander.getInstance();
		
		commander.addCommand(menuItem.getActionCommand());	
		
	}
}
