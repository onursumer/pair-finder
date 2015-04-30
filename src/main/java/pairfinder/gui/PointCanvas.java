package pairfinder.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

public class PointCanvas extends JPanel
{
	private static final int prefWidth = 896;
	private static final int prefHeight = 640;
	private static final int POINT_SIZE = 3;
	private static final int HIGHLIGHT_SIZE = 9;
	
	private List<Point> pointList;		// set of points to be drawn
	private List<Line> pairLineList;	// set of pairs (closest of farthest)
	private List<Rectangle> rectList;	// set of rectangles
	private List<Line> convexHull;		// lines of the convex hull
	private List<Line> antipodalList;	// antipodal pairs (as lines)
	private List<Line> divisionList;	// vertical divisions for sets
	private List<Line> dashedList;		// vertical dashed divisions for sets
	private List<Point> highlightList;	// highlighted point list
	
	// location of the origin as the panel coordinates.
	private int originX;	
	private int originY;
	
	public PointCanvas()
	{
		this.pointList = new ArrayList<Point>();
		this.pairLineList = new ArrayList<Line>();
		this.convexHull = new ArrayList<Line>();
		this.rectList = new ArrayList<Rectangle>();
		this.divisionList = new ArrayList<Line>();
		this.dashedList = new ArrayList<Line>();
		this.highlightList = new ArrayList<Point>();
		this.antipodalList = new ArrayList<Line>();
		this.setBackground(Color.white);
		this.setPreferredSize(new Dimension(prefWidth, prefHeight));
		this.originX = (int)(this.getPreferredSize().getWidth() / 2);
		this.originY = (int)(this.getPreferredSize().getHeight() / 2);
		this.repaint();
	}
	
	/**
	 * Paints component by drawing all the contents to the canvas.
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Point p;
		Line l;
		Rectangle r;
		Iterator<Point> pIter;
		Iterator<Line> lIter;
		Iterator<Rectangle> rIter;
		
		g.setColor(Color.green);
		
		// paint x-axis		
		g.drawLine(this.originX, 0,
				this.originX, this.getPreferredSize().height);
		
		// paint y-axis		
		g.drawLine(0, this.originY,
				this.getPreferredSize().width, this.originY);
		
		g.setColor(Color.yellow);
		
		// paint highlights
		
		pIter = this.highlightList.iterator();
		
		while(pIter.hasNext())
		{
			p = pIter.next();
			g.fillOval(p.x - (HIGHLIGHT_SIZE/2), p.y - (HIGHLIGHT_SIZE/2),
				HIGHLIGHT_SIZE, HIGHLIGHT_SIZE);
		}
		
		g.setColor(Color.black);
		
		// paint points
		
		pIter = this.pointList.iterator();
		
		while(pIter.hasNext())
		{
			p = pIter.next();
			g.fillOval(p.x - (POINT_SIZE/2), p.y - (POINT_SIZE/2),
					POINT_SIZE, POINT_SIZE);
		}
		
		g.setColor(Color.blue);
		
		// paint convex hull lines
		
		lIter = this.convexHull.iterator();
		
		while(lIter.hasNext())
		{
			l = lIter.next();
			g.drawLine(l.getStart().x, l.getStart().y,
					l.getEnd().x, l.getEnd().y);
		}
		
		g.setColor(Color.orange);
		
		// paint antipodal pair lines
		
		lIter = this.antipodalList.iterator();
		
		while(lIter.hasNext())
		{
			l = lIter.next();
			this.drawDashedLine(g, l);
		}
		
		g.setColor(Color.red);
		
		// paint pair lines
		
		lIter = this.pairLineList.iterator();
		
		while(lIter.hasNext())
		{
			l = lIter.next();
			g.drawLine(l.getStart().x, l.getStart().y,
					l.getEnd().x, l.getEnd().y);
		}
		
		g.setColor(Color.blue);
		
		// paint divisions
		
		lIter = this.divisionList.iterator();
		
		while(lIter.hasNext())
		{
			l = lIter.next();
			g.drawLine(l.getStart().x, l.getStart().y,
					l.getEnd().x, l.getEnd().y);
		}
		
		g.setColor(Color.magenta);
		
		// paint dashed divisions
		
		lIter = this.dashedList.iterator();
		
		while(lIter.hasNext())
		{
			l = lIter.next();
			this.drawDashedLine(g, l);
		}
		
		g.setColor(Color.orange);
		
		// paint rectangles
		
		rIter = this.rectList.iterator();
		
		while(rIter.hasNext())
		{
			r = rIter.next();
			g.drawRect(r.getStart().x, r.getStart().y,
					r.getWidth(), r.getHeight());
		}
	}

	private void drawDashedLine(Graphics g, Line line)
	{	
		float dash[] = {10.0f};
	    
		BasicStroke dashed = new BasicStroke(1.0f,
			BasicStroke.CAP_BUTT,
	    	BasicStroke.JOIN_MITER,
	    	10.0f, dash, 0.0f);

		Graphics2D g2 = (Graphics2D)g;  
		
		Stroke oldStroke = g2.getStroke();
		
		g2.setStroke(dashed);
		g2.drawLine(line.getStart().x, line.getStart().y,
			line.getEnd().x, line.getEnd().y);
		
		g2.setStroke(oldStroke);
	}

	/**
	 * Transforms the given point's coordinates from actual coordinates
	 * to panel coordinates.
	 * 
	 * @param point		point to be transformed
	 * @return			transformed point
	 */
	private Point transform(Point point)
	{
		Point p = new Point();
		
		p.x = point.x + this.originX;
		p.y = this.originY - point.y;
		
		return p;
	}
	
	/**
	 * Transforms the given line's coordinates from actual coordinates
	 * to panel coordinates.
	 *
	 * @return			transformed point
	 */
	private Line transform(Line line)
	{
		Point start = transform(line.getStart());
		Point end = transform(line.getEnd());
		
		return new Line(start, end);
	}
	
	/**
	 * Reverts the coordinates of the given point to actual coordinates.
	 * Point's coordinates are assumed to be panel coordinates.
	 * 
	 * @param point		point to be reverted
	 * @return			reverted point
	 */
	public Point revert(Point point)
	{
		Point p = new Point();
		
		p.x = point.x - this.originX;
		p.y = this.originY - point.y;
		
		return p;
	}
	
	public void addPoint(Point point)
	{
		Point p = transform(point);
		pointList.add(p);
	}
	
	public void addPairLine(Point start, Point end)
	{
		Line l = transform(new Line(start, end));
		pairLineList.add(l);
	}
	
	public void addHullLine(Point start, Point end)
	{
		Line l = transform(new Line(start, end));
		convexHull.add(l);
	}
	
	public void addDivision(int x)
	{
		Point start = new Point(x, this.originY);
		Point end = new Point(x, -1 * this.originY);
		
		Line l = transform(new Line(start,end));
		divisionList.add(l);
	}
	
	public void addDashed(int x)
	{
		Point start = new Point(x, this.originY);
		Point end = new Point(x, -1 * this.originY);
		
		Line l = transform(new Line(start,end));
		dashedList.add(l);
	}
	
	public void addHighlight(Point point)
	{
		Point p = transform(point);
		highlightList.add(p);
	}
	
	public void addAntipodal(Point start, Point end)
	{	
		Line l = transform(new Line(start, end));
		antipodalList.add(l);
	}
	
	public void addRect(Point start, int width, int height)
	{
		Rectangle r = new Rectangle(transform(start), width, height);
		rectList.add(r);
	}
	
	public void clearPoints()
	{
		pointList.clear();
	}
	
	public void clearLines()
	{
		pairLineList.clear();
	}
	
	public void clearRects()
	{
		rectList.clear();	
	}
	
	public void clearHull()
	{
		convexHull.clear();	
	}
	
	public void clearDivs()
	{
		divisionList.clear();	
	}
	
	public void clearHighlights()
	{
		highlightList.clear();	
	}

	public void clearAntipodals()
	{
		antipodalList.clear();	
	}
	
	public void clearDashed()
	{
		dashedList.clear();	
	}
}
