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

public class NotifyTask extends Task {

	private Maw maw;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		FINISH,
		ALLIANCE
	}
	
	public NotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
			
		SmartConsole.Print("Agent #" + maw.getID() +" New NotifyTask: " + information.getType().toString(), DebugModes.TASK);
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
						
			switch ( information.getType() )
			{
				default: return;
				case FOOD: goForFood2( context, space, grid ); break;
				case ENEMY_CREATURE: goForWpierdol( context, space, grid ); break;
			}
	
		}
		
	}
	
	/***************************************************/
	/******************* WPIERDOL  *********************/
	/***************************************************/

	private void goForWpierdol(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {
		Enemy enemy = (Enemy) information.getAgent();
		
		if ( enemy == null ) {
			stage = Stages.FINISH;
			return;
		}
		
		// TODO fix function
		int neededBros = (int) Math.ceil(
					( ( enemy.getHealth() * enemy.getDamage() ) / 
					( 
							( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
							Constants.Constants.MOBILE_HEALTH * Constants.Constants.MOBILE_ATTACK 
					) ) * 1.2f
				); 
		
		/*SmartConsole.Print(" danger: " + enemy.getHealth() * enemy.getDamage() + "   " + 
								" mobile danger: " + 
								( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
								Constants.Constants.MOBILE_HEALTH * Constants.Constants.MOBILE_ATTACK 
								+ "         needed bros: " + neededBros , DebugModes.TASK);*/
		
		neededBros = 8; // TODO fix magic number
		
		if ( neededBros > maw.getNumberOfFreeChildren() )
		{
			// TODO: ask other maw.
			return;
		}
		
		/******* CRAETE FORMATION SECTION *********/
		
		List<Mobile> agents = new ArrayList<Mobile>();
		int max = Constants.Constants.BIGGEST_DISTANCE;
		int extent = 5;
		while ( agents.size() < neededBros )
		{
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), neededBros, extent);
			extent += 1;
			
			if(extent > max)
				return;
		}
		
		GridPoint enemyPoint = grid.getLocation( enemy );
		if(enemyPoint == null) {
			stage = Stages.FINISH;
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
		
		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.Wpierdol ); // what's the formation doing?
		f.setGoingPoint( enemyPoint ); // where's the food?
	
		stage = Stages.FINISH;
	}

	
	/***************************************************/
	/********************* FOOD ************************/
	/***************************************************/
	
	private void goForFood(Context<Object> context, 
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


	
	private void goForFood2(Context<Object> context, 
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

	@Override
	public void delayTask() {
		// TODO Auto-generated method stub
		
	}



}
