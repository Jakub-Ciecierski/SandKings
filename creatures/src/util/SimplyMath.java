package util;

import repast.simphony.space.grid.GridPoint;

public class SimplyMath {
	
	public static double Distance(GridPoint gp1, GridPoint gp2){
		double distance = 0;
		
		double dx = gp1.getX() - gp2.getX();
		double dy = gp1.getY() - gp2.getY();
		distance = Math.sqrt(dx*dx + dy*dy);
		
		return distance;
	}
}
