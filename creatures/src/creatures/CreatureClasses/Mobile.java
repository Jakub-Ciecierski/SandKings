package creatures.CreatureClasses;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import creatures.Agent;
import map.Food;
import repast.simphony.parameter.Parameter;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.Dimensions;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;

/**
 * @author Asmodiel
 *	base class for mobile
 */
public abstract class Mobile extends Agent{
	
	public enum GoingWhere
	{
		Uknown,
		Home,
		ForFood,
		HomeWithFood,
		Explore,
		Wpierdol
	}
	
	// creature properties
	private float strength = 0;
	private int experience = 0;
	private int intelligence = 0;
	private int carryCapacity = 1000;
	private int carriedWeight = 0;
	private List<Food> carriedStuff = new ArrayList<Food>();
	private int diplomacySkill = 0;
	
	//Moving logic
	private boolean isGoingSomewhere = false;
	private GridPoint goingPoint;
	private GoingWhere goingWhere = GoingWhere.Uknown;
	
		private int playerID = 0;

	// simulation props
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	
	public Mobile( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID)
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
	}
	
	// are we standing on food?
	public List<Food> FoodAtPoint(GridPoint pt)
	{
		List<Food> food = new ArrayList<Food>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Food) {
				food.add( (Food) obj);
			}
		}
		return food;
	}
	
	private void StartCarrying( Food food )
	{
		this.carriedStuff.add(food);
		this.carriedWeight += food.getWeight();
		food.setPicked(true);
	}
	
	protected void MoveCarriedStuff()
	{
		// get current location in grid
		GridPoint gp = grid.getLocation(this);
		for ( Food food : this.carriedStuff )
		{
			space.moveTo(food, gp.getX(), gp.getY());
			grid.moveTo(food, gp.getX(), gp.getY());
		}
	}
	
	public void MoveThere()
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
		switch ( this.goingWhere )
		{
			case Explore:
					// wat?
				break;
			case ForFood:
					// TODO
				break;
			case Home:
					// TODO
				break;
			case HomeWithFood:
					DropFood();
				break;
			case Wpierdol:
				
				break;
			default: break;
			case Uknown: break;				
		}
		this.goingPoint = null;
		this.isGoingSomewhere = false;
		this.goingWhere = GoingWhere.Uknown;
	}
	
	private void DropFood( )
	{
		Maw m = MawFinder.Instance().GetMaw( this.playerID );
		for ( Food f : this.carriedStuff )
		{
			m.GiveFood( f );
		}
		this.carriedWeight = 0;
		this.carriedStuff.clear();
	}
	
	private void GoHome()
	{
		this.setGoingSomewhere(true);
		goingPoint = MawFinder.Instance().GetMawPosition( this.playerID );
	}

	@SuppressWarnings("unchecked")
	private void PickUpFood(List<Food> foodHere) {
		int found = 0;
		// food with highest power-weight ratio
		Collections.sort( foodHere );
		
		// iterate over foodHere
		for ( Food food : foodHere )
		{
			// check if food too heavy
			if ( food.getWeight() > ( this.carryCapacity - this.carriedWeight ) )
			{
				// TODO: call for bros or ignore this food
				continue; 
			} else
			{
				// lift
				if(!food.isPicked()) {
					StartCarrying( food );
					found ++;
				}
			}
		}
		
		if ( found > 0 )
		{
			this.goingWhere = GoingWhere.HomeWithFood;
			// go home.
			GoHome();
		}
	}

	public void Explore()
	{
		// get current location in grid
		GridPoint gp = grid.getLocation(this);
		
		// TODO: remember food in vicinity
	
		List<Food> foodHere = FoodAtPoint( gp );
		if ( foodHere.size() > 0 ) PickUpFood( foodHere );
		
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
		// only move if not already there
		if ( !gp.equals( grid.getLocation(this) ) )
		{
			NdPoint thisLocation = space.getLocation(this);
			NdPoint goalLocation = new NdPoint ( gp.getX (), gp.getY ());
			double angle = SpatialMath.calcAngleFor2DMovement( space, thisLocation, goalLocation );
			space.moveByVector(this, 1, angle, 0);
			thisLocation = space.getLocation(this);	
			// WARNING: without Math.round this gets cut and has a converging behavior when running randomly around
			grid.moveTo(this, (int)Math.round(thisLocation.getX()), (int)Math.round(thisLocation.getY()) );
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
	 * @return the strength
	 */
	@Parameter(displayName = "strength", usageName = "strength")
	public float getStrength() {
		return this.strength;
	}
	/**
	 * @param strength the strength to set
	 */
	public void setStrength(float strength) {
		this.strength = strength;
	}
	
	/**
	 * @return the experience
	 */
	@Parameter(displayName = "experience", usageName = "experience")
	public int getExperience() {
		return experience;
	}
	/**
	 * @param experience the experience to set
	 */
	public void setExperience(int experience) {
		this.experience = experience;
	}
	
	
	/**
	 * @return the intelligence
	 */
	@Parameter(displayName = "intelligence", usageName = "intelligence")
	public int getIntelligence() {
		return intelligence;
	}
	/**
	 * @param intelligence the intelligence to set
	 */
	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
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
	 * @return the diplomacySkill
	 */
	@Parameter(displayName = "diplomacy", usageName = "diplomacySkill")
	public int getDiplomacySkill() {
		return diplomacySkill;
	}
	/**
	 * @param diplomacySkill the diplomacySkill to set
	 */
	public void setDiplomacySkill(int diplomacySkill) {
		this.diplomacySkill = diplomacySkill;
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
	
	
}
