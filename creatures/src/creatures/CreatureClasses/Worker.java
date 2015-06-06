/**
 * 
 */
package creatures.CreatureClasses;

import creatures.Formation;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import Constants.Constants;

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

	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.CREATURES_MOVE_INTERVAL)
	public void step()
	{
		// Gain Information if in formation.
		
		seekForKnowledge();
		
		if( isInFormation() )
		{
			Formation f = this.getMyFormation();
			if(f == null || f.isDisbanded())
				setInFormation(false);
			return;
		}
		
		// send message
		Attack();
		
		scheduler.updateSchulder();
		
		if(currentTask != null)
			if(!currentTask.isFinished()){
				currentTask.execute();
				return;
			}
		if(this.getMove()) {
			// Arrived at destination
			if ( this.IsAtDestination() ){
				this.ActOnArrival();
				return;
			}
			else if ( this.isGoingSomewhere() ){
				this.MoveThere();
				return;
			} 
			else {
				this.Explore();
				this.MoveCarriedStuff();
				return;
			}
		}
	}
}
