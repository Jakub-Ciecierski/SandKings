package communication.knowledge;

import creatures.Agent;
import repast.simphony.space.grid.GridPoint;

/**
 * The information consists of:
 * 	What - What was seen
 * 	When - time when it was seen
 * 	Where - point in the map
 */
public class Information {

	// Agent that is associated with this information
	private Agent agent;
	// What is this information
	private InformationType type;
	// When it was received
	private double tickCount;
	// Where it was seen
	private GridPoint point;
	
	public boolean isUsefull = true;

	public Information(Agent agent, InformationType type, double tickCount, GridPoint point){
		this.agent = agent;

		this.type = type;
		this.tickCount = tickCount;
		this.point = point;
	}
	
	public InformationType getType(){
		return this.type;
	}
	
	public Agent getAgent(){
		return this.agent;
	}
	
	public GridPoint getGridPoint(){
		return this.point;
	}
	
	public double getTickCount(){
		return this.tickCount;
	}
	
	public void setGridPoint(GridPoint gp){
		this.point = gp;
	}
	
	public void setTickCount(double tickCount){
		this.tickCount = tickCount;
	}
	
}