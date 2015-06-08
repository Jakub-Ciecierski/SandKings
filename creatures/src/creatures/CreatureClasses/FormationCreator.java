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
	
	private enum Types {
		STRICT,
		LOOSE
	}

	private Maw maw;
	
	private int size;
	private GridPoint goingPoint;
	private Formation.GoingWhere goingWhere;
	
	private List<Formation> allianceFormations = null;
	
	private Types type;
	
	public FormationCreator(Maw maw, int size, 
							Formation.GoingWhere goingWhere,
							GridPoint goingPoint){
		this.size = size;
		this.goingPoint = goingPoint;
		
		this.maw = maw;
		
		this.goingWhere = goingWhere;
		
		type = Types.STRICT;
	}
	
	public FormationCreator(Maw maw, int size, 
			Formation.GoingWhere goingWhere,
			GridPoint goingPoint,
			List<Formation> allianceFormations){
		this.size = size;
		this.goingPoint = goingPoint;
		
		this.maw = maw;
		
		this.goingWhere = goingWhere;
		
		this.allianceFormations = allianceFormations;
		
		type = Types.LOOSE;
	}
	
	public void setType(Types type){
		this.type = type;
	}
	
	public void addAlliance(List<Formation> allianceFormations){
		this.allianceFormations = allianceFormations;
	}
	
	public boolean AttemptFormation(){

		if(goingPoint == null) {
			return false;
		}
		
		Context<Object> context = ContextUtils.getContext(maw);
		ContinuousSpace<Object> space = maw.getSpace(); 
		Grid<Object> grid = maw.getGrid();

		GridPoint meetingGridPt = grid.getLocation(maw);
		NdPoint meetingSpacePt = space.getLocation(maw);

		List<Mobile> agents = new ArrayList<Mobile>();
		
		int max = Constants.Constants.BIGGEST_DISTANCE;
		int extent = 50;
		
		// Strict - look until found enough
		if(type == Types.STRICT){
			while ( agents.size() < size )
			{
				agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), size, extent);	
				extent += 5;
				
				if(extent > max)
					return false;
			}
		}
		// Loose - go with what you have
		else if(type == Types.LOOSE) {
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), size, extent);
			size = agents.size(); 
		}

		Formation f = new Formation( space, grid, maw.getPlayerID());
		context.add(f);

		GridPoint test =  new GridPoint(25,25);
		NdPoint test1 = new NdPoint(25,25);
		
		f.setNeededSize( size );
		f.setMeetingPoint(meetingGridPt, meetingSpacePt);
		f.addToFormation(agents);
		f.setGoal(goingPoint, goingWhere);

		// Decide if this formation is alone or with alliance
		if(allianceFormations == null){
			f.setCanStartMoving(true);
		}
		else{
			f.setAllianceFormations(allianceFormations);
			allianceFormations.add(f);
			f.setCanStartMoving(true);
		}

		return true;
	}


}
