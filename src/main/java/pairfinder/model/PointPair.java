package pairfinder.model;

public class PointPair 
{
	private PPPoint[] pair;

	public PointPair() {
		super();
		pair = new PPPoint[2];
	}

	public void setPair(PPPoint[] pair) {
		this.pair = pair;
	}

	public PPPoint[] getPair() {
		return pair;
	}
	
	public PPPoint getFirstPoint()
	{
		return pair[0];
	}
	
	public PPPoint getSecondPoint()
	{
		return pair[1];
	}
	
	public void setFirstPoint(PPPoint p)
	{
		pair[0] = p;
	}
	
	public void setSecondPoint(PPPoint p)
	{
		pair[1] = p;
	}

}
