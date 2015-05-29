package schedules.tasks.mobile;

import java.util.List;

import map.Food;
import repast.simphony.space.grid.GridPoint;
import schedules.tasks.Task;
import Constants.Constants;
import communication.knowledge.Information;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import creatures.CreatureClasses.Mobile;

public class InformFoodTask extends Task {
	private Mobile mobile;
	
	private int stage = 0;
	
	public InformFoodTask (Information information, Mobile mobile) {
		super(information);
		this.mobile = mobile;
	}

	@Override
	public void execute() {
		GridPoint currPoint = mobile.grid.getLocation(mobile);

		Maw maw = MawFinder.Instance().GetMaw(mobile.getPlayerID());
		
		GridPoint destPoint = mobile.grid.getLocation(maw);
		
		if(stage == 0)
			mobile.moveTowards(destPoint);
		
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
			
			maw.getKnowledgeBase().addInformation(newInfo);
			
		}

		if(stage == 1){
			isFinished = true;
			information.isUsefull = false;
		}
	}

}
