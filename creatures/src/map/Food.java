package map;

import repast.simphony.context.Context;
import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import Constants.Constants;

/**
 * @author Viet Ba
 *	class for Terrarium
 */

public class Food implements Comparable {
	private int foodID; //food type
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	
	private int power = 1;
	private int weight = 1;
	private int ratio = 1;
	private boolean isPicked = false;
	public Food (ContinuousSpace<Object> space, Grid<Object> grid, int foodID) {
		this.space = space;
		this.grid = grid;
		this.foodID = foodID;
		
		switch(this.foodID) {
			case 0: //pizza
				power = Constants.PIZZA_CALORIES;
				weight = Constants.PIZZA_WEIGHT;
				break;
			case 1: //donut
				power = Constants.DONUT_CALORIES;
				weight = Constants.DONUT_WEIGHT;
				break;
			case 2: //grape
				power = Constants.GRAPE_CALORIES;
				weight = Constants.GRAPE_WEIGHT;
				break;
			case 3: //cabbage
				power = Constants.CABBAGE_CALORIES;
				weight = Constants.CABBAGE_WEIGHT;
				break;
			default:
				break;
		}
		if ( weight == 0 ) weight = 1; // because no.
		this.ratio = power / weight;
	}
	
	public void Delete() {
		  Context<Object> context = ContextUtils.getContext(this);
		  if(this != null && context != null)
			  context.remove( this );	
	 }
	
	@Parameter(displayName = "Food", usageName = "foodID")
	public int getFoodID() {
		return foodID;
	}
	
	/**
	 * @param foodID the foodID to set
	 */
	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}
	
	public int getPower() {
		return power;
	} 
	public int getWeight() {		
		return weight;
	}
	public int getRatio() {
		return ratio;
	}

	@Override
	public int compareTo(Object o) {
		if ( o.getClass() != Food.class )
			return -100;
		Food f = (Food) o;
		return ( this.getRatio() - f.getRatio() ) ;
	}

	public boolean isPicked() {
		return isPicked;
	}

	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}
	
	
}
