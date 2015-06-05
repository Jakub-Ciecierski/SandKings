package schedules.tasks.maw;
import java.util.ArrayList;
import java.util.List;

import Enemies.Enemy;
import map.Food;
import communication.knowledge.Information;
import creatures.Formation;
import creatures.CreatureClasses.*;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import schedules.tasks.Task;
import util.SmartConsole;
import util.SmartConsole.DebugModes;

public class FoodNotifyTask extends Task {

	private Maw maw;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		FINISH,
		ALLIANCE
	}
	
	public FoodNotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
			
		SmartConsole.Print("Agent #" + maw.getID() +" New FoodNotifyTask: " + information.getType().toString(), DebugModes.TASK);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		if ( stage == Stages.FINISH ) 
		{
			finish();
			return;
		} 
		else if ( stage == Stages.BEGIN )
		{
			// SPAWN FORMATION FOR THE JOB
			
			Context<Object> context = ContextUtils.getContext(maw);
			ContinuousSpace<Object> space = maw.getSpace(); 
			Grid<Object> grid = maw.getGrid();
			
			goForFood( context, space, grid );
		}
		
	}
	
	private void goForFood(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {

		Food food = (Food) information.getAgent();
		if ( food == null ) {
			stage = Stages.FINISH;
			return;
		}

		int neededBros = food.getWeight();
		
		FormationCreator formationCreator = new FormationCreator(maw, 
												neededBros, 
												Formation.GoingWhere.ForFood,
												information.getGridPoint());
		maw.addPendingFormation(formationCreator);

		stage = Stages.FINISH;
	}

	
	
	
	private void goForFood2(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {

		Food food = (Food) information.getAgent();
		if ( food == null ) {
			stage = Stages.FINISH;
			return;
		}
		
		int neededBros = food.getWeight();
		
		if ( neededBros > maw.getNumberOfFreeChildren() ) return; 
		
		List<Mobile> agents = new ArrayList<Mobile>();
		
		int extent = 5;
		int max = Constants.Constants.BIGGEST_DISTANCE;
		while ( agents.size() < neededBros )
		{
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), neededBros, extent);
			extent += 1;

			if(extent > max)
				return;
		}

		Formation f = new Formation( space, grid, maw.getPlayerID());
		
		context.add(f);
		f.setNeededSize( neededBros );
		NdPoint spacePt = space.getLocation(maw);
		GridPoint gridPt = grid.getLocation(maw);
		
		space.moveTo( f, spacePt.getX(), spacePt.getY());
		grid.moveTo(  f, gridPt.getX(),  gridPt.getY() );
		
		for ( Mobile m : agents )
		{
			f.addToFormation(m);
			
		}
		
		gridPt = grid.getLocation(food);
		if(gridPt == null){
			stage = Stages.FINISH;
			return;
		}

		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.ForFood ); // what's the formation doing?
		f.setGoingPoint( gridPt ); // where's the food?

		stage = Stages.FINISH;
	}

}
