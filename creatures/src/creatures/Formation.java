package creatures;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import map.Food;
import Constants.Constants;
import creatures.CreatureClasses.*;
import creatures.CreatureClasses.Mobile.GoingWhere;

public class Formation {
	
	public enum GoingWhere
	{
		Uknown,
		Home,
		ForFood,
		HomeWithFood,
		Explore,
		Wpierdol
	}
	private int playerID = 0;
	private int neededSize = 0;
	//private int carryCapacity = 0;
	//private float size = Constants.CREATURES_SIZE;
	
	//Moving logic
	private boolean isGoingSomewhere = false;
	private GridPoint goingPoint;
	private GoingWhere goingWhere = GoingWhere.Uknown;
	
	// simulation props
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;	
	
	List<Mobile> soldiers = new ArrayList<Mobile>();
	
	// carrying stuff
	private int carryCapacity = 0;
	private Food carriedStuff;	
	
	public Formation(ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID)
	{
		this.playerID = setPlayerID;
	}
	
	// only called when we need a new member
	public void findNewMember()
	{
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
							 mobile.getPlayerID() == this.playerID && 
							!mobile.isInFormation() &&
							!mobile.isGoingSomewhere()
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
		this.soldiers.add(m);
		this.setCarryCapacity(this.getCarryCapacity() + m.getCarryCapacity());
	}
	public void kickOut( Mobile m )
	{
		m.setInFormation(false);
		this.setCarryCapacity(this.getCarryCapacity() - m.getCarryCapacity());
		this.soldiers.remove(m);
	}
	
	public int getSize()
	{
		return soldiers.size();
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
				food.add( (Food) obj);
			}
		}
		return food;
	}
	public void PickUpFood( Food f )
	{
		// TODO: do this.
	}
	public void MoveCarriedStuff()
	{
		GridPoint gp = grid.getLocation(this);
		space.moveTo(this.carriedStuff, gp.getX(), gp.getY());
		grid.moveTo(this.carriedStuff, gp.getX(), gp.getY());
	}	
	public void StartCarrying( Food food )
	{
		//this.carriedStuff.add(food);
		this.carriedStuff = food;
		//this.carriedWeight += food.getWeight();
		food.setPicked(true);
	}	
	public void attack( /* Fightable f */ )
	{
		for( Mobile m : soldiers)
		{
			// ey yo, m, attack this!
		}
	}
	public void Disband()
	{
		for( Mobile m : soldiers)
		{
			this.kickOut( m );
		}		
	}
	public void MoveThere ( )
	{
		for ( Mobile m : soldiers )
		{
			m.moveTowards( this.goingPoint );
		}
	}
	public void ActOnArrival()
	{
		// TODO: figure shit out
	}	
	public void step()
	{
		if ( this.getSize() < this.getNeededSize() )
		{
			this.findNewMember();
		}
		
		if ( this.IsAtDestination() )
		{
			this.ActOnArrival();
		} else if ( this.isGoingSomewhere() )
		{
			this.MoveThere();
			this.MoveCarriedStuff();
		} else {
			//this.Explore();
			this.Disband();
			this.MoveCarriedStuff();
		}
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
		return isGoingSomewhere;
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
}
