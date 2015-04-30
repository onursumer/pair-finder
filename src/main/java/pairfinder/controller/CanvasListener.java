package pairfinder.controller;

import pairfinder.gui.MainFrame;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CanvasListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e)
	{
		MainFrame frame = MainFrame.getInstance();
		
		int x = e.getX();
		int y = e.getY(); 
		
		// TODO debug
		
		Point input = frame.getCanvas().revert(new Point(x, y));
		frame.getToolbar().updateCoords(input.x, input.y);
	}
	
}
