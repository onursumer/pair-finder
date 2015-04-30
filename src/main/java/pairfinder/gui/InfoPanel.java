package pairfinder.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InfoPanel extends JPanel
{
	private JLabel label;
	
	public InfoPanel()
	{
		super();
		
		label = new JLabel("Welcome to Point Pair Finder!");
		
		this.setLayout(new BorderLayout());
		this.add(label, BorderLayout.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public void setInfo(String text)
	{
		label.setText(text);
	}

	public void appendInfo(String info)
	{
		label.setText(label.getText() + " " + info);
		
	}
}
