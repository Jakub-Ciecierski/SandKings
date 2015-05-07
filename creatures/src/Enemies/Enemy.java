package Enemies;

import java.util.List;

import Constants.Constants;
import map.Food;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameter;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;

public class Enemy {
	private int enemyID; //enemy type
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	public Enemy (ContinuousSpace<Object> space, Grid<Object> grid, int enemyID) {
		this.space = space;
		this.grid = grid;
		this.enemyID = enemyID;
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
		int strength = 0;
		
		switch(this.enemyID) {
		case 0: //spider
			strength = 200;
			break;
		case 1: //snake
			strength = 1000;
			break;
		default:
			break;
		}
		
		return strength;
	} 
	public int getWeight() {
		int weight = 0;
		
		switch(this.enemyID) {
		case 0: //spider
			weight = 10;
			break;
		case 1: //snake
			weight = 100;
			break;
		default:
			break;
		}
		
		return weight;
	}
	
	@ScheduledMethod ( start = 100 , interval = 500)
	public void Move()
	{
		// get current location in grid
		GridPoint gp = grid.getLocation(this);
		
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
}
