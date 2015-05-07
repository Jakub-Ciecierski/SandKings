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
	public void AddMaw(Maw m) 
	{	
		_mawList.add(m);
	}
	
}
