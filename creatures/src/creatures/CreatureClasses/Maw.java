/**
 * 
 */
package creatures.CreatureClasses;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import Constants.Constants;


/**
 * @author Asmodiel
 *	class for mother
 */
public class Maw {
	private int power;
	
	private int playerID;
	private int numberOfChildren;

	// simulation props
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	
	public Maw( ContinuousSpace<Object> space, Grid<Object> grid, int setPlayerID, int power )
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
		this.power = power;
	}	
	
	
	/**
	 * @return the power
	 */
	@Parameter(displayName = "Power", usageName = "power")
	public int getPower() {
		return power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}
	
	/**
	 * @return the playerID
	 */
	@Parameter(displayName = "Player", usageName = "playerID")
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @param playerID the playerID to set
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * @return the numberOfChildren
	 */
	@Parameter(displayName = "# of kids", usageName = "numberOfChildren")
	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	@ScheduledMethod ( start = Constants.START , interval = Constants.MOBILE_SPAWN_INTERVAL)
	public void step()
	{
		if ( power > 0 )
		{
			Context<Object> context = ContextUtils.getContext(this);
			NdPoint spacePt = space.getLocation(this);
			GridPoint gridPt = grid.getLocation(this);
			Worker child = new Worker( space, grid, playerID );
			
			context.add(child);
			space.moveTo(child, spacePt.getX(), spacePt.getY());
			grid.moveTo(child, gridPt.getX(), gridPt.getY());
			
			numberOfChildren++;
			power--;
		}
	}
	
}
