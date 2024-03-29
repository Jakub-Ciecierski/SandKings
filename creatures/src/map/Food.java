package map;

import creatures.Agent;
import creatures.CreatureClasses.MawFinder;
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

@SuppressWarnings("rawtypes")
public class Food extends Agent implements Comparable {
	private int foodID; //food type
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	
	private int power = 1;
	private int weight = 1;
	
	private int ownerID = 0;
	
	private boolean isPicked = false;
	
	private boolean isDelivered = false; 
	
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
			case 4: //meat
				power = Constants.MEAT_CALORIES;
				weight = Constants.MEAT_WEIGHT;
				break;
			case 5: //buffalo steak
				power = Constants.STEAK_CALORIES;
				weight = Constants.MEAT_WEIGHT;
				break;
			default:
				break;
		}
		if ( weight == 0 ) weight = 1; // because no.
	}
	
	public void Delete() {
		  @SuppressWarnings("unchecked")
		Context<Object> context = ContextUtils.getContext(this);
		  if(this != null && context != null)
			  context.remove( this );	
	 }
	
	public void updateProfit(){
		profit = power;
	}
	public float getRatio()
	{
		return profit/(50 * weight);
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

	@Override
	public int compareTo(Object o) {
		if ( o.getClass() != Food.class )
			return -100;
		Food f = (Food) o;
		return (int) ( this.getRatio() - f.getRatio() ) ;
	}
	
	public boolean isPickingBlocked( int fractionID ) {
		return isPicked || ( this.ownerID != fractionID && MawFinder.Instance().areWeFriends( this.getOwnerID(), fractionID ) );
	}

	public boolean isPicked() {
		return isPicked;
	}

	public void setPicked(boolean isPicked) {
		this.isPicked = isPicked;
	}

	/**
	 * @return the ownerID
	 */
	public int getOwnerID() {
		return ownerID;
	}

	/**
	 * @param ownerID the ownerID to set
	 */
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}
	
	public boolean isDelivered(){
		return this.isDelivered;
	}
	
	public void setDelivered(boolean delivered){
		this.isDelivered = delivered;
	}
	
}
