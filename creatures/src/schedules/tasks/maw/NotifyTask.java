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

public class NotifyTask extends Task {

	private Maw maw;
	
	private int stage = 0;
	
	public NotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
		

		System.out.println("*********************************************************");
		System.out.println("Agent #" + maw.getID() +" New NotifyTask: " + information.getType().toString());
		System.out.println("********************************************************* \n\n");
	
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		if ( stage == 1 ) 
		{
			isFinished = true;
			information.isUsefull = false;
			return;
		} else if ( stage == 0 )
		{
			// SPAWN FORMATION FOR THE JOB
			
			Context<Object> context = ContextUtils.getContext(maw);
			ContinuousSpace<Object> space = maw.getSpace(); 
			Grid<Object> grid = maw.getGrid();
			
			
			switch ( information.getType() )
			{
				default: return;
				case FOOD: goForFood( context, space, grid ); break;
				case ENEMY_CREATURE: goForWpierdol(context, space, grid); break;
			}
			
			
		}
		
	}

	private void goForWpierdol(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {
		// TODO Auto-generated method stub
		Enemy enemy = (Enemy) information.getAgent();
		if ( enemy == null )return;
		int neededBros = (int) Math.ceil(
					( ( enemy.getHealth() * enemy.getDamage() ) / 
					( 
							maw.getStrength() * maw.getStrength() * 
							Constants.Constants.MOBILE_HEALTH * Constants.Constants.MOBILE_HEALTH 
					) ) * 1.2f
				);   
		System.out.println( "needed bros: " + neededBros );
		neededBros = 8;
		if ( neededBros > maw.getNumberOfFreeChildren() )
		{
			// TODO: ask other maw.
			return;
		}
		
		List<Mobile> agents = new ArrayList<Mobile>();
		int extent = 5;
		while ( agents.size() < neededBros )
		{
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), neededBros, extent);
			extent += 1;
			System.out.println( "maw [" + agents.size() + "/" + neededBros + "]    c4attack bros in " + extent );
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
		System.out.println( "maw [" + agents.size() + "/" + neededBros + "]    added bros " + agents.size() + " to f #" + f.getID() );
		
		gridPt = grid.getLocation( enemy );
		//gridPt = information.getGridPoint();
		
		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.Wpierdol ); // what's the formation doing?
		f.setGoingPoint( gridPt ); // where's the food?
		
		
		System.out.println("atck formation " + f.getID() + " created at " + gridPt.getX() + ":" + gridPt.getY() + " for " + f.getNeededSize() + "." );
		stage = 1;
	}

	private void goForFood(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {
		// TODO Auto-generated method stub
		Food food = (Food) information.getAgent();
		if ( food == null ) return;
		int neededBros = food.getWeight();// + 3;
		
		if ( neededBros > maw.getNumberOfFreeChildren() ) 
		{
			return;
		}
		
		
		
		List<Mobile> agents = new ArrayList<Mobile>();
		int extent = 5;
		while ( agents.size() < neededBros )
		{
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), neededBros, extent);
			extent += 1;
			System.out.println( "maw [" + agents.size() + "/" + neededBros + "]    c4food bros in " + extent );
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
		System.out.println( "maw [" + agents.size() + "/" + neededBros + "]    added bros " + agents.size() + " to f #" + f.getID() );
		
		gridPt = grid.getLocation(food);
		//gridPt = information.getGridPoint();
		
		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.ForFood ); // what's the formation doing?
		f.setGoingPoint( gridPt ); // where's the food?
		
		
		System.out.println("food formation " + f.getID() + " created at " + gridPt.getX() + ":" + gridPt.getY() + " for " + f.getNeededSize() + "." );
		stage = 1;
	}




}
