/**
 * 
 */
package jzombies;

import java.util.ArrayList;
import java.util.List;

import communication.MessageHandler;
import communication.MessageQueue;
import communication.messages.QueryMessage;
import communication.rules.RuleSet;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.graph.Network;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.SimUtilities;


/**
 * @author Kuba
 *
 */
public class Mobile extends Agent {
	private ContinuousSpace < Object > space;
	private Grid < Object > grid;

	private boolean moved = false;
	
	public RuleSet ruleSet;
	
	public Mobile (int id, ContinuousSpace < Object > space , Grid < Object > grid, MessageQueue messageQueue) {
		this.id = id;
		
		this.space = space;
		this.grid = grid;
		
		ruleSet = new RuleSet();
		
		this.messageQueue = messageQueue;
	}
	
	@ScheduledMethod ( start = 1 , interval = 1)
	public void step(){
		// get current location in grid
		GridPoint gp = grid.getLocation(this);
		
		// get random next position
		int randX = ( RandomHelper.nextIntFromTo(-1, 1) );
		int randY = ( RandomHelper.nextIntFromTo(-1, 1) );

		randX += gp.getX();
		randY += gp.getY();
		
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
		lookForFriends();
		this.moveTowards(randomGP);
	}
	
	public void lookForFriends(){
		// get the grid location of this Human
		GridPoint pt = grid.getLocation ( this );
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid , pt ,
		Mobile . class , 1 , 1);
		
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Mobile && (Mobile)obj != this){
					QueryMessage query = new QueryMessage("Do you love me ?");
					
					sendMessage((Mobile)obj,query);
				}
			}
			
		}
		
		
	}

	public void moveTowards ( GridPoint pt ) {
		// only move if we are not already in this grid location
		if (!pt.equals ( grid.getLocation ( this ))) {
			NdPoint myPoint = space.getLocation ( this );
			NdPoint otherPoint = new NdPoint (pt.getX(), pt.getY());
			
			double angle = SpatialMath.calcAngleFor2DMovement ( space ,
			myPoint , otherPoint );
			
			space.moveByVector (this, 1, angle, 0);
			myPoint = space.getLocation (this);
			grid.moveTo (this, (int)myPoint.getX(), (int)myPoint.getY());
			
			moved = true;
		}
	}

}
