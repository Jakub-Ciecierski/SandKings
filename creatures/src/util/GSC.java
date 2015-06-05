package util;

import map.EventType;
import map.EventsInfo;
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
		
		for(Object obj : grid.getObjectsAt( gp.getX(), gp.getY() ))
			if(obj instanceof EventsInfo) return;
				
		Context<Object> context = GSC.Instance().getContext();
		if (this.context == null) return;
		
		EventsInfo info = new EventsInfo(this.space, this.grid, type, timeout);
		
	    context.add(info);			     
	    this.grid.moveTo(info, (int)gp.getX(), (int)gp.getY());
	    this.space.moveTo(info,  (int)gp.getX(), (int)gp.getY());
		
	}

}
