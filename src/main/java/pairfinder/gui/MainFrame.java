package pairfinder.gui;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pairfinder.controller.CanvasListener;

public class MainFrame extends JFrame
{
	public static final String TITLE = "CS 564 - Project";
	
	private static MainFrame singleton = new MainFrame();
	
	private JPanel mainPanel;
	private InfoPanel infoPanel;
	private MainMenu menuBar;
	private JScrollPane scrollPane;
	private PointCanvas canvas;
	private ToolBar toolbar; 
	private JFileChooser fc;
	
	private MainFrame()
	{
		super();
		this.init();
		this.setTitle(TITLE);
		this.setJMenuBar(this.menuBar);
		this.setContentPane(mainPanel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(908, 742);
	}
	
	public static MainFrame getInstance()
	{
		return singleton;
	}
	
	private void init()
	{
		// init main panel
		mainPanel = new JPanel();
		
		// init input panel
		infoPanel = new InfoPanel();
		
		// init menu
		menuBar = new MainMenu();
		fc = new JFileChooser("Load");
		
		// init canvas
		canvas = new PointCanvas();
		canvas.addMouseListener(new CanvasListener());
		
		// init tool bar
		toolbar = new ToolBar();
		
		// init scroll pane
		scrollPane = new JScrollPane(canvas);
		
		// layout
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(toolbar, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
	}
	
	public void showErrorMsg(String errorMsg)
	{
		JOptionPane.showMessageDialog(this,
				errorMsg,
				"Error",
				JOptionPane.ERROR_MESSAGE);
	}
	
	public int showOpenDialog()
	{
		return fc.showOpenDialog(this);
	}
	
	public int showSaveDialog()
	{
		return fc.showSaveDialog(this);
	}
	
	public String getSelectedFile()
	{
		return fc.getSelectedFile().getAbsolutePath();
	}
	
	public PointCanvas getCanvas()
	{
		return canvas;
	}
	
	public ToolBar getToolbar()
	{
		return toolbar;
	}
	
	public void setInfo(String info)
	{
		infoPanel.setInfo(info);
	}

	public void appendInfo(String info)
	{
		infoPanel.appendInfo(info);
	}
}
