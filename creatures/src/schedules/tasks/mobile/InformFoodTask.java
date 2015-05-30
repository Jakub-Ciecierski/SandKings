package schedules.tasks.mobile;

import java.util.List;

import map.Food;
import repast.simphony.space.grid.GridPoint;
import schedules.tasks.Task;
import Constants.Constants;
import communication.knowledge.Information;
import communication.messages.InformMessage;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import creatures.CreatureClasses.Mobile;

public class InformFoodTask extends Task {
	private Mobile mobile;
	
	private int stage = 0;
	
	public InformFoodTask (Information information, Mobile mobile) {
		super(information);
		this.mobile = mobile;
		
		System.out.println("*********************************************************");
		System.out.println("Agent #" + mobile.getID() +" New InfoFood: " + information.getType().toString());
		System.out.println("********************************************************* \n\n");
	
	}

	@Override
	public void execute() {
		if ( mobile.isInFormation() ) return;
		GridPoint currPoint = mobile.grid.getLocation(mobile);

		Maw maw = MawFinder.Instance().GetMaw(mobile.getPlayerID());
		
		GridPoint destPoint = mobile.grid.getLocation(maw);
		
		if(stage == 0)
		{
			//System.out.println("agent goin home");
			mobile.moveTowards(destPoint);
		}
		
		if(currPoint.getX() == destPoint.getX() && 
			currPoint.getY() == destPoint.getY() && stage == 0)
		{
			stage = 1;
			
			Information newInfo = new Information(
					information.getAgent(),
					information.getType(),
					information.getGetTickCount(),
					information.getGridPoint()
					);
			
			//InformMessage informMessage = new InformMessage(newInfo);
			//mobile.sendMessage(maw, informMessage);
			//System.out.println("    agent informed mother ");

			maw.getKnowledgeBase().addInformation(newInfo);
			
		}

		if(stage == 1){
			isFinished = true;
			information.isUsefull = false;
		}
	}

}
