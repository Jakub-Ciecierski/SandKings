package Enemies;

import java.util.ArrayList;
import java.util.List;

import creatures.CreatureClasses.Mobile;
import Constants.Constants;
import map.Food;
import repast.simphony.engine.schedule.ScheduledMethod;
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
import repast.simphony.util.SimUtilities;

public class Enemy implements Comparable {
	private int enemyID; //enemy type
	private int strength;
	private int weight;
	private int health;
	private int calories;
	private double ratio;
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	public Enemy (ContinuousSpace<Object> space, Grid<Object> grid, int enemyID) {
		this.space = space;
		this.grid = grid;
		this.enemyID = enemyID;
		
		switch(this.enemyID) {
		case 0: //spider
			this.strength = Constants.SPIDER_ATTACK;
			this.setHealth(Constants.SPIDER_HEALTH);
			this.calories = Constants.SPIDER_CALORIES;
			this.weight = Constants.SPIDER_WEIGHT;
			break;
		case 1: //snake
			this.strength = Constants.SNAKE_ATTACK;
			this.setHealth(Constants.SNAKE_HEALTH);
			this.calories = Constants.SNAKE_CALORIES;
			this.weight = Constants.SNAKE_WEIGHT;
			break;
		default:
			break;
		}
		this.ratio = ( this.calories * 1000 )/ ( this.health + this.strength + this.weight );
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
	
	public int getStrength() {		
		return this.strength;
	} 
	public int getWeight() {
		return this.weight;
	}
	
	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.ENEMIES_MOVE_INTERVAL)
	public void step()
	{
		Move();
		Attack();
	}
	
	public void Attack()
	{
		GridPoint gp = grid.getLocation(this);
		GridCellNgh<Mobile> nghCreator = new GridCellNgh<Mobile>(grid, gp, Mobile.class, 1, 1);
		List<GridCell<Mobile>> gridCells = nghCreator.getNeighborhood(true);
		SimUtilities.shuffle(gridCells, RandomHelper.getUniform());

		for (GridCell<Mobile> cell : gridCells) {
			if (cell.size() > 0) {
				for(Mobile m : cell.items())
					m.Damage(this.strength);
			}
		}
	}
	public void Move()
	{
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
		if ( randX < 10 ) randX = 10;
		// Y too small
		if ( randY < 10 ) randY = 10;
		
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

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getCalories() {
		return calories;
	}

	public double getRatio() {
		return ratio;
	}

	@Override
	public int compareTo(Object o) {
		if ( o.getClass() != Enemy.class )
			return -100;
		Enemy e = (Enemy) o;
		return (int)( this.getRatio() - e.getRatio() ) ;
	}

}
