package creatures.CreatureClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import map.Food;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameter;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import schedules.MobileScheduler;
import util.SmartConsole;
import util.SmartConsole.DebugModes;
import Constants.Constants;
import communication.knowledge.Information;
import communication.knowledge.InformationType;
import communication.knowledge.KnowledgeBase;
import creatures.Agent;
import creatures.Fightable;
import creatures.Formation;
import creatures.CreatureClasses.MawFinder.MawRelation;

/**
 * @author Asmodiel
 *	base class for mobile
 */
public abstract class Mobile extends Fightable {
	
	public Object MOBILE_MUTEX = new Object();
	
	public enum GoingWhere
	{
		Uknown,
		Home,
		ForFood,
		HomeWithFood,
		Explore,
		Wpierdol,
		PickUpFood
	}
	
	// creature properties
	private int food = Constants.MOBILE_STARTING_FOOD;
	
	// formation stuff
	protected boolean isInFormation = false;
		
	// carrying stuff
	private int carryCapacity = Constants.MOBILE_CARRY_CAPACITY;
	public Food carriedStuff;
	
	// stats
	private boolean isStarving = false;
	
	//Moving logic
	public boolean isGoingSomewhere = false;
	public GridPoint goingPoint;
	public GoingWhere goingWhere = GoingWhere.Uknown;
	private boolean move = true;
	
	
	private Formation myFormation;
	
	private KnowledgeBase knowledgeBase = new KnowledgeBase(Constants.MOBILE_MAX_KNOWLEDGE);
	
	protected MobileScheduler scheduler = new MobileScheduler(this);
	
	public Mobile( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID)
	{
		super(space, grid, setPlayerID, Constants.MOBILE_ATTACK, Constants.MOBILE_HEALTH, Constants.MOBILE_MEAT_NO);
	}


	// are we standing on food?
	public List<Food> FoodAtPoint(GridPoint pt)
	{
		List<Food> listOfFood = new ArrayList<Food>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Food) {
				Food food = (Food) obj;
				if(!food.isPicked())
					listOfFood.add( food);
			}
		}
		return listOfFood;
	}
	
	private void StartCarrying( Food food )
	{
		this.carriedStuff = food;
		food.setPicked(true);
	}
	
	public void MoveCarriedStuff()
	{
		if ( carriedStuff != null )
		{
			// get current location in grid
			GridPoint gp = grid.getLocation(this);
			space.moveTo(carriedStuff, gp.getX(), gp.getY());
			grid.moveTo(carriedStuff, gp.getX(), gp.getY());
		}
	}
	
	protected void MoveThere()
	{
		moveTowards( goingPoint );
	}
	
	
	public boolean IsAtDestination()
	{
		if ( goingPoint == null ) return false;
		
		// get current location in grid
		GridPoint currentPos = grid.getLocation(this);
		if ( currentPos == null ) return false;
		
		return ( goingPoint.getX() == currentPos.getX() && goingPoint.getY() == currentPos.getY() );
	}
	
	public void ActOnArrival()
	{
		boolean isNewTask = false;
		switch ( this.goingWhere )
		{
			case Explore:
					// wat?
				break;
			case ForFood:
					AskForFood();
				break;
			case Home:
					// TODO
				break;
			case HomeWithFood:
					//DropCarriedFood();
				break;
			case Wpierdol:
				
				break;
			case PickUpFood:
				//List<Food> foodHere = FoodAtPoint( goingPoint );
				//if ( foodHere.size() > 0 ) PickUpFood( foodHere );
				//isNewTask = true;
				break;
			case Uknown: break;
			default: break;
		}
		if(!isNewTask)
		{
			this.goingPoint = null;
			this.isGoingSomewhere = false;
			this.goingWhere = GoingWhere.Uknown;
		}

	}
	
	private void AskForFood()
	{
		Maw m = MawFinder.Instance().GetMaw( this.playerID );
		
		if(m.hasFood())
		{
			ReceiveFood();
		}
		else
		{
			Starve();
		}
	}

	public void ReceiveFood()
	{
		this.goingPoint = null;
		this.isGoingSomewhere = false;
		this.goingWhere = GoingWhere.Uknown;
		
		// max the food ^^
		this.food = Constants.MOBILE_STARTING_FOOD;
	}

	public void DropCarriedFood( )
	{
		if ( carriedStuff != null )
		{
			Maw m = MawFinder.Instance().GetMaw( this.playerID );
			m.ReceiveFood( carriedStuff );
			this.carriedStuff = null;
		}
	}
	
	public void DropCarriedFoodOnTheGround(){
		if ( carriedStuff != null )
		{
			carriedStuff.setPicked(false);
			this.carriedStuff = null;
		}
	}
	
	public void Starve()
	{
		if(!isStarving) {
			this.food = Constants.MOBILE_STARTING_FOOD;
			isStarving = true;
			
		}
		else {
			Die();
		}
	}
	
	public void GoHome()
	{
		this.setGoingSomewhere(true);
		goingPoint = MawFinder.Instance().GetMawPosition( this.playerID );
	}
	
	public boolean IsAtLocation(GridPoint point)
	{
		if ( point == null ) return true;

		int tolerance = 1;
		GridPoint currentPos = grid.getLocation(this);
		if ( currentPos == null ) return true;
		
		return ( 
			Math.abs( point.getX() - currentPos.getX() ) < tolerance && 	
			Math.abs( point.getY() - currentPos.getY() ) < tolerance 
		);
	}
	
	@SuppressWarnings("unchecked")
	public boolean PickUpFood(List<Food> foodHere) {
		// food with highest power-weight ratio
		Collections.sort( foodHere );
		
		// iterate over foodHere
		for ( Food food : foodHere )
		{
			//if( food.isPickingBlocked(this.playerID) ) 
			if(food.isPicked())
				continue;
			// check if food too heavy
			if ( carriedStuff == null &&  food.getWeight() <= this.carryCapacity )
			{	// lift
				StartCarrying( food );
				return true;
			}
		}
		return false;
	}

	public void Explore()
	{
		if(isFighting)
			return;
		// get current location in grid
		GridPoint gp = grid.getLocation(this);

		// calculate gohome desire
		if ( getGoHomeDesire( gp ) )
		{
			this.goingWhere = GoingWhere.ForFood;
			GoHome();
			return;
		}
		MoveRandomly( gp );
	}
	
	private boolean getGoHomeDesire( GridPoint gp ) {
		Maw mother = MawFinder.Instance().GetMaw(this.playerID);
		if ( mother == null ) return true;
		double distance = MawFinder.Instance().GetDistanceToMaw(this.playerID, gp.getX(), gp.getY());
		if( food < distance + Constants.MOBILE_STARTVATION_THRESHOLD)
			return true;
		else if ( distance < Constants.MOBILE_GO_HOME_THRESHOLD * DangerRelation(mother) )
			return false;
		int random = RandomHelper.nextIntFromTo(0, (int) ( Constants.BIGGEST_DISTANCE - Constants.MOBILE_GO_HOME_THRESHOLD ));
		if ( (distance - Constants.MOBILE_GO_HOME_THRESHOLD * DangerRelation(mother))/4 > random )
			return true;
		
		return false;
	}
	
	private float DangerRelation(Maw maw){
		if ( maw == null ) return 0;		return (MawFinder.Instance().getBiggestDanger() / maw.getDanger()) * Constants.MAW_DISTANCE_FACTOR;
	}

	private void MoveRandomly( GridPoint gp )
	{
		// get random next position
		int randX = ( RandomHelper.nextIntFromTo(-1, 1) );
		int randY = ( RandomHelper.nextIntFromTo(-1, 1) );

		randX += gp.getX(); randY += gp.getY();
		
		// catch out of bounds
		GridDimensions spaceDim = grid.getDimensions();
		
		// X too big
		if ( randX >= spaceDim.getWidth() ) randX = spaceDim.getWidth() - 1;
		// Y too big
		if ( randY >= spaceDim.getHeight() ) randY = spaceDim.getHeight() - 1;
		
		// X too small
		if ( randX < 1 ) randX = 1;
		// Y too small
		if ( randY < 1 ) randY = 1;
		
		GridPoint randomGP = new GridPoint( randX, randY );
		this.moveTowards(randomGP);
	}

	public void moveTowards( GridPoint gp )
	{
		moveTowards( gp, false );
		MoveCarriedStuff();
	}
	public void moveTowards( GridPoint gp, boolean isMoveAway )
	{
		
		// only move if not already there
		if ( !IsAtLocation( gp ) )
		{
			NdPoint thisLocation = space.getLocation(this);
			NdPoint goalLocation = new NdPoint ( gp.getX (), gp.getY ());
				
			if ( goalLocation == null ) System.out.println("goal null ");
			if ( thisLocation == null ) System.out.println("this null xD");

			double angle = SpatialMath.calcAngleFor2DMovement( space, thisLocation, goalLocation );
			if ( isMoveAway) 
				space.moveByVector(this, -1, angle, 0);
			else 
				space.moveByVector(this, 1, angle, 0);
			thisLocation = space.getLocation(this);	
			// WARNING: without Math.round this gets cut and has a converging behavior when running randomly around
			grid.moveTo(this, (int)Math.round(thisLocation.getX()), (int)Math.round(thisLocation.getY()) );
			
			food--;
		}
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
	 * @return the strength (of Maw)
	 */
	@Parameter(displayName = "strength", usageName = "strength")
	public float getStrength() {
		return MawFinder.Instance().GetMawStrength(this.playerID); 
	}

	/**
	 * @return the carryCapacity
	 */
	@Parameter(displayName = "carry capacity", usageName = "carry capacity")
	public int getCarryCapacity() {
		return carryCapacity;
	}
	/**
	 * @param carryCapacity the carryCapacity to set
	 */
	public void setCarryCapacity(int carryCapacity) {
		this.carryCapacity = carryCapacity;
	}
	

	/**
	 * @return the isGoingSomewhere
	 */
	public boolean isGoingSomewhere() {
		return isGoingSomewhere;
	}

	/**
	 * @param isGoingSomewhere the isGoingSomewhere to set
	 */
	public void setGoingSomewhere(boolean isGoingSomewhere) {
		this.isGoingSomewhere = isGoingSomewhere;
	}

	/**
	 * Return agents in given neighborhood
	 * @param extentX
	 * @param extentY
	 */
	public List<Agent> getAgentsInVicinity(int extentX, int extentY){
		List<Agent> vicinity = new ArrayList<Agent>();
		
		// get the grid location of this Human
		GridPoint pt = grid.getLocation(this);
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh <Agent> nghCreator = new GridCellNgh <Agent>(grid , pt,
		Agent.class , extentX , extentY);
		
		List <GridCell<Agent>> gridCells = nghCreator.getNeighborhood(true);
		
		for ( GridCell <Agent> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Agent || 
						(obj instanceof Mobile && (Mobile)obj != this)){
					Agent agent = (Agent)obj;
					vicinity.add(agent);
				}	
			}
		}
		return vicinity;
	}

	public List<Food> getFoodInVicinity(int extentX, int extentY){
		List<Food> vicinity = new ArrayList<Food>();
		
		// get the grid location of this Human
		GridPoint pt = grid.getLocation(this);
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh <Food> nghCreator = new GridCellNgh <Food>(grid , pt,
				Food.class , extentX , extentY);
		
		List <GridCell<Food>> gridCells = nghCreator.getNeighborhood(true);
		
		for ( GridCell <Food> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Food){
					Food food = (Food)obj;
					if ( !food.isPicked() )
						vicinity.add(food);
				}	
			}
		}
		return vicinity;
	}
	
	/**
	 * Seeks for knowledge, adds interesting points in the map
	 * and saves it in mobile's knowledge base
	 */
	public void seekForKnowledge(){
		// get all agents in mobiles vicinity
		List<Agent> vicinity = getAgentsInVicinity(Constants.MOBILE_VICINITY_X, Constants.MOBILE_VICINITY_Y);
		
		for(int i = 0; i < vicinity.size(); i++){
		
			Agent agent = vicinity.get(i);

			// Do not learn about picked up food
			if(agent instanceof Food){
				Food food = (Food) agent;
				
				if( food.isPickingBlocked(this.playerID) ) //if(food.isPicked())
					continue;
			}
			// Do not learn about alliance formation
			if(agent instanceof Formation){
				Formation f = (Formation)agent;
				if(MawFinder.Instance().areWeFriends(this.playerID, f.getPlayerID())){
					continue;
				}
			}
				
			InformationType infoType = KnowledgeBase.GetInfoType(agent);

			// if info is interesting add it
			if(infoType != InformationType.GARBAGE){

				GridPoint pt = grid.getLocation(agent);

				double tickCount = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
				
				Information info = new Information(agent, infoType, tickCount, pt);
				
				if(this.knowledgeBase.addInformation(info)){

					SmartConsole.Print("Agent #" + this.id +" Gained knowledge", DebugModes.KB);
					SmartConsole.Print("What: " + infoType.toString(), DebugModes.KB);
					SmartConsole.Print("Where: [" + pt.getX() + ", " + pt.getY() +"] ", DebugModes.KB);
					SmartConsole.Print("When: " + tickCount, DebugModes.KB);
				}
			}
		}
	
	}

	/**
	 * @return the isInFormation
	 */
	public boolean isInFormation() {
		return isInFormation;
	}


	/**
	 * @param isInFormation the isInFormation to set
	 */
	public void setInFormation(boolean isInFormation) {
		this.isInFormation = isInFormation;
	}
	public boolean getMove() {
		return move;
	}


	public void setMove(boolean move) {
		this.move = move;
	}
		
	public GridPoint getGoingPoint() {
		return goingPoint;
	}

	public void setGoingPoint(GridPoint goingPoint) {
		this.goingPoint = goingPoint;
	}


	public Formation getMyFormation() {
		return myFormation;
	}


	public void setMyFormation(Formation myFormation) {
		this.myFormation = myFormation;
	}
	
	public KnowledgeBase getKnowledgeBase(){
		return this.knowledgeBase;
	}
}
