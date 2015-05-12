package creatures.CreatureClasses;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.space.grid.GridPoint;

public class MawFinder {
	// singleton
	private static MawFinder instance = new MawFinder();
	public static MawFinder Instance() { return instance; }
	protected MawFinder() { }
	
	private List<Maw> _mawList = new ArrayList<Maw>();
	public Maw GetMaw( int id )
	{
		for ( Maw m : _mawList )
		{
			if ( m.getPlayerID() == id ) return m;
		}
		return null;
	}
	public GridPoint GetMawPosition( int id )
	{
		for ( Maw m : _mawList )
		{
			if ( m.getPlayerID() == id ) 
				return m.getGridpos();
		}
		return null;
	}
	public double GetDistanceToMaw( int id, int x, int y )
	{
		int x1 = GetMawPosition( id ).getX();
		int x2 = x;
		
		int y1 = GetMawPosition( id ).getY();
		int y2 = y;
		
		return Math.sqrt( Math.pow(( x2-x1 ), 2) + Math.pow(( y2-y1 ), 2) );
	}
	
	public void AddMaw(Maw m) 
	{	
		_mawList.add(m);
	}
	
}
