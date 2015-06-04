package creatures.CreatureClasses;

import java.util.ArrayList;
import java.util.List;

import creatures.Formation;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import util.SmartConsole;
import util.SmartConsole.DebugModes;

public class FormationCreator {
	
	private Maw maw;
	
	private int size;
	private GridPoint goingPoint;
	
	public FormationCreator(Maw maw, int size, GridPoint goingPoint){
		this.size = size;
		this.goingPoint = goingPoint;
		
		this.maw = maw;
	}
	
	public boolean AttemptFormation(){
		Context<Object> context = ContextUtils.getContext(maw);
		ContinuousSpace<Object> space = maw.getSpace(); 
		Grid<Object> grid = maw.getGrid();
		
		
		List<Mobile> agents = new ArrayList<Mobile>();
		int max = Constants.Constants.BIGGEST_DISTANCE;
		int extent = 5;
		while ( agents.size() < size )
		{
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), size, extent);
			extent += 1;
			
			if(extent > max)
				return false;
			SmartConsole.Print("maw [" + agents.size() + "/" + size + "]    c4attack bros in " + extent, DebugModes.BASIC);
		}
		
		if(goingPoint == null) {

			return false;
		}

		Formation f = new Formation( space, grid, maw.getPlayerID());
		
		context.add(f);
		f.setNeededSize( size );
		NdPoint spacePt = space.getLocation(maw);
		GridPoint gridPt = grid.getLocation(maw);
		
		space.moveTo( f, spacePt.getX(), spacePt.getY());
		grid.moveTo(  f, gridPt.getX(),  gridPt.getY() );
		
		for ( Mobile m : agents )
		{
			f.addToFormation(m);
		}
		SmartConsole.Print("maw [" + agents.size() + "/" + size + "]    added bros " + agents.size() + " to f #" + f.getID(), DebugModes.BASIC);
		
		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.Wpierdol ); // what's the formation doing?
		f.setGoingPoint( goingPoint ); // where's the food?

		SmartConsole.Print("atck formation " + f.getID() + " created at " + gridPt.getX() + ":" + gridPt.getY() + " for " + f.getNeededSize() + ".", DebugModes.BASIC);
		return true;
	}
}
