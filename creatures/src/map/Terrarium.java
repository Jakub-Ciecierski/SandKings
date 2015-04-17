package map;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Viet Ba
 *	class for Terrarium
 */

public class Terrarium {
	
	private int colourID;
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	public Terrarium (ContinuousSpace<Object> space, Grid<Object> grid, int colourID) {
		this.space = space;
		this.grid = grid;
		this.colourID = colourID;
	}
	
	@Parameter(displayName = "MapColour", usageName = "colourID")
	public int getColourID() {
		return colourID;
	}
	
	/**
	 * @param foodID the colourID to set
	 */
	public void setColourID(int resourceID) {
		this.colourID = resourceID;
	}

}
