/**
 * 
 */
package map;

import Constants.Constants;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

/**
 * @author Viet Ba
 * class for events happening in the simulation:
 * - defense
 * - attack
 * - alliance
 * - starvation
 */

public class EventsInfo {
	@SuppressWarnings("unused")
	private ContinuousSpace < Object > space; 
	@SuppressWarnings("unused")
	private Grid< Object > grid;
	private int tickCount = 0;
	private int timeout;
	private EventType eventType;
	
	public EventsInfo (ContinuousSpace<Object> space, Grid<Object> grid, EventType type, int timeout) { 
		this.space = space;
		this.grid = grid;
		this.eventType = type;
		this.timeout = timeout;
	}
	
	public EventType getEventType(){
		return this.eventType;
	}
	
	@SuppressWarnings("unchecked")
	@ScheduledMethod ( start = Constants.MOVE_START , interval = Constants.CREATURES_MOVE_INTERVAL)
	public void step() {		
		this.tickCount++;
		
		if(this.tickCount == this.timeout) {
			Context<Object> context = ContextUtils.getContext(this);
			
			if(this == null || context == null)
				return;
			
			context.remove( this );
		}		
	}
	
}
