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
	private Formation.GoingWhere goingWhere;
	
	public FormationCreator(Maw maw, int size, 
							Formation.GoingWhere goingWhere,
							GridPoint goingPoint){
		this.size = size;
		this.goingPoint = goingPoint;
		
		this.maw = maw;
		
		this.goingWhere = goingWhere;
	}
	
	public boolean AttemptFormation(){
		Context<Object> context = ContextUtils.getContext(maw);
		ContinuousSpace<Object> space = maw.getSpace(); 
		Grid<Object> grid = maw.getGrid();

		GridPoint meetingGridPt = grid.getLocation(maw);
		NdPoint meetingSpacePt = space.getLocation(maw);

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

		GridPoint test =  new GridPoint(25,25);
		NdPoint test1 = new NdPoint(25,25);
		
		f.setNeededSize( size );
		f.setMeetingPoint(meetingGridPt, meetingSpacePt);
		f.addToFormation(agents);
		f.setCanStartMoving(true);
		f.initiateGoal(goingPoint, goingWhere);

		return true;
	}


}
