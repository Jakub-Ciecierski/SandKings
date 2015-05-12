/**
 * 
 */
package creatures.CreatureClasses;

import NodalNetwork.*;

import java.util.ArrayList;
import java.util.List;

import creatures.Agent;
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
public class Maw extends Agent {
	private int food;
	private int power;
	private int maxNumOfChildren;
	private int playerID;
	private int numberOfChildren;
	private int numOfLostChildren;
	private String name;

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
		this.food = power;
		this.maxNumOfChildren = power;
		
		switch ( this.playerID )
		{
			case 1:
				this.name = "Red";
				break;
			case 2:
				this.name = "Blue";
				break;
			case 3:
				this.name = "White";
				break;
			case 4:
				this.name = "Black";
				break;
			default:
				this.name = "Uknown";
				
		}
	}	
	
	public void LostAMobile()
	{
		this.numberOfChildren--;
		this.numOfLostChildren++;
		if(this.numberOfChildren != 0)
			this.setPower(this.power - (this.power / this.numberOfChildren));
	}
			
	public void ReceiveFood( Food f )
	{
		this.setFood( this.food + f.getPower() );
		this.setPower( this.power + f.getPower() );
		this.AddStrengthToChildren(f.getPower());
		//add strength to children
		f.Delete();
	}
	
	public boolean hasFood()
	{
		if ( NN.getElementDesire("food") + Constants.MAW_FOOD_DESIRE_THRESHOLD < this.getFood()  )
			return true;
		else	
			return false;
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
		this.maxNumOfChildren = this.numberOfChildren + ( power/Constants.CHILDREN_PER_POWER );
	}
	
	/**
	 * @return the playerID
	 */
	@Parameter(displayName = "Player", usageName = "Maw Name")
	public String getMawName()
	{
		return this.name;
	}
	
	
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

	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.MOBILE_SPAWN_INTERVAL)
	public void step()
	{
		TrySpawnMobile();
	}
	
	private void TrySpawnMobile()
	{
		if ( numberOfChildren < this.maxNumOfChildren )
		{	
			Context<Object> context = ContextUtils.getContext(this);

			Worker child = new Worker( space, grid, playerID );
				child.setSize(this.getPower()/Constants.MOBILE_SIZE_MULTIPLIER);
				children.add(child);
				context.add(child);
			
			NdPoint spacePt = space.getLocation(this);
			GridPoint gridPt = grid.getLocation(this);
			space.moveTo(child, spacePt.getX(), spacePt.getY());
			grid.moveTo(child, gridPt.getX(), gridPt.getY());
			
			numberOfChildren++;		
			food -= Constants.FOOD_PER_SPAWN;
			NN.incrementDesire("food");
		}
	}
	

	private void AddStrengthToChildren(float extra) {			
			if(children.get(0).getStrength() < 300) {
				for(Worker child : children) {
					child.setStrength(child.getStrength() + extra);	
					child.setSize(this.getPower()/Constants.MOBILE_SIZE_MULTIPLIER);
				}
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

	public int getMaxNumOfChildren() {
		return maxNumOfChildren;
	}

	public int getNumOfLostChildren() {
		return numOfLostChildren;
	}

	/**
	 * @return the food
	 */
	public int getFood() {
		return food;
	}

	/**
	 * @param food the food to set
	 */
	public void setFood(int food) {
		this.food = food;
	}

}
