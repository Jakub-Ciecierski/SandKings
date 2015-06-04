/**
 * 
 */
package map;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * @author Viet Ba
 *
 */
public class GraveStone {
	@SuppressWarnings("unused")
	private ContinuousSpace < Object > space; 
	@SuppressWarnings("unused")
	private Grid< Object > grid;
	
	public GraveStone (ContinuousSpace<Object> space, Grid<Object> grid) { 
		this.space = space;
		this.grid = grid;
	}
}
