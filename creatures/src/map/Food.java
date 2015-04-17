package map;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;


/**
 * @author Viet Ba
 *	class for Terrarium
 */

public class Food {

	private int foodID; //food type
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	public Food (ContinuousSpace<Object> space, Grid<Object> grid, int foodID) {
		this.space = space;
		this.grid = grid;
		this.foodID = foodID;
	}
	
	@Parameter(displayName = "Food", usageName = "foodID")
	public int getFoodID() {
		return foodID;
	}
	
	/**
	 * @param foodID the foodID to set
	 */
	public void setFoodID(int resourceID) {
		this.foodID = resourceID;
	}
	
}
