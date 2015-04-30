package pairfinder.gui;

import java.awt.Point;

public class Line
{
	private Point start;
	private Point end;
	
	public Line(Point begin, Point end)
	{
		super();
		this.start = begin;
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}
}
