package pairfinder.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import pairfinder.model.PPPoint;
import pairfinder.model.PointPair;
import pairfinder.model.PointSet;
import pairfinder.gui.MainFrame;

public class Controller 
{	
	public static final int DEFAULT_INTERVAL = 2500;
	public static final int DEFAULT_SIZE = 50;
	
	private static Controller singleton = new Controller();
 	
	public Object mutex;
	
	private PointSet pointSet;
	
	private ClosestPairFinder cpf;
	private FarthestPairFinder fpf;
	private PairFinder bfpf;
	private FileOperator fileOperator;
	
	private int animInterval;
	private int setSize;
	private boolean paused;
	
	/**
	 * Constructor
	 */
	private Controller()
	{
		super();
		cpf = new ClosestPairFinder();
		fpf = new FarthestPairFinder();
		bfpf = new PairFinder();
		fileOperator = new FileOperator();
		pointSet = new PointSet();
		animInterval = DEFAULT_INTERVAL;
		paused = false;
		mutex = new Object();
		setSize = DEFAULT_SIZE;
	}
	
	public static Controller getInstance()
	{
		return singleton;
	}
	
	/**
	 * Updates the canvas by adding the points in the point set to the canvas.
	 */
	public void updatePoints()
	{
		MainFrame frame = MainFrame.getInstance();
		
		frame.getCanvas().clearPoints();
		
		for (int i = 0; i < this.pointSet.getSet().size(); i++)
		{
			frame.getCanvas().addPoint(this.pointSet.getSet().get(i).getPoint());
		}
		
		frame.getCanvas().repaint();
	}
	
	/**
	 * Generates a random point list of size setSize.
	 */
	public void randomPPPList()
	{
		ArrayList<PPPoint> set = new ArrayList<PPPoint>();
		ArrayList<PPPoint> copySet = new ArrayList<PPPoint>();
		
		for (int i = 0; i < this.setSize; i++)
		{
			PPPoint pppoint = new PPPoint();
			
			pppoint.setPoint(randPoint());
			pppoint.setIndex(i);
			
			set.add(pppoint);
		}
		
		this.pointSet.setSet(set);
		
		this.bfpf.copyPPPointList(set, copySet);
		this.pointSet.setSetSortedByY(copySet);
	}
	
	/**
	 * Generates a random point within the visible canvas bounds,
	 * and returns the generated point.
	 * 
	 * @return	generated point. 
	 */
	private Point randPoint()
	{
		MainFrame frame = MainFrame.getInstance();
		
		int width = frame.getCanvas().getPreferredSize().width;
		int height = frame.getCanvas().getPreferredSize().height;
		
		Random generator = new Random();
		
		int p = generator.nextInt(width) - width/2;
		int r = generator.nextInt(height) - height/2;
		
		return new Point(p,r);
	}
	
	/**
	 * Generates a random point and adds it to the point set.
	 */
	public void randomPoint()
	{
		PPPoint pppoint = new PPPoint();
		PPPoint copyPoint = new PPPoint();
		
		pppoint.setPoint(randPoint());
		pppoint.setIndex(this.pointSet.getSet().size());
		
		this.pointSet.getSet().add(pppoint);
		
		this.bfpf.copyPPPoint(pppoint, copyPoint);
		this.pointSet.getSetSortedByY().add(copyPoint);
	}
	
	/**
	 * Adds a specific point to the point canvas.
	 * Point coordinates are taken from the input fields of the toolbar. 
	 */
	public void addPoint()
	{
		MainFrame frame = MainFrame.getInstance();
		PPPoint pppoint = new PPPoint();
		
		String xVal = frame.getToolbar().getXval();
		String yVal = frame.getToolbar().getYval();
		
		boolean error = false;
		
		if((xVal == null) || (yVal == null))
			error = true;
		else if(!xVal.matches("[-]?[0-9]+"))
			error = true;
		else if(!yVal.matches("[-]?[0-9]+"))
			error = true;
		
		if(error)
		{
			frame.showErrorMsg("Invalid input!");
			return;
		}
		
		pppoint.setPoint(new Point(Integer.parseInt(xVal), Integer.parseInt(yVal)));
		pppoint.setIndex(this.pointSet.getSet().size());
		
		this.pointSet.getSet().add(pppoint);
	}
	
	/**
	 * Clears the current point set.
	 */
	public void clearPointSet()
	{
		this.pointSet = new PointSet();
	}
	
	/**
	 * Clears the point canvas by removing all geometric objects excepts axes.
	 */
	public void clearCanvas()
	{
		MainFrame frame = MainFrame.getInstance();
		
		frame.getCanvas().clearPoints();
		frame.getCanvas().clearLines();
		frame.getCanvas().clearHull();
		frame.getCanvas().clearRects();
		frame.getCanvas().clearDashed();
		frame.getCanvas().clearAntipodals();
		frame.getCanvas().clearHighlights();
		frame.getCanvas().repaint();
	}
	
	/**
	 * Finds the closest point pair and animates the process on the canvas.
	 */
	public void fcp()
	{
		PPPoint[] pair;
		MainFrame frame = MainFrame.getInstance();
		
		if(this.pointSet == null || 
				this.pointSet.getSet() == null || 
				this.pointSet.getSet().size() == 0)
		{
			frame.showErrorMsg("Empty point set!");
			return;
		}
		
		pair = this.cpf.findClosestPair(this.pointSet);
		
		frame.getCanvas().clearDivs();
		frame.getCanvas().clearDashed();
		frame.getCanvas().addPairLine(pair[0].getPoint(), pair[1].getPoint());
		frame.getCanvas().repaint();
		
		frame.setInfo("Closest pair: " + "(" + pair[0].getPoint().x + "," + pair[0].getPoint().y + ")" +
				" & " + "(" + pair[1].getPoint().x + "," + pair[1].getPoint().y + ")");
	}
	
	/**
	 * Finds the farthest point pair and animates the process on the canvas.
	 */
	public void ffp()
	{
		PPPoint[] pair;
		MainFrame frame = MainFrame.getInstance();
		
		if(this.pointSet == null || 
				this.pointSet.getSet() == null || 
				this.pointSet.getSet().size() == 0)
		{
			frame.showErrorMsg("Empty point set!");
			return;
		}
		
		// clear previous lines, rects, etc.
		clearResults();
		
		pair = this.fpf.findFarthestPair(this.pointSet);
		
		frame.getCanvas().addPairLine(pair[0].getPoint(), pair[1].getPoint());
		
		this.suspend();
		
		frame.getCanvas().repaint();
		
		frame.setInfo("Farthest pair: " + "(" + pair[0].getPoint().x + "," + pair[0].getPoint().y + ")" +
				" & " + "(" + pair[1].getPoint().x + "," + pair[1].getPoint().y + ")");
	}
	
	/**
	 * Removes everything from the point canvas, except points and axes.
	 */
	public void clearResults()
	{
		MainFrame frame = MainFrame.getInstance();
		
		frame.getCanvas().clearAntipodals();
		frame.getCanvas().clearRects();
		frame.getCanvas().clearDashed();
		frame.getCanvas().clearDivs();
		frame.getCanvas().clearHull();
		frame.getCanvas().clearHighlights();
		frame.getCanvas().clearLines();
	}

	/**
	 * Brute force closest pair finder.
	 */
	public void bfcp()
	{
		if(this.pointSet == null || 
				this.pointSet.getSet() == null || 
				this.pointSet.getSet().size() == 0)
		{
			MainFrame.getInstance().showErrorMsg("Empty point set!");
		}
		else
			this.bfpf.BFClosestPair(this.pointSet.getSet());
	}
	
	/**
	 * Brute force farthest pair finder.
	 */
	public void bffp()
	{
		if(this.pointSet == null || 
				this.pointSet.getSet() == null || 
				this.pointSet.getSet().size() == 0)
		{
			MainFrame.getInstance().showErrorMsg("Empty point set!");
		}
		else
			this.bfpf.BFFarthestPair(this.pointSet.getSet());
	}
	
	/**
	 * Stores the information of the current point set 
	 * to the specified file.
	 * 
	 * @param filename	output filename
	 */
	public void save(String filename)
	{
		fileOperator.savePointSet(pointSet.getSet(), filename);
	}
	
	/**
	 * Loads the point set stored in the file with given name
	 *  
	 * @param filename	input filename
	 */
	public void load(String filename)
	{
		ArrayList<PPPoint> set = fileOperator.getPointSet(filename);
		ArrayList<PPPoint> copySet = new ArrayList<PPPoint>();
		
		this.clearPointSet();
		this.clearCanvas();
		
		this.pointSet.setSet(set);
		
		this.bfpf.copyPPPointList(set, copySet);
		this.pointSet.setSetSortedByY(copySet);
	}
	
	/**
	 * Draws a rectangle during the process of candidate closest pair searching,
	 * for the specific point.
	 * 
	 * @param line			line to be animated
	 * @param point			point to be highlighted
	 * @param minDistance	dimension criteria for the rectangle
	 * @param Q2			point Set Q2
	 * @param S2			point Set S2
	 */
	public void animateRect(int line, Point point, double minDistance, PointSet Q2, PointSet S2)
	{
		MainFrame frame = MainFrame.getInstance();
		
		int x = line;
		int y = point.y + (int)minDistance;
		int height = (int)(2 * minDistance);
		int width = (int)minDistance;
		int distance;
		
		// clip the rectangle if necessary
		if(Q2.getSet().size() > 0)
		{
			distance = Q2.getSet().get(Q2.getSet().size() - 1).getPoint().x - line;
			
			if(width > distance)
				width = distance;
		}
		else
		{
			distance = S2.getSet().get(S2.getSet().size() - 1).getPoint().x - line;
			
			if(width > distance)
				width = distance;
		}
			
		
		frame.getCanvas().addRect(new Point(x,y), width, height);
		frame.getCanvas().addHighlight(point);
		frame.setInfo("Searching closest pair candidates for the point (" + point.x + "," + point.y + ")");
		frame.getCanvas().repaint();
		
		this.suspend();
		
		frame.getCanvas().clearRects();
		frame.getCanvas().clearHighlights();
	}
	
	/**
	 * Draws vertical lines for the given line, and for the sets
	 * S1, S2, Q1, Q2.
	 * 
	 * @param lineX		x coordinate of search line
	 * @param S1		point set S1
	 * @param S2		point Set S2
	 * @param Q1		point Set Q1
	 * @param Q2		point Set Q2
	 */
	public void animateDivision(int lineX, PointSet S1, PointSet S2, PointSet Q1, PointSet Q2)
	{	
		int lastS2 = S2.getSet().size() - 1;
		int lastQ2 = Q2.getSet().size() - 1;
		
		MainFrame frame = MainFrame.getInstance();
		
		frame.getCanvas().clearDivs();
		frame.getCanvas().clearDashed();
		
		// add left border of the point set S1
		frame.getCanvas().addDivision(S1.getSet().get(0).getPoint().x);
		
		// add left border of the Q1 (if exists)
		if(Q1.getSet().size() > 0)
			frame.getCanvas().addDashed(Q1.getSet().get(0).getPoint().x);
		
		frame.getCanvas().addDivision(lineX);		
		
		// add right border of the point set Q2 (if exists)
		if(Q2.getSet().size() > 0)
			frame.getCanvas().addDashed(Q2.getSet().get(lastQ2).getPoint().x);
		
		// add right border of the point set S2
		frame.getCanvas().addDivision(S2.getSet().get(lastS2).getPoint().x);
	}
	
	/**
	 * Draws antipodal pairs during in the given pair list
	 * 
	 * @param antipodalPairs	antipodals to be drawn
	 */
	public void animateAntipodals(ArrayList<PointPair> antipodalPairs)
	{
		MainFrame frame = MainFrame.getInstance();
		Iterator<PointPair> iter = antipodalPairs.iterator();
		PointPair pp;
		
		while(iter.hasNext())
		{
			frame.setInfo("Finding antipodal pairs...");
			
			pp = iter.next();
			
			frame.getCanvas().addAntipodal(pp.getFirstPoint().getPoint(),
				pp.getSecondPoint().getPoint());
			
			this.suspend();
			
			frame.getCanvas().repaint();
		}
	}
	
	/**
	 * Draws the convex hull of the point set.
	 */
	public void animateHull()
	{
		MainFrame frame = MainFrame.getInstance();
		Iterator<PPPoint> iter = this.pointSet.getConvexHull().iterator();
		PPPoint prev = null, current = null, first = null;
		
		while(iter.hasNext())
		{
			frame.setInfo("Constructing convex hull...");
			
			current = iter.next();
			
			if(prev != null)
				frame.getCanvas().addHullLine(prev.getPoint(), current.getPoint());
			else
				first = current;
			
			prev = current;
			
			this.suspend();
			
			frame.getCanvas().repaint();
		}
		
		frame.getCanvas().addHullLine(first.getPoint(), current.getPoint());
		this.suspend();
		
		frame.getCanvas().repaint();
	}
	
	/**
	 * Sleeps the thread for a specific amount of time. If the animation
	 * is paused, then suspends the thread until it is notified. 
	 */
	private void suspend()
	{
		synchronized (mutex)
		{
			while(paused)
			{
				MainFrame.getInstance().appendInfo("[PAUSED]");
				
				try {
		              mutex.wait();
				} catch (InterruptedException ex) {
		              
				}
			}
		}
		
		try {
			Thread.sleep(animInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Boolean isPaused()
	{
		return paused;
	}

	public void setPaused(Boolean paused)
	{
		this.paused = paused;
	}

	public void setAnimInterval(int interval)
	{
		this.animInterval = interval;
	}
	
	public void setRandomSize(int setSize)
	{
		this.setSize = setSize;
	}
	
	public PointSet getPointSet()
	{
		return pointSet;
	}
}
