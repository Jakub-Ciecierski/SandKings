package util;

import map.EventType;
import map.EventsInfo;
import Constants.Constants;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class GSC {
	// singleton
	private static GSC instance = new GSC();
	public static GSC Instance() { return instance; }
	
	private Context<Object> context;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	
	protected GSC() {}

	public Context<Object> getContext() {
		return context;
	}

	public void setContext(Context<Object> context) {
		this.context = context;
	}

	public ContinuousSpace<Object> getSpace() {
		return space;
	}

	public void setSpace(ContinuousSpace<Object> space) {
		this.space = space;
	}

	public Grid<Object> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}
	
	public void AddEventInfo(EventType type, int timeout, GridPoint gp) {
		if (!(this.grid.getObjectsAt(gp.getX(), gp.getY()) instanceof EventsInfo) ) { //don't create info where it already is
			Context<Object> context = GSC.Instance().getContext();
			if (this.context != null) {
				EventsInfo starvationInfo = new EventsInfo(this.space, this.grid, type, timeout);
<<<<<<< HEAD
			    context.add(starvationInfo);			     
			    this.grid.moveTo(starvationInfo, (int)gp.getX(), (int)gp.getY());
			    this.space.moveTo(starvationInfo,  (int)gp.getX(), (int)gp.getY());
=======
				int distance = Constants.EVENT_DISTANCE;
			    context.add(starvationInfo);
			    this.grid.moveTo(starvationInfo, (int)gp.getX() + distance, (int)gp.getY() + distance);
			    this.space.moveTo(starvationInfo,  (int)gp.getX() + distance, (int)gp.getY() + distance);
>>>>>>> 379b83a276a4a5e0a858f51089094ff0be17c220
			}
		}
	}

}
