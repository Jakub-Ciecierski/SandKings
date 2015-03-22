package creatures.CreatureClasses;

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
public abstract class Mobile {
	
	// creature properties
	private int strength = 0;
	private int experience = 0;
	private int intelligence = 0;
	private int carryCapacity = 0;
	private int diplomacySkill = 0;
	
	private int playerID = 0;
	
	// simulation props
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	
	public Mobile( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID )
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
	}

	public void randomMove()
	{
		// get current location in grid
		GridPoint gp = grid.getLocation(this);
		// get random next position
		int randX = gp.getX() + RandomHelper.nextIntFromTo(-1, 2);
		int randY = gp.getY() + RandomHelper.nextIntFromTo(-1, 2);
		
		// catch out of bounds
		GridDimensions spaceDim = grid.getDimensions();
		
		// X too big
		if ( randX >= spaceDim.getWidth() )
		{
			randX = spaceDim.getWidth() - 1;
		}
		// Y too big
		if ( randY >= spaceDim.getHeight() )
		{
			randY = spaceDim.getHeight() - 1;
		}
		
		// X too small
		if ( randX < 1 )
		{
			randX = 1;
		}
		// Y too small
		if ( randY < 1 )
		{
			randY = 1;
		}
		
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
			grid.moveTo(this, (int)thisLocation.getX(), (int)thisLocation.getY() );
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
	public int getStrength() {
		return strength;
	}
	/**
	 * @param strength the strength to set
	 */
	public void setStrength(int strength) {
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
	
	
}
