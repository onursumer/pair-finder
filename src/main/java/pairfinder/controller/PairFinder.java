package pairfinder.controller;

import java.awt.Point;
import java.util.ArrayList;

import pairfinder.model.PPPoint;
import pairfinder.model.PointPair;
import pairfinder.model.PointSet;

public class PairFinder 
{

	/** returns the distance between two points */
	protected double distance (Point a, Point b)
	{
		return (Math.sqrt( Math.pow(Math.abs(a.x - b.x), 2) + Math.pow(Math.abs(a.y - b.y), 2)));
		
	}
	
	/** returns the area of the oab triangle */
	protected double area(Point o, Point a, Point b)
	{
		return ((o.x * a.y) + (b.x * o.y) + (a.x * b.y) - (b.x * a.y) - (o.x * b.y) - (a.x * o.y)) / 2;
	}
	
	/** returns true if oab is a left turn */
	protected boolean leftTurn(Point o, Point a, Point b)
	{
		int area = ((o.x * a.y) + (b.x * o.y) + (a.x * b.y) - (b.x * a.y) - (o.x * b.y) - (a.x * o.y)) / 2;
		
		if ( area > 0)
			return true;
		else
			return false;
	}
	
	protected void copyPPPoint(PPPoint source, PPPoint destination)
	{
		destination.setIndex(source.getIndex());
		destination.getPoint().y = source.getPoint().y;
		destination.getPoint().x = source.getPoint().x;
	}
	
	protected void copyPPPointList(ArrayList<PPPoint> source, ArrayList<PPPoint> destination)
	{
		for (int i=0; i<source.size(); i++)
		{
			destination.add(new PPPoint(source.get(i)));
		}
	}
	
	// returned subset: set[from] + set[from+1] + ... + set[to]
	protected PointSet subSet (PointSet set, int from, int to)
	{
		PointSet subSet = new PointSet();
		
		if ( to >= set.getSet().size())
		{
			return subSet;
		}
		
		for (int i = from; i <= to; i++)
		{
			subSet.getSet().add(new PPPoint(set.getSet().get(i)));
			subSet.getSetSortedByY().add(new PPPoint(set.getSet().get(i)));
		}
		
		sortByY(subSet.getSetSortedByY());
		
		return subSet;
	}
	
	/** inserts the remained points during the merge operation */
	protected void insertRemainedPoints (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, 
										int left, int leftEnd, int right, int rightEnd, 
										int tmpPos, int numElements)
	{
		// insert remained points in the left set
	    while( left <= leftEnd )
	    {
	    	copyPPPoint(orgSet.get(left), copySet.get(tmpPos));
    		tmpPos++; left++;
	    }
	    // insert remained points in the right set
	    while( right <= rightEnd )
	    {
	    	copyPPPoint(orgSet.get(right), copySet.get(tmpPos));
    		tmpPos++; right++;
	    }

	    // copy tmpArray back
	    for( int i = 0; i < numElements; i++, rightEnd-- )
	    {
	    	copyPPPoint(copySet.get(rightEnd), orgSet.get(rightEnd));
	    }
	}
	
	protected PPPoint getPoint (ArrayList<PPPoint> set, int index)
	{
		for (int i = 0; i < set.size(); i++)
			if (set.get(i).getIndex() == index)
				return set.get(i);
		
		return null;
	}
	
	/** returns the point P (index of point in the array list): P.y = "value" 
	 ** or returns the point P which has the smallest y coordinate that is greater than "value" */
	/** set should be sorted! */
	protected int firstPointInRect (ArrayList<PPPoint> set, double value, int left, int right)
	{
		if (left >= right)
		{
			return right;
		}
		else
		{
			int leftEnd = (left+right) / 2;
			
			if (set.get(leftEnd).getPoint().y >= value)
			{
				return firstPointInRect(set, value, left, leftEnd);
			}
			else
			{
				return firstPointInRect(set, value, leftEnd+1, right);
			}
		}
	}
	
	/** merges two point set (according to x coordinates) */
	private void mergeByX (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, int left, int right, int rightEnd)
	{
	
		int leftEnd = right - 1;
		int tmpPos = left; 
		int numElements = rightEnd - left + 1;
	    
		// merge while neither left nor right set is completely scanned
	    while( left <= leftEnd && right <= rightEnd )
	    {
	    	// insert from left
	    	if( orgSet.get(left).getPoint().x <= orgSet.get(right).getPoint().x )
	    	{
	    		copyPPPoint(orgSet.get(left), copySet.get(tmpPos));
	    		tmpPos++; left++;
	    	}
	    	// insert from right
	    	else
	    	{
	    		copyPPPoint(orgSet.get(right), copySet.get(tmpPos));
	    		tmpPos++; right++;
	    	}
	    }
	    
	    insertRemainedPoints(orgSet, copySet, left, leftEnd, right, rightEnd, tmpPos, numElements);
	}

	/** merges two point set (according to y coordinates) */
	private void mergeByY (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, int left, int right, int rightEnd)
	{
		int leftEnd = right - 1;
		int tmpPos = left; 
		int numElements = rightEnd - left + 1;
	    
		// merge while neither left nor right set is completely scanned
	    while( left <= leftEnd && right <= rightEnd )
	    {
	    	// insert from left
	    	if( orgSet.get(left).getPoint().y <= orgSet.get(right).getPoint().y )
	    	{
	    		copyPPPoint(orgSet.get(left), copySet.get(tmpPos));
	    		tmpPos++; left++;
	    	}
	    	// insert from right
	    	else
	    	{
	    		copyPPPoint(orgSet.get(right), copySet.get(tmpPos));
	    		tmpPos++; right++;
	    	}	 
	    }
	    
	    // insert remaining point
	    insertRemainedPoints(orgSet, copySet, left, leftEnd, right, rightEnd, tmpPos, numElements);
	}

	/** recursive merge sort function (sorts points according to x coordinates) */
	private void mergeSortByX (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, int left, int right)
	{
		if (left < right)
		{
			// calculate center
			int center = ( left + right ) / 2;
			// divide the set, and sort by recursive calls
			mergeSortByX( orgSet, copySet, left, center );
			mergeSortByX( orgSet, copySet, center + 1, right );
			// merge divided sets
			mergeByX( orgSet, copySet, left, center + 1, right );
		}
	}

	/** recursive merge sort function (sorts points according to y coordinates) */
	private void mergeSortByY (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, int left, int right)
	{
		if (left < right)
		{
			// calculate center
			int center = ( left + right ) / 2;
			// divide the set, and sort by recursive calls
			mergeSortByY( orgSet, copySet, left, center );
			mergeSortByY( orgSet, copySet, center + 1, right );
			// merge divided sets
	        mergeByY( orgSet, copySet, left, center + 1, right );
		}
	}

	/** sorts points according to x coordinates with merge sort */
	protected void sortByX (ArrayList<PPPoint> pointSet)
	{
		ArrayList<PPPoint> tempSet = new ArrayList<PPPoint>();
		copyPPPointList(pointSet, tempSet);
		
		// initial call to recursive merge sort function
		mergeSortByX(pointSet, tempSet, 0, (pointSet.size()-1));
		
	}

	/** sorts points according to y coordinates with merge sort */
	protected void sortByY (ArrayList<PPPoint> pointSet)
	{
		ArrayList<PPPoint> tempSet = new ArrayList<PPPoint>();
		copyPPPointList(pointSet, tempSet);
		
		// initial call to recursive merge sort function
		mergeSortByY(pointSet, tempSet, 0, (pointSet.size()-1));
	}
	
	/** prints given set(s) with a start and an ending message */
	@Deprecated
	protected void print(String msg, PointSet S1, PointSet S2)
	{
		
		System.out.println("-----START: " + msg.toUpperCase() + "-----");
		
		if ( S2 != null )
		{
			System.out.println("SET: S1");
			printPointSet(S1);
			System.out.println("SET: S2");
			printPointSet(S2);
		}
		else
		{
			System.out.println("SET: S");
			printPointSet(S1);
		}
		
		System.out.println("-----END: " + msg.toUpperCase() + "-----");
	}
	
	/** */
	@Deprecated
	protected void print(String msg, ArrayList<PPPoint> pointSet, ArrayList<PointPair> pairSet)
	{
		System.out.println("-----START: " + msg.toUpperCase() + "-----");
		
		if ( pointSet != null )
		{
			System.out.println("Point Set");
			for (int i = 0; i < pointSet.size(); i++)
			{
				System.out.println("\t Index: " + pointSet.get(i).getIndex() + ", Point: " + pointSet.get(i).getPoint());
			}
		}
		else if ( pairSet != null )
		{
			System.out.println("Pair Set");
			for (int i = 0; i < pairSet.size(); i++)
			{
				System.out.println("\tPoint 1:");
				System.out.println("\tIndex: " + pairSet.get(i).getFirstPoint().getIndex() + ", Point: " + pairSet.get(i).getFirstPoint().getPoint());
				
				System.out.println("\tPoint 2:");
				System.out.println("\tIndex: " + pairSet.get(i).getSecondPoint().getIndex() + ", Point: " + pairSet.get(i).getSecondPoint().getPoint());
			}
		}
		else
		{
			
		}
		
		System.out.println("-----END: " + msg.toUpperCase() + "-----");
	}
	
	@Deprecated
	protected void printPointSet(PointSet set)
	{
		System.out.println("index1: " + set.getClosestPairAt(0));
		System.out.println("index2: " + set.getClosestPairAt(1));
		
		System.out.println("orginal set: ");
		for (int i=0; i<set.getSet().size(); i++)
		{
			System.out.println(set.getSet().get(i).getPoint() + " , index: " + set.getSet().get(i).getIndex());
		}
	}
	
	/** finds and prints the closest pair in the set */
	@Deprecated
	public void BFClosestPair (ArrayList<PPPoint> set)
	{
		/** find closest pair */
		double distance = 1.0/0.0;
		int index1 = -1;
		int index2 = -1;
		Point point1 = new Point();
		Point point2 = new Point();
		
		for ( int i = 0; i < set.size(); i++ )
		{
			for ( int j = i+1; j < set.size(); j++ )
			{
				double currDistance = distance(set.get(i).getPoint(), set.get(j).getPoint());
				if ( currDistance < distance )
				{
					distance = currDistance;
					index1 = set.get(i).getIndex();
					index2 = set.get(j).getIndex();
					point1 = set.get(i).getPoint();
					point2 = set.get(j).getPoint();
				}
			}
		}
		
		/** print closest pair */
		System.out.println("--CLOSEST PAIR--");
		System.out.println("Point 1: , index = " + index1 + ", Coordinates: " + point1);
		System.out.println("Point 2: , index = " + index2 + ", Coordinates: " + point2);
	}
	
	/** finds and prints the closest pair in the set */
	@Deprecated
	public void BFFarthestPair (ArrayList<PPPoint> set)
	{
		/** find farthest pair */
		double distance = 0.0;
		int index1 = -1;
		int index2 = -1;
		Point point1 = new Point();
		Point point2 = new Point();
		
		for ( int i = 0; i < set.size(); i++ )
		{
			for ( int j = i+1; j < set.size(); j++ )
			{
				double currDistance = distance(set.get(i).getPoint(), set.get(j).getPoint());
				if ( currDistance > distance )
				{
					distance = currDistance;
					index1 = set.get(i).getIndex();
					index2 = set.get(j).getIndex();
					point1 = set.get(i).getPoint();
					point2 = set.get(j).getPoint();
				}
			}
		}
		
		/** print farthest pair */
		System.out.println("--FARTHEST PAIR--");
		System.out.println("Point 1: , index = " + index1 + ", Coordinates: " + point1);
		System.out.println("Point 2: , index = " + index2 + ", Coordinates: " + point2);
	}

}
