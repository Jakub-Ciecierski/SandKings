package schedules.tasks.mobile;

import Constants.Constants;
import communication.knowledge.Information;
import creatures.Agent;
import creatures.CreatureClasses.Mobile;
import schedules.tasks.Task;

public class ReturnFoodTask extends Task{

	private Mobile mobile;
	
	public ReturnFoodTask(Information information, Mobile mobile) {
		super(information);
		this.mobile = mobile;
		
		if(Constants.DEBUG_MODE){
			System.out.println("*********************************************************");
			System.out.println("Agent #" + mobile.getID() +" New ReturnFoodTask");
			System.out.println("********************************************************* \n\n");
		}
	}

	@Override
	public void execute() {
		// DO WORK

		// finish the task
		isFinished = true;
		information = null;
	}

}
