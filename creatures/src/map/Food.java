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
	 * @param enemyID the foodID to set
	 */
	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}
	
	public int getPower() {
		int power = 0;
		
		switch(this.foodID) {
		case 0: //pizza
			power = 50;
			break;
		case 1: //donut
			power = 40;
			break;
		case 2: //grape
			power = 20;
			break;
		case 3: //cabbage
			power = 10;
			break;
		default:
			break;
		}
		
		return power;
	} 
	public int getWeight() {
		int weight = 0;
		
		switch(this.foodID) {
		case 0: //pizza
			weight = 3;
			break;
		case 1: //donut
			weight = 2;
			break;
		case 2: //grape
			weight = 5;
			break;
		case 3: //cabbage
			weight = 10;
			break;
		default:
			break;
		}
		
		return weight;
	}
}
