package pairfinder.controller;

import java.awt.Point;
import java.util.ArrayList;

import pairfinder.model.PPPoint;
import pairfinder.model.PointPair;
import pairfinder.model.PointSet;

public class FarthestPairFinder extends PairFinder
{

	/** finds and returns the farthest pair in the given point set */
	public PPPoint[] findFarthestPair (PointSet set)
	{
		sortByX(set.getSet());
		set.setConvexHull(new ArrayList<PPPoint>());
		findConvexHull(set);
		return findFarthestPairOnConvexHull(set.getConvexHull());
	}
	
	/** finds the convex hull of a set of point, and stores it in the convexHull array list */
	private void findConvexHull(PointSet set)
	{
		/** find the centroid of the set */
		PPPoint centroid = centroid(set.getSet());
		
		/** transform points in a way that the centroid will be the new origin */
		transformPoints(set, centroid);

		/** sort points lexicographically first by polar angle, and then distance from centroid */
		sortByPolarAngle(set.getConvexHull());
		
		/** find the convex hull of the set via Graham's Scan */
		grahamsScan(set.getConvexHull());
		
		/** re-transform the points to their original positions */
		reTransformPoints(set.getConvexHull(), centroid);
	}
	
	/** find the farthest pair on the given convex hull */
	private PPPoint[] findFarthestPairOnConvexHull(ArrayList<PPPoint> convexHull)
	{
		/** find the antipodal pairs */
		ArrayList<PointPair> antipodalPairs = findAntipodalPairs(convexHull);
		
		// animate convex hull and antipodal pairs
		Controller.getInstance().animateHull();
		Controller.getInstance().animateAntipodals(antipodalPairs);
		
		/** one of the antipodal pairs is the farthest pair */
		return findFarthestInAntipodalPairs(antipodalPairs);
	}
	
	/** calculates and returns the centroid point in the given set */
	private PPPoint centroid (ArrayList<PPPoint> set)
	{
		int x = 0;
		int y = 0;
		int index = -1;

		for (int i = 0; i < set.size(); i++)
		{
			x += set.get(i).getPoint().x;
			y += set.get(i).getPoint().y;
		}
		
		x /= set.size();
		y /= set.size();

		return new PPPoint(x, y, index); 
	}
	
	/** transforms points in a way that the centroid will be the new origin */
	private void transformPoints(PointSet set,  PPPoint centroid)
	{
		int x, y, index;
		
		for (int i = 0; i < set.getSet().size(); i++)
		{
			x = set.getSet().get(i).getPoint().x - centroid.getPoint().x;
			y = set.getSet().get(i).getPoint().y - centroid.getPoint().y;
			index = set.getSet().get(i).getIndex();
			
			set.getConvexHull().add(new PPPoint(x, y, index));
		}
	}
	
	/** re-transforms the points to their original positions */
	private void reTransformPoints(ArrayList<PPPoint> set,  PPPoint centroid)
	{
		for (int i = 0; i < set.size(); i++)
		{
			set.get(i).getPoint().x += centroid.getPoint().x;
			set.get(i).getPoint().y += centroid.getPoint().y;
		}
	}
	
	/** sorts points lexicographically: first by polar angle, and than distance from centroid */
	/** merge sort algorithm is applied for sorting operation */
	private void sortByPolarAngle(ArrayList<PPPoint> set)
	{
		ArrayList<PPPoint> tempSet = new ArrayList<PPPoint>();
		copyPPPointList(set, tempSet);
		
		// initial call to recursive merge sort function
		mergeSortByPolarAngle(set, tempSet, 0, (set.size()-1));
	}
	
	/** recursive merge sort function (sorts points first by polar angle, and than distance from centroid) */
	private void mergeSortByPolarAngle (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, int left, int right)
	{
		if (left < right)
		{
			// calculate center
			int center = ( left + right ) / 2;
			// divide the set, and sort by recursive calls
			mergeSortByPolarAngle( orgSet, copySet, left, center);
			mergeSortByPolarAngle( orgSet, copySet, center + 1, right);
			// merge divided sets
            mergeByPolarAngle( orgSet, copySet, left, center + 1, right);
		}
	}
	
	/** merges two point set */
	private void mergeByPolarAngle (ArrayList<PPPoint> orgSet, ArrayList<PPPoint> copySet, int left, int right, int rightEnd)
	{

		int leftEnd = right - 1;
		int tmpPos = left; 
		int numElements = rightEnd - left + 1;
	    int area;
	    
	    // merge while neither left nor right set is completely scanned
	    while( left <= leftEnd && right <= rightEnd )
	    {
	    	area = comparePoints(orgSet.get(left).getPoint(), orgSet.get(right).getPoint());
	    	// insert from left
	    	if( area > 0 )
	    	{
	    		copyPPPoint(orgSet.get(left), copySet.get(tmpPos));
	    		tmpPos++; left++;
	    	}
	    	// insert from right
	    	else{
	    		copyPPPoint(orgSet.get(right), copySet.get(tmpPos));
	    		tmpPos++; right++;
	    	}	 
	    }
	    
	    // insert remaining point
	    insertRemainedPoints(orgSet, copySet, left, leftEnd, right, rightEnd, tmpPos, numElements);
	}
	
	/** Convex hull of a given set is calculated via Graham's Scan algorithm */
	private void grahamsScan (ArrayList<PPPoint> set)
	{
		// store start index
		int startI = findLowestY(set);
		int currentI = startI;
		
		/** v: current point 
		 *  w: the point before the start point */
		PPPoint v = set.get(currentI);
		int startIndex = v.getIndex();
		PPPoint w = set.get(pred(currentI, set.size()));
		
		boolean cond = false;
		
		// scan the sorted (according to polar angle and distance from the centroid) 
		// point set through counter-clockwise direction
		while ( (! cond ) || (set.get(next(currentI, set.size())).getIndex() != startIndex) )
		{
			// store the next point
			int nextVI = next(currentI, set.size());
			// store the point after the next point
			int nextnextVI = next(nextVI, set.size());
			
			// make condition true if we reach the final three point in the convex hull
			if ( set.get(nextVI).getIndex() == w.getIndex() )
			{
				cond = true;
			}
			
			v = set.get(currentI);
			PPPoint nextV = set.get(nextVI);
			PPPoint nextnextV = set.get(nextnextVI);
			
			// if current three point (v, next[v], next[next[v]]) is a left turn, then next[v] stays in the convex hull
			if ( leftTurn(v.getPoint(), nextV.getPoint(), nextnextV.getPoint()) )
			{
				currentI = nextVI;
			}
			// otherwise, next[v] is removed from the convex hull
			// and, in the next scan, check pred[v], v, and next[v] points for left turn
			else
			{
				set.remove(nextVI);
				int predVI = pred(currentI, set.size());
				currentI = predVI;
			}
		}
	}
	
	/** finds and returns all antipodal pairs in a given convex hull */
	private ArrayList<PointPair> findAntipodalPairs (ArrayList<PPPoint> convexHull)
	{
		ArrayList<PointPair> antipodalPairs = new ArrayList<PointPair>();
		
		int k = 1;
		int m = convexHull.size()-1; // store the last vertex
		
		// find the farthest point from the edge between m and first vertex (say v1)
		while ( area(convexHull.get(m).getPoint(), convexHull.get(0).getPoint(), convexHull.get(k+1).getPoint()) 
				> area(convexHull.get(m).getPoint(), convexHull.get(0).getPoint(), convexHull.get(k).getPoint()) )
		{
			k++;
		}
		
		int i = 0;
		int j = k;
		
		// add v1-vk pair to antipodal pair set, and scan for other antipodal pairs have v1
		// while scanning, we look for the farthest point from the v1-v2 edge (say vl)
		// points between vk and vl (v(k), v(k+1), ..., v(l)) and v1 are antipodal pairs, and they are added to the array list
		// continue iteration until we reach the vm point.
		while ( (i <= k) && (j <= m) )
		{
			PointPair pair = new PointPair();
			pair.setFirstPoint(convexHull.get(i));
			pair.setSecondPoint(convexHull.get(j));
			antipodalPairs.add(pair);
			
			while ( (j < m) && (area(convexHull.get(i).getPoint(), convexHull.get(i+1).getPoint(), convexHull.get(j+1).getPoint()) 
					> area(convexHull.get(i).getPoint(), convexHull.get(i+1).getPoint(), convexHull.get(j).getPoint())))
			{				
				j++;
				
				if( j <= m)
				{
					PointPair pair2 = new PointPair();
					pair2.setFirstPoint(convexHull.get(i));
					pair2.setSecondPoint(convexHull.get(j));
					antipodalPairs.add(pair2);
				}
				
			}
			
			i++;
		}
		
		return antipodalPairs;
	}
	
	/** return the index of next element */
	private int next(int current, int size)
	{
		return ( current + 1 ) % size;
	}
	
	/** return the index of previous element */
	private int pred(int current, int size)
	{
		if ( (current - 1) < 0 )
		{
			return size-1;
		}
		else if ( current == size)
		{
			return current-2;
		}
		else
		{
			return current-1;
		}
	}
	
	/** returns the index (in the array list) of lowest y value */
	private int findLowestY(ArrayList<PPPoint> set)
	{
		int lowestYIndex = 0;
		int lowestYValue = set.get(0).getPoint().y;
		
		for (int i = 0; i < set.size(); i++){
			
			if ( set.get(i).getPoint().y < lowestYValue)
			{
				lowestYIndex = i;
				lowestYValue = set.get(i).getPoint().y;
			}
		}
		
		return lowestYIndex;
	}
	
	/** comparison is done by first looking polar angles, if angles are equal, than distances from centroid (0,0) are calculated */
	private int comparePoints (Point a, Point b)
	{
		int area = ((a.x * b.y) - (b.x * a.y)) / 2;
		
		// if polat angles are equal
		if ( area == 0)
		{
			// than calculate distances from (0,0)
			double oa = distance(new Point(0,0),a);
			double ob = distance(new Point(0,0),b);
			
			if (oa > ob) return 1;
			else return -1;
		}
		else
		{
			return area;
		}
	}
	
	/** after finding antipodal pairs, find the farthest pair with a brute force algorithm */
	private PPPoint[] findFarthestInAntipodalPairs(ArrayList<PointPair> antipodalPairs)
	{
		PPPoint[] farthestPair = new PPPoint[2];
		PPPoint p1 = null, p2 = null;
		
		/** find farthest pair */
		double distance = 0.0;
		
		for ( int i = 0; i < antipodalPairs.size(); i++ )
		{
			double currDistance = distance(antipodalPairs.get(i).getFirstPoint().getPoint(), antipodalPairs.get(i).getSecondPoint().getPoint());
			if ( currDistance > distance )
			{
				distance = currDistance;
				p1 = antipodalPairs.get(i).getFirstPoint();
				p2 = antipodalPairs.get(i).getSecondPoint();
			}
		}
		
		farthestPair[0] = p1;
		farthestPair[1] = p2;
		
		return farthestPair;
	}
}
