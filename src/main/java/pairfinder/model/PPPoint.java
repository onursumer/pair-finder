package pairfinder.model;

import java.awt.Point;

public class PPPoint 
{
	private int index;
	private Point point;
	
	public PPPoint() {
		super();
		this.index = 0;
		this.point = new Point();
	}
	
	public PPPoint(int x, int y, int _index) {
		super();
		this.index = _index;
		this.point = new Point();
		this.point.x = x;
		this.point.y = y;
	}
	
	public PPPoint(PPPoint ppp) {
		super();
		this.index = ppp.getIndex();
		this.point = new Point(ppp.getPoint());
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	public int getIndex() {
		return index;
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	public Point getPoint() {
		return point;
	}
	
	
	
}
