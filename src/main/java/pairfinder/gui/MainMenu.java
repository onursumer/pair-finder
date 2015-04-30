package pairfinder.gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import pairfinder.controller.MenuListener;

public class MainMenu extends JMenuBar
{
	public static final String FILE = "File";
	public static final String OPEN = "Open";
	public static final String SAVE = "Save";
	public static final String EXIT = "Exit";
	public static final String HELP = "Help";
	public static final String HOWTO = "How to use..";
	public static final String ABOUT = "About";
	
	
	private JMenu file;
	private JMenu help;
	
	private JMenuItem open;
	private JMenuItem save;
	private JMenuItem exit;
	private JMenuItem howTo;
	private JMenuItem about;
	
	public MainMenu()
	{
		super();
		
		// init menu and menu items
		file = new JMenu(FILE);
		help = new JMenu(HELP);
		open = new JMenuItem(OPEN);
		save = new JMenuItem(SAVE);
		exit = new JMenuItem(EXIT);
		howTo = new JMenuItem(HOWTO);
		about = new JMenuItem(ABOUT);
		
		// add items to the menus
		file.add(open);
		file.add(save);
		file.add(exit);
		help.add(howTo);
		help.add(about);
		
		// add menus to the bar
		this.add(file);
		this.add(help);
		
		// add listener
		MenuListener listener = MenuListener.getInstance();
		open.addActionListener(listener);
		save.addActionListener(listener);
		exit.addActionListener(listener);
		howTo.addActionListener(listener);
		about.addActionListener(listener);
	}
	
	
}
