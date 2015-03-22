/**
 * 
 */
package creatures.CreatureClasses;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Asmodiel
 *
 */
public class Worker extends Mobile {

	public Worker(ContinuousSpace<Object> space, Grid<Object> grid,
			int setPlayerID) {
		super(space, grid, setPlayerID);
		// TODO Auto-generated constructor stub
	}

	@ScheduledMethod ( start = 1 , interval = 1)
	public void step()
	{
		this.randomMove();
	}
	
}
