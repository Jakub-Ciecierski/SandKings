/**
 * 
 */
package creatures.CreatureClasses;

import NodalNetwork.*;

import java.util.ArrayList;
import java.util.List;

import map.Food;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.random.RandomHelper;
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
	private nodeNetwork NN;
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	private GridPoint gridpos;
	private List<Worker> children = new ArrayList<Worker>();

	
	public Maw( ContinuousSpace<Object> space, Grid<Object> grid, int setPlayerID, int power )
	{
		NN = new nodeNetwork();
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
		this.power = power;
	}	
	
	public void GiveFood( Food f )
	{
		this.power += f.getPower();
		f.Delete();
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
			children.add(child);
			context.add(child);
			space.moveTo(child, spacePt.getX(), spacePt.getY());
			grid.moveTo(child, gridPt.getX(), gridPt.getY());
			
			numberOfChildren++;
			power--;
			
		}
		else if (power == 0) {
				AddStrengthToChildren();
		}
	}
	
	private void AddStrengthToChildren() {
		if(RandomHelper.nextIntFromTo(0, 100) == 50) {
			//float strength = 0;
			float extra = (float)0.1;
			
			if(playerID == 1) //red one be bigger
				extra = (float)0.5;
			
			if(children.get(0).getStrength() < 300) {
				for(Worker child : children) {
					child.setStrength(child.getStrength() + extra);	
					//strength = child.getStrength();
	
				}
			}
			//System.out.println("Strength[" + playerID + "]: " + strength);
		}		
	}

	/**
	 * @return the gridpos
	 */
	public GridPoint getGridpos() {
		return gridpos;
	}

	/**
	 * @param gridpos the gridpos to set
	 */
	public void setGridpos(GridPoint gridpos) {
		this.gridpos = gridpos;
	}
}
