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

public class EnemyNotifyTask extends Task {

	private Maw maw;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		FINISH,
		ALLIANCE
	}
	
	private int currentAskMawID;
	private boolean doAlliance;
	
	public EnemyNotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
		
		SmartConsole.Print("Agent #" + maw.getID() +" New EnemyNotifyTask: " + information.getType().toString(), DebugModes.TASK);
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
			
			goForWpierdol( context, space, grid );
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
		
		System.out.println( " danger: " + enemy.getHealth() * enemy.getDamage() + "   " + 
				" mobile danger: " + 
					( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
						Constants.Constants.MOBILE_HEALTH * Constants.Constants.MOBILE_ATTACK 
				+ "         needed bros: " + neededBros );
		
		neededBros = 8; // TODO fix magic number
		
		if ( neededBros > maw.getNumberOfFreeChildren() )
		{
			askForAlliance();
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
			SmartConsole.Print("maw [" + agents.size() + "/" + neededBros + "]    c4attack bros in " + extent, DebugModes.BASIC);
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
		SmartConsole.Print("maw [" + agents.size() + "/" + neededBros + "]    added bros " + agents.size() + " to f #" + f.getID(), DebugModes.BASIC);
		
		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.Wpierdol ); // what's the formation doing?
		f.setGoingPoint( enemyPoint ); // where's the food?

		SmartConsole.Print("atck formation " + f.getID() + " created at " + gridPt.getX() + ":" + gridPt.getY() + " for " + f.getNeededSize() + ".", DebugModes.BASIC);
		
		stage = Stages.FINISH;
	}

	private boolean askForAlliance(){
		
		
		
		return false;
	}
}
