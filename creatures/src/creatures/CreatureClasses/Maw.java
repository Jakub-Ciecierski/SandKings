package creatures.CreatureClasses;

import NodalNetwork.*;

import java.util.ArrayList;
import java.util.List;

import communication.knowledge.KnowledgeBase;
import creatures.Fightable;
import map.Food;
import map.God;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import schedules.MawScheduler;
import schedules.Scheduler;
import map.EventType;
import util.GSC;import util.SmartConsole;
import util.SmartConsole.DebugModes;import Constants.Constants;


/**
 * @author Asmodiel
 *	class for mother
 */
public class Maw extends Fightable {
	private float food;
	private float eatenFood = Constants.MOBILE_STARTING_FOOD;
	
	private int playerID;
	private int numberOfChildren;
	private int numOfLostChildren;
	private int maxNumOfChildren; //max number of children AT A TIME
	private int childrenBornCount = 200;
	private int strengthCount = 0;
	private float strength;
	private String name;
	private int aggresion = 0;

	// simulation props
	private nodeNetwork NN;
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	private GridPoint gridpos;
	private List<Worker> children = new ArrayList<Worker>();

	private KnowledgeBase knowledgeBase = new KnowledgeBase(Constants.MAW_MAX_KNOWLEDGE);
	private MawScheduler scheduler = new MawScheduler(this);
	
	private int pizza = 0, donut = 0, grape = 0, cabbage = 0, meat = 0, steak = 0; //how many she have eaten of each
	public double timeOfDeath = 0;
	
	//private int pendingFormations = 0;
	
	private List<FormationCreator> pendingFormations = new ArrayList<FormationCreator>();
	
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
		updateDanger();
		updateProfit();	
	}	
	

	private void tryEat(){
		if(eatenFood > 0)
		{
			eatenFood -= Constants.MAW_EATEN_FOOD_DECREASE_VALUE;
		}
		else
		{
			if(food > Constants.MAW_FOOD_DECREASE_VALUE)
			{
				eatenFood += Constants.MAW_FOOD_DECREASE_VALUE;// Constants.MOBILE_STARTING_FOOD;
				food -=  Constants.MAW_FOOD_DECREASE_VALUE;
			}
			else
			{
				dealDamage( Constants.MAW_DAMAGE_DECREASE_VALUE, 0 );
				
				GSC.Instance().AddEventInfo(EventType.Starvation, Constants.STARVATION_TIMEOUT, 
						new GridPoint(this.getGridpos().getX() + Constants.EVENT_DISTANCE, this.getGridpos().getY() + Constants.EVENT_DISTANCE));	
			}
		}
	}
	
	public void updateDanger(){
		danger = numberOfChildren * strength * Constants.MOBILE_ATTACK * Constants.MOBILE_HEALTH + 1;
	}
	public void updateProfit(){
		profit = numberOfChildren * Constants.MEAT_CALORIES + Constants.MAW_MEAT_NO * Constants.STEAK_CALORIES + food;
	}
	public float getRatio()
	{
		return profit/danger;
	}
	
	public void LostAMobile( Mobile m )
	{
		this.numberOfChildren--;
		this.numOfLostChildren++;
		this.children.remove(m);
	}
			
	public void ReceiveFood( Food f )
	{
		this.setFood( this.food + f.getPower() );
		switch(f.getFoodID()) {
			case 0:
				pizza++;
				break;
			case 1:
				donut++;
				break;
			case 2:
				grape++;
				break;
			case 3:
				cabbage++;
				break;
			case 4:
				meat++;
				break;
			case 5:
				steak++;
				break;
			default:
				break;	
		}
		//add strength to children
		f.Delete();
	}

    public void DropMawFood(){
		@SuppressWarnings("unchecked")
		Context<Object> context = ContextUtils.getContext( this );

		int x = grid.getLocation ( this ).getX();
		int y = grid.getLocation ( this ).getY();
		
		for( int i = 0; i < food/Constants.STEAK_CALORIES; i++)
			dropMeat(5, x, y, context);
    }
	
	public boolean hasFood()
	{
		if ( this.getFood() >= Constants.MOBILE_STOMACH_SIZE * (1 + this.strength))
		{
			this.food -=  Constants.MOBILE_STOMACH_SIZE * (1 + this.strength);
			return true;
		}
		else
		{
			aggresion++;
			return false;
		}
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
		int kids = 0;
		for ( Mobile m : children )
		{
			if ( m.getHealth() > 0 ) kids++;
		}
		this.numberOfChildren = kids;
		return kids;
	}

	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.MOBILE_SPAWN_INTERVAL)
	public void step()
	{
		if ( this.getNumberOfChildren() == 0 && this.getFood() < Constants.FOOD_PER_SPAWN ){ this.Die(); return; }
		
		TrySpawnMobile();
		TryIncrementStrength();
		tryEat();
		
		if(currentTask != null)
			if(!currentTask.isFinished())
				currentTask.execute();
		
		scheduler.updateSchulder();
		

		updateDanger();
		updateProfit();
		
		createFormation();
		UpdateMaxNumOfChildren();
		
	}
	
	private void UpdateMaxNumOfChildren()
	{
		if(numberOfChildren > maxNumOfChildren)
			maxNumOfChildren = numberOfChildren;
	}
	private void TrySpawnMobile()
	{
		if ( Constants.MAW_BIRTHING_FACTOR * numberOfChildren * Constants.MOBILE_STOMACH_SIZE * (1 + this.strength) < this.food  
			 && childrenBornCount > Constants.MAW_CHILDPOOP_COUNTER && this.food > Constants.FOOD_PER_SPAWN)
		{	
			@SuppressWarnings("unchecked")
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

	public int getAggresion() {
		return aggresion;
	}

	public void setAggresion(int aggresion) {
		this.aggresion = aggresion;
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

	public float getEatenFood() {
		return eatenFood;
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
		for( Mobile mobile : children){
			float constant = 1 + getStrength();	
			mobile.setDamage(Constants.MOBILE_ATTACK * constant);
			mobile.setHealth(Constants.MOBILE_HEALTH * constant);
		}
	}

	public int getMaxNumOftChildren() {
		return maxNumOfChildren;
	}
	
	public KnowledgeBase getKnowledgeBase(){
		return this.knowledgeBase;
	}

	/**
	 * @return the space
	 */
	public ContinuousSpace<Object> getSpace() {
		return space;
	}

	/**
	 * @param space the space to set
	 */
	public void setSpace(ContinuousSpace<Object> space) {
		this.space = space;
	}

	/**
	 * @return the grid
	 */
	public Grid<Object> getGrid() {
		return grid;
	}

	/**
	 * @param grid the grid to set
	 */
	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}
	
	public int getPizza() {
		return this.pizza;
	}
	
	public int getDonut() {
		return this.donut;
	}
	
	public int getGrape() {
		return this.grape;
	}
	
	public int getCabbage() {
		return this.cabbage;
	}
	
	public int getMeat() {
		return this.meat;
	}
	
	public int getSteak() {
		return this.steak;
	}
	
	public MawScheduler getScheduler(){
		return this.scheduler;
	}

	public int getNumberOfFreeChildren() {
		// TODO Auto-generated method stub
		int free = 0;
		for ( Mobile m : children )
		{
			if ( !m.isInFormation() && !m.isGoingSomewhere() &&
					(m.getCurrentTask() == null ||
					m.getCurrentTask() != null &&
					m.getCurrentTask().isFinished()))
			{
				free++;
			}
		}
		return free;
	}
	
	public void addPendingFormation(FormationCreator formationCreator){
		pendingFormations.add(formationCreator);
	}
	
	public boolean createFormation(){
		if(pendingFormations.size() > 0){
			SmartConsole.Print("Maw #" + this.getPlayerID() + "FromationCreator Size:" + pendingFormations.size(), DebugModes.CR_FORMATION);
			FormationCreator pendingFormation = pendingFormations.get(0);
			//SmartConsole.Print("Maw #" + this.getPlayerID() + "Attempting FromationCreator", DebugModes.FORMATION);
			
			if(pendingFormation.AttemptFormation() || pendingFormation.toRemove){
				pendingFormations.remove(0);
				return true;
			}
		}
		return false;
	}
}
