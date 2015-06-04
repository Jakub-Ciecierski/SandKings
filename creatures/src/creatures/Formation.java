package creatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import communication.messages.DamageMessage;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import util.SmartConsole;
import util.SmartConsole.DebugModes;
import map.Food;
import Constants.Constants;
import creatures.CreatureClasses.*;
import creatures.CreatureClasses.Mobile.GoingWhere;

public class Formation extends Fightable {
	
	public Formation(ContinuousSpace<Object> space, Grid<Object> grid,
			int playerID) {
		super(space, grid, 5);
		if ( grid == null ) SmartConsole.Print("formation grid null", DebugModes.FORMATION);
		if ( space == null ) SmartConsole.Print("formation space null", DebugModes.FORMATION);
		this.playerID = playerID;
		// TODO Auto-generated constructor stub
	}

	public enum GoingWhere
	{
		Uknown,
		Home,
		ForFood,
		HomeWithFood,
		Explore,
		Wpierdol
	}
	//private List<Integer> owners = new ArrayList<Integer>();
	
	private boolean isComplete = false;
	private int playerID = 0;
	private int neededSize = 0;
	//private int carryCapacity = 0;
	//private float size = Constants.CREATURES_SIZE;
	
	//Moving logic
	private boolean isGoingSomewhere = false;
	private GridPoint goingPoint;

	private GoingWhere goingWhere = GoingWhere.Uknown;	
	
	List<Mobile> soldiers = new ArrayList<Mobile>();
	List<Mobile> pendingSoldiers = new ArrayList<Mobile>();
	
	// carrying stuff
	private int carryCapacity = 0;
	private int carriedWeight = 0;
	private List<Food> carriedStuff = new ArrayList<Food>();	
	
	// only called when we need a new member
	public void findNewMember(int ID)
	{
		if ( grid == null ) SmartConsole.Print("find member: formation grid null", DebugModes.FORMATION);
		// get the grid location of this Human
		GridPoint pt = grid.getLocation ( this );
		// use the GridCellNgh class to create GridCells for the surrounding neighborhood
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid, pt, Mobile.class, 5, 5 );
		
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if( obj instanceof Mobile ){
					
					Mobile mobile = (Mobile) obj;
					if( 
							mobile.getPlayerID() == ID && 
							!mobile.isInFormation() &&
							!mobile.isGoingSomewhere() && 
							this.getSize() < this.getNeededSize()
						)
					{
						this.addToFormation( mobile );
					}
				}
			}
		}
	}
	public void addToFormation( Mobile m )
	{
		SmartConsole.Print("found new pending member", DebugModes.FORMATION);
		GridPoint currentPos = grid.getLocation(this);
		m.setInFormation(true);
		m.setGoingSomewhere(false);
		//m.setGoingPoint( currentPos );
		m.setMyFormation(this);
		
		this.pendingSoldiers.add(m);
	}
	
	public boolean addPending()
	{
		GridPoint currentPos = grid.getLocation(this);
		
		List<Mobile> newPendingSoldiers = new ArrayList<Mobile>();
		for( int i = 0; i < pendingSoldiers.size(); i++)
		{
			
			if(!pendingSoldiers.get(i).IsAtLocation(currentPos))
			{
				pendingSoldiers.get(i).moveTowards(currentPos);
				
				newPendingSoldiers.add(pendingSoldiers.get(i));
			}
			else
			{
				this.setCarryCapacity(this.getCarryCapacity() + pendingSoldiers.get(i).getCarryCapacity());
				soldiers.add(pendingSoldiers.get(i));
			}
		}
		
		pendingSoldiers = newPendingSoldiers;
		
		if(pendingSoldiers.size() == 0)
			return true;
		return false;
	}
	

	public void kickOut( Mobile m )
	{
		m.setInFormation(false);
		m.setGoingSomewhere(false);
		m.setMove(true); // so that he starts moving
		m.setGoingPoint(null);
		m.setMyFormation(null);
		this.setCarryCapacity(this.getCarryCapacity() - m.getCarryCapacity());
		//this.soldiers.remove(m);
	}
	
	public int getSize()
	{
		return soldiers.size();// + pendingSoldiers.size();
	}
	
	public boolean IsAtDestination()
	{
		if ( goingPoint == null ) return false;
		
		// get current location in grid
		GridPoint currentPos = grid.getLocation(this);
		if ( currentPos == null ) return false;
		
		return ( goingPoint.getX() == currentPos.getX() && goingPoint.getY() == currentPos.getY() );
	}	
	// are we standing on food?
	public List<Food> FoodAtPoint(GridPoint pt)
	{
		List<Food> food = new ArrayList<Food>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Food) {
				if ( !((Food) obj).isPicked() )
					food.add( (Food) obj);
			}
		}
		return food;
	}
	
	public void MoveCarriedStuff()
	{
		GridPoint gp = grid.getLocation(this);
		@SuppressWarnings("unchecked")
		Context<Object> context = ContextUtils.getContext( this );
		for ( Food f : carriedStuff )
		{
			if ( context.contains(f) )
			{
				space.moveTo(f, gp.getX(), gp.getY());
				grid.moveTo( f, gp.getX(), gp.getY());
			}
		}
	}	
	public void StartCarrying( Food food )
	{
		this.carriedStuff.add(food);
		food.setPicked(true);
		setCarriedWeight( getCarriedWeight() + food.getWeight() );
		SmartConsole.Print("Formation " + getID() + " picked up food.", DebugModes.FORMATION);
				
		//this.IsAtDestination(false);
		this.setGoingSomewhere(true);
		this.goingWhere = GoingWhere.HomeWithFood;
		this.goingPoint = MawFinder.Instance().GetMawPosition(this.playerID);		
	}	
	public void Attack( /* Fightable f */ )
	{
		for( Mobile m : soldiers)
		{
			m.Attack();
		}
	}
	public void Disband()
	{
		if ( soldiers.size() > 0 )
		synchronized ( soldiers )
		{
			for( Mobile m : soldiers)
			{
				this.kickOut( m );
			}		
		}
		for( Food f : carriedStuff )
		{
			f.setPicked(false);
		}
		
		SmartConsole.Print("Formation " + getID() + " disbanded.", DebugModes.FORMATION);
		soldiers.clear();
		carriedStuff.clear();
		this.Die();
		//context.remove(this);
	}
	public void MoveThere ( )
	{
		// Makes formation look cooler by randomizing the movement of each mobile
		Random rnd = new Random();
		
		this.moveTowards( this.goingPoint );
		GridPoint gp = grid.getLocation(this);
		for ( Mobile m : soldiers )
		{
			int randX = ( RandomHelper.nextIntFromTo(-1, 1) );
			int randY = ( RandomHelper.nextIntFromTo(-1, 1) );
			
			space.moveTo(m, gp.getX() + randX, gp.getY() + randY);
			grid.moveTo( m, gp.getX() + randX, gp.getY() + randY);
		}
	}
	public void ActOnArrival()
	{
		SmartConsole.Print("Formation " + getID() + " arrived.", DebugModes.FORMATION);
		switch ( this.goingWhere )
		{
			case Explore:
					// wat?
				break;
			case ForFood:
					//AskForFood();
					SmartConsole.Print("Formation " + getID() + " for food.", DebugModes.FORMATION);
					PickupFood();
				break;
			case Home:
					// TODO
				break;
			case HomeWithFood:
					SmartConsole.Print("Formation " + getID() + " home with food.", DebugModes.FORMATION);
					DropCarriedFood();
				break;
			case Wpierdol:
				SmartConsole.Print("Formation " + getID() + " for wpierdol.", DebugModes.FORMATION);
				// look for enemies in 5x5 NH
				GridPoint closestEnemy = AreEnemiesNearby();
				// if NH contains enemies
				if( closestEnemy != null)
				{
					moveTowards(closestEnemy);
					return;
				}
				else
					this.Disband();
			case Uknown: break;
			default: break;
		}
	}	
	@SuppressWarnings("unchecked")
	private void PickupFood() {
		
		List<Food> foodHere = FoodAtPoint( grid.getLocation(this) );
		if ( foodHere.size() <= 0 ) 
		{
			SmartConsole.Print("Formation " + getID() + " no food found.", DebugModes.FORMATION);
			this.Disband();
			return;
		}
		
		// food with highest power-weight ratio
		Collections.sort( foodHere );
		
		// iterate over foodHere
		for ( Food food : foodHere )
		{
			if ( this.getCarryCapacity() >= food.getWeight() )
			{
				StartCarrying( food );
				break;
			}
		}
		
	}

	private void DropCarriedFood( )
	{
		if ( carriedStuff != null )
		{
			Maw m = MawFinder.Instance().GetMaw( this.playerID );
			for( Food f : carriedStuff )
				m.ReceiveFood( f );
			this.carriedStuff = new ArrayList<Food>();
		}
		this.Disband();
	}	
	
	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.CREATURES_MOVE_INTERVAL)
	public void step()
	{
		if(!addPending()) return;
		FormationAttackCheck();
		
		// NOT ENOUGH BROS IN FORMATION
		if ( this.getSize() < this.getNeededSize() / Constants.FORMATION_NEEDED_FRACTION )
		{
			//this.findNewMember(this.playerID);
			this.Disband(); // TODO: emergency disband?
			return;
		}
		else
		{
			if ( !isComplete )
			{
				SmartConsole.Print("Formation " + getID() + " assembly completed.", DebugModes.FORMATION);
				isComplete = true;
			}
		}		
	
		if(isFighting)
		{
			SmartConsole.Print("Formation " + getID() + " formation fighting.", DebugModes.FORMATION);

			Attack();
			return;
		}
		// ARRIVED.
		if ( this.IsAtDestination() )
		{
			this.ActOnArrival();
			return;
		} 
		
		// FOOD LOGIC
		if ( this.goingWhere == GoingWhere.ForFood || this.goingWhere == GoingWhere.HomeWithFood )
		{

			// MOVE SOMEWHERE
			if ( this.isGoingSomewhere() )
			{
				this.MoveThere();
				this.MoveCarriedStuff();
			}
		}
		
		// WPIERDOL LOGIC
		if ( this.goingWhere == GoingWhere.Wpierdol )
		{
			// look for enemies in 5x5 NH
			GridPoint closestEnemy = AreEnemiesNearby();
			// if NH contains enemies
			if( closestEnemy != null)
			{
				moveTowards(closestEnemy);
				return;
			}
			else
			{
				MoveThere();
			}
		}
	}

	public void FormationAttackCheck(){
		GridPoint pt = grid.getLocation ( this );
		isFighting = false;
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid , pt ,
		Mobile . class , 1 , 1);
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Fightable && (Fightable)obj != this){			
					Fightable mobile = (Fightable)obj;
					if(!MawFinder.Instance().areWeFriends(mobile.playerID, this.playerID))
					{
						isFighting = true;
					}
				}
			}
		}
	}
	public GridPoint AreEnemiesNearby(){
		GridPoint pt = grid.getLocation ( this );
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid , pt ,
		Mobile . class , Constants.MOBILE_VICINITY_X , Constants.MOBILE_VICINITY_Y);
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		List <GridPoint> enemies = new ArrayList<GridPoint>();
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Fightable && (Fightable)obj != this){
					Fightable mobile = (Fightable)obj;
					if(!MawFinder.Instance().areWeFriends(mobile.playerID, this.playerID))
					{
						enemies.add(cell.getPoint());
					}
				}
			}
		}
		
		if( enemies.size() > 0)
			return findClosest(enemies, pt);
		
		return null;
	}
	
	public GridPoint findClosest(List <GridPoint> enemies, GridPoint here){
		GridPoint closest = enemies.get(0);
		for( int i = 0; i < enemies.size(); i++){
			if(grid.getDistance(closest, here) > grid.getDistance(here, enemies.get(i)))
			{
				closest = enemies.get(i);
			}
		}
		
		return closest;
	}
	
	public boolean moveTowards( GridPoint gp )
	{
		// only move if not already there
		if ( gp != null &&  !gp.equals( grid.getLocation(this) ) )
		{
			
			NdPoint thisLocation = space.getLocation(this);
			NdPoint goalLocation = new NdPoint ( gp.getX (), gp.getY ());
			double angle = SpatialMath.calcAngleFor2DMovement( space, thisLocation, goalLocation );
			space.moveByVector(this, 1, angle, 0);
			thisLocation = space.getLocation(this);	
			
			// WARNING: without Math.round this gets cut and has a converging behavior when running randomly around
			grid.moveTo(this, (int)Math.round(thisLocation.getX()), (int)Math.round(thisLocation.getY()) );
			
			for ( Mobile m : soldiers )
			{
				space.moveTo(m, thisLocation.getX(), thisLocation.getY());
				grid.moveTo( m, (int)thisLocation.getX(), (int)thisLocation.getY());
			}
			return true;
		}
		return false;
	}
	

	/**
	 * @return the carryCapacity
	 */
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
		return isGoingSomewhere && this.goingPoint != null;
	}

	/**
	 * @param isGoingSomewhere the isGoingSomewhere to set
	 */
	public void setGoingSomewhere(boolean isGoingSomewhere) {
		this.isGoingSomewhere = isGoingSomewhere;
	}

	/**
	 * @return the goingWhere
	 */
	public GoingWhere getGoingWhere() {
		return goingWhere;
	}

	/**
	 * @param goingWhere the goingWhere to set
	 */
	public void setGoingWhere(GoingWhere goingWhere) {
		this.goingWhere = goingWhere;
	}

	/**
	 * @return the neededSize
	 */
	public int getNeededSize() {
		return neededSize;
	}

	/**
	 * @param neededSize the neededSize to set
	 */
	public void setNeededSize(int neededSize) {
		this.neededSize = neededSize;
	}
	
	public GridPoint getGoingPoint() {
		return goingPoint;
	}
	public void setGoingPoint(GridPoint goingPoint) {
		this.goingPoint = goingPoint;
	}
	public int getCarriedWeight() {
		return carriedWeight;
	}
	public void setCarriedWeight(int carriedWeight) {
		this.carriedWeight = carriedWeight;
	}
	public List<Food> getCarriedStuff() {
		return carriedStuff;
	}
	public void setCarriedStuff(List<Food> carriedStuff) {
		this.carriedStuff = carriedStuff;
	}	
	
}
