package schedules.tasks.mobile;

import java.util.List;

import map.Food;
import Constants.Constants;
import communication.knowledge.Information;
import creatures.Agent;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import creatures.CreatureClasses.Mobile;
import creatures.CreatureClasses.Mobile.GoingWhere;
import repast.simphony.space.grid.GridPoint;
import schedules.tasks.Task;
import util.GSC;
import util.SmartConsole;
import util.SmartConsole.DebugModes;

public class ReturnFoodTask extends Task{

	private Mobile mobile;
	
	private Maw maw;
	
	private GridPoint mawPoint;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		GOING_HOME,
		DELIVER_FOOD,
		FINISH,
	}
	
	public ReturnFoodTask(Information information, Mobile mobile) {
		super(information);
		this.mobile = mobile;

		this.maw = MawFinder.Instance().GetMaw(mobile.getPlayerID());
		mawPoint = GSC.Instance().getGrid().getLocation(maw);
		
		stage = Stages.BEGIN;

		SmartConsole.Print("Agent #" + mobile.getID() +" New ReturnFoodTask", DebugModes.TASK_FOOD);
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
		if(food == null || (food.isPicked() && mobile.carriedStuff != null && mobile.carriedStuff != food)) {
			SmartConsole.Print("Agent #" + mobile.getID() +" TASK NOT RUNNING, FOOD PICKED", DebugModes.TASK_FOOD);
			stage = Stages.FINISH;
		}

		// moving towards destination - food
		if(stage == Stages.BEGIN) {
			mobile.moveTowards(destPoint);
		}
		
		// Arrived at food 
		if(stage == Stages.BEGIN &&
				currPoint.getX() == destPoint.getX() && 
				currPoint.getY() == destPoint.getY())
		{
			List<Food> foodHere = mobile.FoodAtPoint( destPoint );
			
			// pick it up
			if ( foodHere.size() > 0 ) {
				mobile.PickUpFood( foodHere );
				stage = Stages.GOING_HOME;
			}
			// or finish task if no food was found
			else{
				stage = Stages.FINISH;
			}
		}

		// return back home
		if(stage == Stages.GOING_HOME){
			mobile.moveTowards(mawPoint);

			if(currPoint.getX() == mawPoint.getX() && currPoint.getY() == mawPoint.getY()){
				mobile.DropCarriedFood();
				SmartConsole.Print("Agent #" + mobile.getID() +" Delivered Food", DebugModes.TASK_FOOD);
				stage = Stages.FINISH;
				
				//stage = Stages.DELIVER_FOOD;
			}
				
		}
		
		// deliver food
		if(stage == Stages.DELIVER_FOOD){
			mobile.DropCarriedFood();
			SmartConsole.Print("Agent #" + mobile.getID() +" Delivered Food", DebugModes.TASK_FOOD);
			stage = Stages.FINISH;
		}
	}

	@Override
	public void delayTask() {
		mobile.DropCarriedFoodOnTheGround();
		SmartConsole.Print("Agent #" + mobile.getID() +" Delay Task", DebugModes.TASK_FOOD);
	}

}
