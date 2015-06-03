package creatures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import communication.messages.DamageMessage;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import map.Food;
import Constants.Constants;
import creatures.CreatureClasses.*;
import creatures.CreatureClasses.Mobile.GoingWhere;

public class Formation extends Fightable {
	
	public Formation(ContinuousSpace<Object> space, Grid<Object> grid,
			int playerID) {
		super(space, grid, 5);
		if ( grid == null ) System.out.println("formation grid null");
		if ( space == null ) System.out.println("formation space null");
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
	private List<Integer> owners = new ArrayList<Integer>();
	
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
		if ( grid == null ) System.out.println("find member: formation grid null");
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
		System.out.println("   found new pending member");
		GridPoint currentPos = grid.getLocation(this);
		m.setInFormation(true);
		m.setGoingSomewhere(false);
		//m.setGoingPoint( currentPos );
		m.setMyFormation(this);
		
		this.pendingSoldiers.add(m);
	}
	
	public void addPending()
	{
		GridPoint currentPos = grid.getLocation(this);
		
		List<Mobile> newPendingSoldiers = new ArrayList<Mobile>();
		for( int i = 0; i < pendingSoldiers.size(); i++)
		{
			
			if(!pendingSoldiers.get(i).IsAtLocation(currentPos))
			{
				//System.out.println("   soldier " + i + "[#"+ pendingSoldiers.get(i).getID() +"/" + pendingSoldiers.size() + "]     moving towards me: ["+currentPos.getX()+":"+currentPos.getY()+"]");
				pendingSoldiers.get(i).moveTowards(currentPos);
				
				newPendingSoldiers.add(pendingSoldiers.get(i));
			}
			else
			{
				
				//pendingSoldiers.get(i).
				//System.out.println("   pending soldier arrived.");
				
				this.setCarryCapacity(this.getCarryCapacity() + pendingSoldiers.get(i).getCarryCapacity());
				soldiers.add(pendingSoldiers.get(i));
				
			}
		}
		
		pendingSoldiers = newPendingSoldiers;
	}
	

	public void kickOut( Mobile m )
	{
		//System.out.println("   kicked out member");
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
		return soldiers.size() + pendingSoldiers.size();
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
		setCarryCapacity(getCarryCapacity() + food.getWeight()); 
		System.out.println("Formation " + getID() + " picked up food.");
				
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
		System.out.println("   disbanded.");
		soldiers.clear();
		this.Die();
	}
	public void MoveThere ( )
	{
		//System.out.println("formation movin'");
		this.moveTowards( this.goingPoint );
		GridPoint gp = grid.getLocation(this);
		for ( Mobile m : soldiers )
		{
				space.moveTo(m, gp.getX(), gp.getY());
				grid.moveTo( m, gp.getX(), gp.getY());
		}
	}
	public void ActOnArrival()
	{
		System.out.print("Formation " + getID() + " arrived");
		switch ( this.goingWhere )
		{
			case Explore:
					// wat?
				break;
			case ForFood:
					//AskForFood();
						System.out.println("  for food.");
					PickupFood();
				break;
			case Home:
					// TODO
				break;
			case HomeWithFood:
						System.out.println("  home with food.");
					DropCarriedFood();
				break;
			case Wpierdol:
				System.out.println("    for wpierdol.");
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
			System.out.println("no food found");
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
			//System.out.println("food too heavy");
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
		addPending();
		FormationAttackCheck();
		if(isFighting)
		{
			System.out.println("formation fighting.");
			Attack();
			return;
		}
		// ARRIVED.
		if ( this.IsAtDestination() )
		{
			this.ActOnArrival();
			return;
		} 
		
		//System.out.println("formation checkpoint 1");
		
		// NOT ENOUGH BROS IN FORMATION
		if ( this.getSize() < this.getNeededSize() )
		{
			/*System.out.println(
				"Formation " + getID() + " ["+ this.getSize() + "/" + this.getNeededSize() + "]" + 
					" called for bros.");*/
			this.findNewMember(this.playerID);
			return;
		} else
		{
			if ( !isComplete )
			{
				System.out.println("Formation " + getID() + " assembly completed. " );
				isComplete = true;
			}
		}
		
		// FOOD LOGIC
		if ( this.goingWhere == GoingWhere.ForFood || this.goingWhere == GoingWhere.HomeWithFood )
		{

			// MOVE SOMEWHERE
			if ( this.isGoingSomewhere() )
			{
				/*System.out.println(
						"Formation " + getID() + " ["+ this.getSize() +"]" + 
							" going somewhere: " + this.goingPoint.getX() + ":" + this.goingPoint.getY() );*/
				this.MoveThere();
				this.MoveCarriedStuff();
			}
		}
		//System.out.println("formation checkpoint 2");
		
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
		Mobile . class , 5 , 5);
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
			
			//System.out.println("      f ["+ this.getID() +"] going from: " + thisLocation.getX() + ":" + thisLocation.getY() + " to: " + gp.getX() + ":" + gp.getY() );

			
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
