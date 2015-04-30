package pairfinder.controller;

import java.awt.Point;
import java.util.ArrayList;

import pairfinder.model.PPPoint;
import pairfinder.model.PointSet;

public class ClosestPairFinder extends PairFinder
{
	
	/** finds and returns the closest pair in the given point set */
	public PPPoint[] findClosestPair (PointSet set)
	{
		/** initialization */
		sortByY( set.getSetSortedByY() );
		sortByX( set.getSet() );

		PPPoint[] pair = new PPPoint[2];
		
		// find closest pair
		closestPair(set);
		pair[0] = getPoint(set.getSet(), set.getClosestPairAt(0));
		pair[1] = getPoint(set.getSet(), set.getClosestPairAt(1));
		
		return pair;
	}

	/** finds closest pair in the given point set */
	private void closestPair (PointSet set)
	{
		if ( set.getSet().size() != 1)
		{
			/** calculate the line l for the given set */
			/** l is a perpendicular line which divides the set in the middle */
			int left = 0;
			int right = (set.getSet().size())-1;
			int center = ( left + right ) / 2;
			int rightmostS1 = set.getSet().get(center).getPoint().x;
			int leftmostS2  = set.getSet().get(center +1).getPoint().x;
			int line = (rightmostS1 + leftmostS2) / 2;
			/** Construct S1 and S2 subset: */
			/** S1 U S2 = S */
			/** All points reside at the left of the line l is included in S1 */
			/** All points reside at the right of the line l is included in S2 */
			PointSet S1 = subSet(set, left, center);
			PointSet S2 = subSet(set, center + 1, right);
			
			/** find closest pairs of S1 and S2 */
			closestPair( S1 );
			closestPair( S2 );
			
			/** find the closest pair of S */
			int[] closestPair = mergeClosestPair( S1, S2, line );
			set.setClosestPairAt(0, closestPair[0]);
			set.setClosestPairAt(1, closestPair[1]);
			
			// TODO print("closest pair", set, null);
		}
	}
	
	/** finds the closest pair in S1 U S2 = S */
	private int[] mergeClosestPair (PointSet S1, PointSet S2, int line)
	{	
		/** --------------------------------------------- **/
		// TODO print("merge closest pair, with line: " + line, S1, S2);
		/** --------------------------------------------- **/
		
		int[] closestPair = new int[2]; // indexes of closest pair
		
		// we got three possibilities:
		// 1: closest pair is in S1
		// 2: closest pair is in S2
		// 3: one point in closest pair is in S1 an the other one is in S2
		
		double d1 = minDistance(S1); // minimum distance in S1
		double d2 = minDistance(S2); // minimum distance in S2
		double minDistance = d1;
		
		if ( d2 < d1)
		{
			minDistance = d2;
		}
		
		// |S1| = |S2| = 1
		if ( minDistance == (1.0/0.0) )
		{
			closestPair[0] = S1.getSet().get(0).getIndex();
			closestPair[1] = S2.getSet().get(0).getIndex();
		}
		else
		{
			// d1 < d2, so S2 cannot have the closest pair in S
			if (minDistance == d1)
			{
				closestPair[0] = S1.getClosestPairAt(0);
				closestPair[1] = S1.getClosestPairAt(1);
			}
			// d2 < d1, so S1 cannot have the closest pair in S
			else
			{
				closestPair[0] = S2.getClosestPairAt(0);
				closestPair[1] = S2.getClosestPairAt(1);
			}
			
			// construct a subset Q1 from S1
			// X coordinates of all points in Q1 is between the range line-d and line
			int to = S1.getSet().size()-1;
			int from = 0;
			
			for (int i = to; i >= 0; i--)
			{
				if ( S1.getSet().get(i).getPoint().x < (line-minDistance))
				{
					from = i+1;
					break;
				}
			}
			
			PointSet Q1 = subSet(S1, from, to);
			// TODO print("Q1, line: " + line + ", line-d: " + (line-minDistance), Q1, null);

			// construct a subset Q2 from S2
			// X coordinates of all points in Q2 is between the range line and line+d
			to = 0;
			from = 0;
			
			for (int i = 0; i < S2.getSet().size(); i++)
			{
				if ( S2.getSet().get(i).getPoint().x > (line+minDistance))
				{
					to = i-1;
					break;
				}
				if( i == S2.getSet().size()-1)
				{
					to = i;
				}
			}
			
			PointSet Q2 = subSet(S2, from, to);
			// TODO print("Q2, line: " + line + ", line+d: " + (line+minDistance), Q2, null);
			
			// animate division
			Controller.getInstance().animateDivision(line, S1, S2, Q1, Q2);
			
			// now we have the points in the range "line-d" and "line+d"
			for (int i=0; i<Q1.getSet().size(); i++)
			{
				// animate rectangle
				Controller.getInstance().animateRect(line, Q1.getSet().get(i).getPoint(), minDistance, Q2, S2);
				
				PPPoint[] rectangle = getD2DRectangle(Q2.getSetSortedByY(), Q1.getSet().get(i).getPoint().y, minDistance);
				
				for (int j = 0; j < rectangle.length; j++)
				{
					if (rectangle[j] != null)
					{
						double distance = distance(Q1.getSet().get(i).getPoint(), rectangle[j].getPoint());
						if ( distance < minDistance )
						{
							closestPair[0] = Q1.getSet().get(i).getIndex();
							closestPair[1] = rectangle[j].getIndex();
							minDistance = distance;
						}
					}
				}
			}
		}
		
		return closestPair;
	}
	
	private PPPoint[] getD2DRectangle(ArrayList<PPPoint> set, int y, double distance)
	{
		PPPoint[] rectangle = new PPPoint[6];
		
		if ( set.size() > 0 )
		{
			int arrayI = 0;
			int firstPointI = firstPointInRect(set, (y-distance), 0, (set.size()-1));
			
			for (int i = firstPointI; (i <= (firstPointI+6)) && (i < set.size()); i++)
			{
				if ( set.get(i).getPoint().y > (y+distance) )
				{
					break;
				}
				rectangle[arrayI++] = set.get(i);
			}
		}
		
		return rectangle;
	}
	
	/** return the minimum distance in the given point set */
	private double minDistance (PointSet set)
	{
		Point a = null, b = null;
		
		// find the closest pair points in the set
		for (int i=0; i< set.getSet().size(); i++)
		{
			if (set.getSet().get(i).getIndex() == set.getClosestPairAt(0))
			{
				a = set.getSet().get(i).getPoint();
			}
			else if (set.getSet().get(i).getIndex() == set.getClosestPairAt(1))
			{
				b = set.getSet().get(i).getPoint();
			}
			
			if ( (a != null) && (b != null) )
			{
				break;
			}
		}
		
		// if current set's closest pair is not specified
		if ( a == null || b == null )
		{
			return (1.0/0.0);
		}
		else
		{
			return distance(a,b);
		}
	}
	
}
