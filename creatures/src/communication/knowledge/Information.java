package communication.knowledge;

import repast.simphony.space.grid.GridPoint;

/*
 * The information consists of:
 * 	What - What was seen
 * 	When - time when it was seen
 * 	Where - point in the map
 */
public class Information {

	// What is this information
	private InformationType type;
	// When it was received
	private double tickCount;
	// Where it was seen
	private GridPoint point;

	public Information(InformationType type, double tickCount, GridPoint point){
		this.type = type;
		this.tickCount = tickCount;
		this.point = point;
	}
	
}