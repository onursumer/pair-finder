package pairfinder.main;

import pairfinder.model.PPPoint;
import pairfinder.model.PointSet;

import pairfinder.controller.ClosestPairFinder;
import pairfinder.controller.Controller;
import pairfinder.controller.FarthestPairFinder;
import pairfinder.controller.FileOperator;
import pairfinder.controller.PairFinder;
import pairfinder.gui.MainFrame;

public class Main
{
	public static void main(String[] args)
	{	
		MainFrame.getInstance().setVisible(true);
	}
	
	private static void test01()
	{
		Controller controller = Controller.getInstance();
		FileOperator op = new FileOperator();
		ClosestPairFinder finder = new ClosestPairFinder();
		FarthestPairFinder finder2 = new FarthestPairFinder();
		PairFinder finder3 = new PairFinder();
		
		PointSet set = new PointSet();
		
		/*
		set.setSet(op.getPointSet("point_set.txt"));
		set.setSetSortedByY(op.getPointSet("point_set.txt"));
		*/
		
		controller.randomPPPList();
		
		for ( int i = 0; i < set.getSet().size(); i++)
		{
			System.out.println("Index: " + set.getSet().get(i).getIndex() + ", Point: " + set.getSet().get(i).getPoint());
		}
		
		PPPoint[] closestPair = finder.findClosestPair(set);
		PPPoint[] farthestPair = finder2.findFarthestPair(set);
		finder3.BFClosestPair(set.getSet());
		finder3.BFFarthestPair(set.getSet());
		
		System.out.println("Closest Pair");
		for (int i = 0; i < closestPair.length; i++)
		{
			System.out.println("\tIndex: " + closestPair[i].getIndex() + ", Point" + closestPair[i].getPoint());
		}
		
		System.out.println("Farthest Pair");
		for (int i = 0; i < farthestPair.length; i++)
		{
			System.out.println("Index: " + farthestPair[i].getIndex() + ", Point" + farthestPair[i].getPoint());
		}
		
		//op.savePointSet(set.getSet(), "random.sop");
		//pairfinder.controller.setMainFrame(frame);
		//pairfinder.controller.setPointSet(set);
		//op.savePointSet(op.getPointSet("point_set.txt"), "out.sop");
	}
	
	private static void test2()
	{
		System.out.print("test 02");
	}
}
