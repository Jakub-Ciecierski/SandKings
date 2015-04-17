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


/**
 * @author Viet Ba
 * Class for God
 */
public class God {

	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	
	public God (ContinuousSpace<Object> space, Grid<Object> grid) { 
		this.space = space;
		this.grid = grid;
	}
	
	/*
	 * We can add everything God will be doing here - dropping food, creatures etc.
	 * */
	@ScheduledMethod ( start = Constants.GOD_MODE_START , interval = Constants.FOOD_DROP_INTERVAL)
	public void step()
	{
		DropFood();
	}
	
	private void DropFood() {		
		int rand = RandomHelper.nextIntFromTo(0, 5);
		if (rand == 0) //20% chance of food drop
		{ 
			Context<Object> context = ContextUtils.getContext(this);
			Food food = new Food( space, grid, 0);
			int x = RandomHelper.nextIntFromTo(2, Constants.GRID_SIZE - 2);
			int y = RandomHelper.nextIntFromTo(2, Constants.GRID_SIZE - 2);
			Object temp = grid.getObjectAt(x, y);
			
			if(!(temp instanceof Food)) //don't add food where food already is
			{ 
				context.add(food);
				space.moveTo(food, x, y);
				grid.moveTo(food, x, y);
			}
		}			
	}
}
