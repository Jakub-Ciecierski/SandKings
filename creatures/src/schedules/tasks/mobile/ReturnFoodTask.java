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
import util.SmartConsole;
import util.SmartConsole.DebugModes;

public class ReturnFoodTask extends Task{

	private Mobile mobile;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		GOING_HOME,
		FINISH,
	}
	
	public ReturnFoodTask(Information information, Mobile mobile) {
		super(information);
		this.mobile = mobile;
		
		stage = Stages.BEGIN;
		
		SmartConsole.Print("Agent #" + mobile.getID() +" New ReturnFoodTask", DebugModes.TASK);
	}

	@Override
	public void execute() {
		if(stage == Stages.FINISH){
			finish();
			return;
		}

		GridPoint destPoint = information.getGridPoint();
		GridPoint currPoint = mobile.grid.getLocation(mobile);

		Food food = (Food)information.getAgent();
		
		// If food not there or is picked
		if(food == null || food.isPicked())  {
			stage = Stages.FINISH;
		}

		// moving towards destination
		if(stage == Stages.BEGIN) {
			mobile.moveTowards(destPoint);
		}
		
		// Arrived at food
		if(stage == Stages.BEGIN &&
				currPoint.getX() == destPoint.getX() && 
				currPoint.getY() == destPoint.getY())
		{
			List<Food> foodHere = mobile.FoodAtPoint( destPoint );
			
			if ( foodHere.size() > 0 ) {
				stage = Stages.GOING_HOME;
				mobile.PickUpFood( foodHere );
			}
			else{
				stage = Stages.FINISH;
			}
		}

		if(stage == Stages.GOING_HOME && mobile.carriedStuff == null){
			stage = Stages.FINISH;
			SmartConsole.Print("Agent #" + mobile.getID() +" Delivered Food", DebugModes.TASK);
		}
	}

}
