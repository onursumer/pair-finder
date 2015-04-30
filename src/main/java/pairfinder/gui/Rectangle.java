package pairfinder.gui;

import java.awt.Point;

public class Rectangle
{
	private Point start;
	private int width;
	private int height;
	
	public Rectangle(Point start, int width, int height)
	{
		super();
		this.start = start;
		this.width = width;
		this.height = height;
	}

	public Point getStart() {
		return start;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
