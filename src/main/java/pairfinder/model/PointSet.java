package pairfinder.model;

import java.util.ArrayList;

public class PointSet {

	private int[] closestPair; // index numbers of closest pair

	private ArrayList<PPPoint> set;
	private ArrayList<PPPoint> convexHull;
	private ArrayList<PPPoint> setSortedByY;
	
	public PointSet() 
	{
		super();
		this.closestPair = new int[2];
		this.set = new ArrayList<PPPoint>();
		this.setSortedByY = new ArrayList<PPPoint>();
		this.convexHull = new ArrayList<PPPoint>();
	}
	
	public void setClosestPairAt (int i, int val)
	{
		this.closestPair[i] = val;
	}
	
	public int getClosestPairAt (int i)
	{
		return closestPair[i];
	}

	public ArrayList<PPPoint> getSet() {
		return set;
	}

	public void setSet(ArrayList<PPPoint> set) {
		this.set = set;
	}

	public void setSetSortedByY(ArrayList<PPPoint> sortedListAccordingToY) {
		this.setSortedByY = sortedListAccordingToY;
	}

	public ArrayList<PPPoint> getSetSortedByY() {
		return setSortedByY;
	}

	public void setConvexHull(ArrayList<PPPoint> set) {
		this.convexHull = set;
	}

	public ArrayList<PPPoint> getConvexHull() {
		return convexHull;
	}
	
	
}
