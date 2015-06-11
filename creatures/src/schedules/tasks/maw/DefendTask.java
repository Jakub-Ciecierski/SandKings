package schedules.tasks.maw;

import java.util.List;

import bsh.This;
import map.EventType;
import Constants.Constants;
import communication.knowledge.Information;
import creatures.Agent;
import creatures.Formation;
import creatures.CreatureClasses.FormationCreator;
import creatures.CreatureClasses.FormationCreator.FormationTypes;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.Worker;
import repast.simphony.space.grid.GridPoint;
import schedules.tasks.Task;
import util.GSC;
import util.SmartConsole;
import util.SmartConsole.DebugModes;

/**
 * Maw orders all its children to form an Formation and defend its base
 * @author Kuba
 *
 */
public class DefendTask extends Task {

	private Maw maw;
	
	public DefendTask(Information information, Maw maw) {
		super(information);

		this.maw = maw;
		
		SmartConsole.Print("Agent #" + maw.getID() +" New DefendTask: " + information.getType().toString(), DebugModes.WAR);
	}

	@Override
	public void execute() {
		
		Agent agent = information.getAgent();
		if(agent == null){
			this.finish();
			return;
		}
		GridPoint gp = GSC.Instance().getGrid().getLocation(agent);
		
		FormationCreator formationCreator = new FormationCreator(maw, 
				maw.getNumberOfChildren(), 
				Formation.GoingWhere.Wpierdol,
				gp);
		
		formationCreator.setType(FormationTypes.LOOSE);
		
		maw.addPendingFormation(formationCreator);
		
		GridPoint targetPoint = GSC.Instance().getGrid().getLocation(this.maw);
		if(targetPoint != null)
			GSC.Instance().AddEventInfo(EventType.Defense, Constants.EVENT_ALLIANCE_TIMEOUT , 
					new GridPoint(targetPoint.getX() - Constants.EVENT_DISTANCE, targetPoint.getY() - Constants.EVENT_DISTANCE));
		
		this.finish();
		
		return;
	}

	@Override
	public void delayTask() {
		// TODO Auto-generated method stub
		
	}

}
