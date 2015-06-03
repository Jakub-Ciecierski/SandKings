package schedules.tasks.mobile;

import java.util.List;

import map.Food;
import Constants.Constants;
import communication.knowledge.Information;
import creatures.Agent;
import creatures.CreatureClasses.Mobile;
import creatures.CreatureClasses.Mobile.GoingWhere;
import repast.simphony.space.grid.GridPoint;
import schedules.tasks.Task;

public class ReturnFoodTask extends Task{

	private Mobile mobile;
	
	private int stage = 0;
	
	public ReturnFoodTask(Information information, Mobile mobile) {
		super(information);
		this.mobile = mobile;
		
		/*
		if(Constants.DEBUG_MODE){
			System.out.println("*********************************************************");
			System.out.println("Agent #" + mobile.getID() +" New ReturnFoodTask");
			System.out.println("********************************************************* \n\n");
		}*/
	}

	@Override
	public void execute() {
		GridPoint destPoint = information.getGridPoint();
		GridPoint currPoint = mobile.grid.getLocation(mobile);
		
		if(stage == 0) {
			mobile.moveTowards(destPoint);
		}
		
		if(currPoint.getX() == destPoint.getX() && 
			currPoint.getY() == destPoint.getY())
		{
			stage = 1;
			
			List<Food> foodHere = mobile.FoodAtPoint( destPoint );
			
			if ( foodHere.size() > 0 ) {
				mobile.PickUpFood( foodHere );
			}
		}
		
		if(stage == 1 && mobile.carriedStuff == null){
			stage = 2;
		}
		
		if(stage == 2){
			finish();
			return;
		}
	}

}
