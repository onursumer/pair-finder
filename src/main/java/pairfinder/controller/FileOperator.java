package pairfinder.controller;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pairfinder.model.PPPoint;

public class FileOperator {
	
	private String getContents (String fileName)
	{
		StringBuilder contents = new StringBuilder();
		
		try {
			BufferedReader input =  new BufferedReader(new FileReader(fileName));
			try {
				String line = null;
		        while (( line = input.readLine()) != null)
		        {
		        	contents.append(line);
		        }    
			}
			finally 
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
		    ex.printStackTrace();
		}
		
		return contents.toString();
	}
	
	private boolean hasPoint (String contents, int begin)
	{
		if ( begin == -1)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private PPPoint getNextPoint (String point)
	{
		// point format: {x,y}
		PPPoint p = new PPPoint();
		p.getPoint().x = Integer.valueOf(point.substring( 1, point.indexOf(',') ));
		p.getPoint().y = Integer.valueOf(point.substring( (point.indexOf(',') + 1),  point.indexOf('}') ));
		return p;
	}
	
	public ArrayList<PPPoint> getPointSet (String fileName)
	{
		ArrayList<PPPoint> pointSet = new ArrayList<PPPoint>();
		
		String contents = getContents(fileName);
		int begin = contents.indexOf('{');
		int end = contents.indexOf('}');
		
		while (hasPoint(contents, begin))
		{
			PPPoint p = getNextPoint(contents.substring(begin, (end + 1)));
			p.setIndex(pointSet.size());
			pointSet.add(p);
			
			begin = contents.indexOf('{', end);
			end = contents.indexOf('}', begin);
		}
		return pointSet;
	}
	
	public void savePointSet (ArrayList<PPPoint> pointSet, String fileName)
	{
		try
		{
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("");
			
			Point p;
			String s;
			for (int i = 0; i < pointSet.size(); i++)
			{
				p = pointSet.get(i).getPoint();
				s = "{" + p.x + "," + p.y + "}";
				out.append(s);
			}
			
			out.close();
		}
		catch (Exception e)
		{
			
		}
	}
}
