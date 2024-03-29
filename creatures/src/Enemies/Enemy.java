package Enemies;

import creatures.Fightable;
import Constants.Constants;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;


/**
 * @author Viet Ba
 *
 */
@SuppressWarnings("rawtypes")
public class Enemy extends Fightable implements Comparable {
	private int enemyID; //enemy type
	
	
	private int countToMove = 0;
	
	public Enemy (ContinuousSpace<Object> space, Grid<Object> grid, int enemyID,
			float attack, float health, int droppedMeat) {
		super(space, grid, 0, attack, health, droppedMeat);

		this.enemyID = enemyID;
		
		updateDanger();
		updateProfit();
	}
	
	public void updateDanger(){
		danger = getDamage() * getHealth();
	}
	public void updateProfit(){
		profit = getDroppedMeat() * Constants.STEAK_CALORIES;
	}
	public float getRatio()
	{
		return profit/danger;
	}
	
	@Parameter(displayName = "Enemy", usageName = "enemyID")
	public int getEnemyID() {
		return enemyID;
	}
	
	/**
	 * @param enemyID the enemyID to set
	 */
	public void setEnemyID(int enemyID) {
		this.enemyID = enemyID;
	}
	
	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.CREATURES_MOVE_INTERVAL)
	public void step()
	{
		countToMove++;
		if ( countToMove == Constants.ENEMIES_MOVE_INTERVAL )
		{
			Move();
			countToMove = 0;
		}
		Attack();
	}

	public void Move()
	{
		if(isFighting)
			return;
		
		// get current location in grid
		GridPoint gp = grid.getLocation(this);
		int margin = Constants.ENEMIES_MARGIN;
		// get random next position
		int randX = ( RandomHelper.nextIntFromTo(-1, 1) );
		int randY = ( RandomHelper.nextIntFromTo(-1, 1) );

		randX += gp.getX();
		randY += gp.getY();
		
		// X too big
		if ( randX >= Constants.GRID_SIZE - margin ) randX = Constants.GRID_SIZE - margin;
		// Y too big
		if ( randY >= Constants.GRID_SIZE - margin ) randY = Constants.GRID_SIZE - margin;
		
		// X too small
		if ( randX < margin ) randX = margin;
		// Y too small
		if ( randY < margin ) randY = margin;
		
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

	@Override
	public int compareTo(Object o) {
		if ( o.getClass() != Enemy.class )
			return -100;
		Enemy e = (Enemy) o;
		return (int)( this.getRatio() - e.getRatio() ) ;
	}

}
