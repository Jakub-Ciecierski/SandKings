package map;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;


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
	
	public Food (ContinuousSpace<Object> space, Grid<Object> grid, int foodID) {
		this.space = space;
		this.grid = grid;
		this.foodID = foodID;
		
		switch(this.foodID) {
			case 0: //pizza
				power = 50;
				weight = 3;
				break;
			case 1: //donut
				power = 40;
				weight = 2;
				break;
			case 2: //grape
				power = 20;
				weight = 5;
				break;
			case 3: //cabbage
				power = 10;
				weight = 10;
				break;
			default:
				break;
		}
		if ( weight == 0 ) weight = 1; // because no.
		this.ratio = power / weight;
	}
	
	@Parameter(displayName = "Food", usageName = "foodID")
	public int getFoodID() {
		return foodID;
	}
	
	/**
	 * @param enemyID the foodID to set
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
	
	
}
