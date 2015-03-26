package map;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Viet Ba
 *	class for resources
 */

public class Resources {
	
	private int resourceID;
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	public Resources (ContinuousSpace<Object> space, Grid<Object> grid, int resourceID) {
		this.space = space;
		this.grid = grid;
		this.resourceID = resourceID;
	}
	
	@Parameter(displayName = "Resource", usageName = "resourceID")
	public int getResourceID() {
		return resourceID;
	}
	
	/**
	 * @param resourceID the resourceID to set
	 */
	public void setResourceID(int resourceID) {
		this.resourceID = resourceID;
	}

}
