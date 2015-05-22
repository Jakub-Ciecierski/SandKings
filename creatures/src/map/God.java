/**
 * 
 */
package map;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import Constants.Constants;
import Enemies.Enemy;


/**
 * @author Viet Ba
 * Class for God
 */
public class God {

	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	private int enemyCount = 0;
	
	public God (ContinuousSpace<Object> space, Grid<Object> grid) { 
		this.space = space;
		this.grid = grid;
	}
	
	/*
	 * We can add everything God will be doing here - dropping food, creatures etc.
	 * */
	@ScheduledMethod ( start = Constants.GOD_MODE_START , interval = Constants.GOD_MODE_INTERVAL)
	public void step()
	{
		DropFood();
		if(enemyCount <= Constants.MAX_NUMBER_OF_ENEMIES)
			DropEnemy();
	}
	
	private void DropEnemy() {		
		int rand = RandomHelper.nextIntFromTo(0, 9);
		if (rand == 0) //10% chance of food drop
		{ 
			@SuppressWarnings("unchecked")
			Context<Object> context = ContextUtils.getContext(this);
			int enemyID = RandomHelper.nextIntFromTo( 0, 2 );
			Enemy enemy = new Enemy( space, grid, enemyID );
			int x = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			int y = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			Object temp = grid.getObjectAt( x, y );
			
			if(!(temp instanceof Enemy)) //don't add enemy where food already is
			{ 
				context.add( enemy );
				space.moveTo( enemy, x, y );
				grid.moveTo( enemy, x, y );
				enemyCount++;
			}
		}			
	}
	
	private void DropFood() {		
		int rand = RandomHelper.nextIntFromTo( 0, 2 );
		if (rand == 0) //33% chance of food drop
		{ 
			int foodID = 3;
			if(RandomHelper.nextIntFromTo( 0, 10 ) == 5)
				foodID = 0;
			else
				foodID = RandomHelper.nextIntFromTo( 1, 3 );
			@SuppressWarnings("unchecked")
			Context<Object> context = ContextUtils.getContext( this );
			Food food = new Food( space, grid, foodID );
			int x = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			int y = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			Object temp = grid.getObjectAt( x, y );
			
			if(!(temp instanceof Food)) //don't add food where food already is
			{ 
				context.add( food );
				space.moveTo( food, x, y );
				grid.moveTo( food, x, y );
			}
		}			
	}
}
