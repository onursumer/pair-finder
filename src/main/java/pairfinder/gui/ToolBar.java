package pairfinder.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import pairfinder.controller.Controller;
import pairfinder.controller.ToolListener;

public class ToolBar extends JToolBar
{
	public static final String OPEN = "Open";
	public static final String SAVE = "Save";
	public static final String CLEAR = "Clear";
	public static final String RANDOM_SET = "RS";
	public static final String RANDOM_POINT = "RP";
	public static final String FIND_CLOSEST = "FCP";
	public static final String FIND_FARTHEST = "FFP";
	public static final String BF_FIND_CLOSEST = "BFCP";
	public static final String BF_FIND_FARTHEST = "BFFP";
	public static final String ADD_POINT = "ADDP";
	public static final String PAUSE = "PAUSE";
	public static final String SET_INTERVAL = "SET_INTERVAL";
	public static final String SET_SIZE = "SET_SIZE";
	
	
	private JButton open;
	private JButton save;
	private JButton randomSet;
	private JButton randomPoint;
	private JButton clear;
	private JButton findClosest;
	private JButton findFarthest;
	private JButton BFClosest;
	private JButton BFFarthest;
	private JButton setInterval;
	private JButton setSize;
	
	private JTextField xField;
	private JTextField yField;
	private JLabel xLabel;
	private JLabel yLabel;
	private JLabel animLabel;
	private JTextField animField;
	private JLabel sizeLabel;
	private JTextField sizeField;
	private JButton add;
	private JButton pause;
	
	public ToolBar()
	{
		super();
		this.initToolkit();
		
		this.add(open);
		this.add(save);
		
		this.addSeparator();
		
		this.add(randomPoint);
		this.add(randomSet);
		this.add(clear);
		
		this.addSeparator();
		
		this.add(findClosest);
		this.add(findFarthest);
		
		this.addSeparator();
		
		this.add(BFClosest);
		this.add(BFFarthest);
		
		this.addSeparator();
		
		this.sizeLabel = new JLabel("size:");
		this.sizeField = new JTextField();
		
		this.add(sizeLabel);
		this.add(sizeField);
		this.add(setSize);
		
		this.addSeparator();
		
		this.animLabel = new JLabel("interval:");
		this.animField = new JTextField();
		
		this.add(animLabel);
		this.add(animField);
		this.add(setInterval);
		
		this.addSeparator();
		
		this.xLabel = new JLabel("x: ");
		this.xField = new JTextField();
		this.add(xLabel);
		this.add(xField);
		
		this.addSeparator();
		
		this.yLabel = new JLabel("y: ");
		this.yField = new JTextField();
		this.add(yLabel);
		this.add(yField);
		
		this.addSeparator();
		
		this.add(add);
		
		this.addSeparator();
		
		this.add(pause);
		
		this.setFloatable(false);
	}

	private void initToolkit()
	{
		ToolListener listener = ToolListener.getInstance();
		
		open = new JButton(new ImageIcon("images/open.gif"));
		open.setActionCommand(OPEN);
		open.setToolTipText("Load set");
		open.addActionListener(listener);
		
		save = new JButton(new ImageIcon("images/save.gif"));
		save.setActionCommand(SAVE);
		save.setToolTipText("Save set");
		save.addActionListener(listener);
		
		clear = new JButton("CLS");
		clear.setActionCommand(CLEAR);
		clear.setToolTipText("Clear canvas");
		clear.addActionListener(listener);
		
		randomPoint = new JButton("RP");
		randomPoint.setActionCommand(RANDOM_POINT);
		randomPoint.setToolTipText("Generate a random point");
		randomPoint.addActionListener(listener);
		
		randomSet = new JButton("RS");
		randomSet.setActionCommand(RANDOM_SET);
		randomSet.setToolTipText("Generate a random point set");
		randomSet.addActionListener(listener);
		
		findClosest = new JButton("FCP");
		findClosest.setActionCommand(FIND_CLOSEST);
		findClosest.setToolTipText("Find closest pair");
		findClosest.addActionListener(listener);
		
		findFarthest = new JButton("FFP");
		findFarthest.setActionCommand(FIND_FARTHEST);
		findFarthest.setToolTipText("Find farthest pair");
		findFarthest.addActionListener(listener);
		
		BFClosest = new JButton("BFCP");
		BFClosest.setActionCommand(BF_FIND_CLOSEST);
		BFClosest.setToolTipText("Find closest pair (BruteForce)");
		BFClosest.addActionListener(listener);
		
		BFFarthest = new JButton("BFFP");
		BFFarthest.setActionCommand(BF_FIND_FARTHEST);
		BFFarthest.setToolTipText("Find farthest pair (BruteForce)");
		BFFarthest.addActionListener(listener);
		
		add = new JButton("Add Point");
		add.setActionCommand(ADD_POINT);
		add.setToolTipText("Add point");
		add.addActionListener(listener);
		
		pause = new JButton("Pause");
		pause.setActionCommand(PAUSE);
		pause.setToolTipText("Pause animation");
		pause.addActionListener(listener);
		
		setInterval = new JButton("Set");
		setInterval.setActionCommand(SET_INTERVAL);
		setInterval.setToolTipText("Set animation interval");
		setInterval.addActionListener(listener);
		
		setSize = new JButton("Set");
		setSize.setActionCommand(SET_SIZE);
		setSize.setToolTipText("Set random set size");
		setSize.addActionListener(listener);
	}
	
	public String getXval()
	{
		return xField.getText();
	}
	
	public String getYval()
	{
		return yField.getText();
	}
	
	public int getInterval()
	{
		if(animField.getText() == null)
			return Controller.DEFAULT_INTERVAL;
		else if(!animField.getText().matches("[1-9][0-9]*"))
			return Controller.DEFAULT_INTERVAL;
		else
			return Integer.parseInt(animField.getText());
	}

	public int getRandSize()
	{
		if(sizeField.getText() == null)
			return Controller.DEFAULT_SIZE;
		else if(!sizeField.getText().matches("[1-9][0-9]*"))
			return Controller.DEFAULT_SIZE;
		else
			return Integer.parseInt(sizeField.getText());
	}

	public void switchPause(boolean paused)
	{
		if(paused)
			this.pause.setText("Resume");
		else
			this.pause.setText("Pause");
	}

	public void updateCoords(int x, int y)
	{
		xField.setText(String.valueOf(x));
		yField.setText(String.valueOf(y));
	}
}
