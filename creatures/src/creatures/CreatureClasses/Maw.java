package creatures.CreatureClasses;

import NodalNetwork.*;

import java.util.ArrayList;
import java.util.List;

import communication.knowledge.Information;
import communication.knowledge.KnowledgeBase;
import creatures.Agent;
import creatures.Fightable;
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
import schedules.MawScheduler;
import Constants.Constants;


/**
 * @author Asmodiel
 *	class for mother
 */
public class Maw extends Fightable {
	private float food;
	private int playerID;
	private int numberOfChildren;
	private int numOfLostChildren;
	private int childrenBornCount = 200;
	private int strengthCount = 0;
	private float strength;
	private String name;

	// simulation props
	private nodeNetwork NN;
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	private GridPoint gridpos;
	private List<Worker> children = new ArrayList<Worker>();

	private KnowledgeBase knowledgeBase = new KnowledgeBase(Constants.MAW_MAX_KNOWLEDGE);
	private MawScheduler scheduler = new MawScheduler(this);

	public Maw( ContinuousSpace<Object> space, Grid<Object> grid, int setPlayerID)
	{
		super(space, grid, setPlayerID, Constants.MAW_ATTACK, Constants.MAW_HEALTH, Constants.MAW_MEAT_NO);
		NN = new nodeNetwork();
		
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
		this.food = Constants.MAW_START_FOOD;
		this.strength  = Constants.MAW_START_STRENGTH;
		
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
	}
			
	public void ReceiveFood( Food f )
	{
		this.setFood( this.food + f.getPower() );
		//add strength to children
		f.Delete();
	}
	
	public boolean hasFood()
	{
		if ( NN.getElementDesire("food") + Constants.MAW_FOOD_DESIRE_THRESHOLD < this.getFood()  )
			return true;

		if ( this.getFood() >= Constants.MOBILE_STOMACH_SIZE * (1 + this.strength))
			{
				this.food -=  Constants.MOBILE_STOMACH_SIZE * (1 + this.strength);
				return true;
			}
		else	
			return false;
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
		TryIncrementStrength();
		
		if(currentTask != null)
			if(!currentTask.isFinished())
				currentTask.execute();
		
		scheduler.updateSchulder();
	}
	
	private void TrySpawnMobile()
	{
		if ( Constants.MAW_BIRTHING_FACTOR * numberOfChildren * Constants.MOBILE_STOMACH_SIZE * (1 + this.strength) < this.food  
			 && childrenBornCount > Constants.MAW_CHILDPOOP_COUNTER)
		{	
			Context<Object> context = ContextUtils.getContext(this);

			Worker child = new Worker( space, grid, playerID );
				children.add(child);
				context.add(child);
			
			NdPoint spacePt = space.getLocation(this);
			GridPoint gridPt = grid.getLocation(this);
			space.moveTo(child, spacePt.getX(), spacePt.getY());
			grid.moveTo(child, gridPt.getX(), gridPt.getY());
			
			numberOfChildren++;		
			food -= Constants.FOOD_PER_SPAWN;
			NN.incrementDesire("food");
			childrenBornCount = 0;
		}
		childrenBornCount ++;
	}
	
	private void TryIncrementStrength()
	{
		if ( Constants.MAW_STRENGTH_FACTOR * numberOfChildren * Constants.MOBILE_STOMACH_SIZE * (1 + this.strength) < this.food  
			&& strengthCount > Constants.MAW_STRENGTH_COUNTER) {	
				this.strength += 0.1;
				strengthCount = 0;
			}
		else if (Constants.MAW_STARVING_FACTOR * numberOfChildren * Constants.MOBILE_STOMACH_SIZE * (1 + this.strength) >= this.food  
				&& strengthCount > Constants.MAW_STRENGTH_COUNTER && this.strength > 0) {
				this.strength -= 0.2;
				strengthCount = 0;
			}
			strengthCount++;
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

	public List<Worker> getChildren() {
		return children;
	}

	public void setChildren(List<Worker> children) {
		this.children = children;
	}

	public int getNumOfLostChildren() {
		return numOfLostChildren;
	}

	/**
	 * @return the food
	 */
	public float getFood() {
		return food;
	}

	/**
	 * @param food the food to set
	 */
	public void setFood(float food) {
		this.food = food;
	}

	public float getStrength() {
		return strength;
	}

	public void setStrength(float strength) {
		this.strength = strength;
	}

	
	public KnowledgeBase getKnowledgeBase(){
		return this.knowledgeBase;
	}
}
