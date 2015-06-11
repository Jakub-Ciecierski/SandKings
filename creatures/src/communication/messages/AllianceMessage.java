package communication.messages;

import schedules.tasks.Task;
import schedules.tasks.maw.EnemyTask;
import schedules.tasks.maw.WarTask;
import util.SmartConsole;
import util.SmartConsole.DebugModes;
import communication.Message;
import communication.knowledge.Information;
import creatures.Agent;
import creatures.CreatureClasses.Maw;

public class AllianceMessage extends Message {

	private Task task;
	
	private int neededBros;

	public AllianceMessage(Task task, int neededBros) {
		this.task = task;
		
		this.neededBros = neededBros;
	}
	
	@Override
	public void handle(Agent sender, Agent recipient) {
		SmartConsole.Print("Agent #" + recipient.getID() + " received AllianceMessage", DebugModes.MESSAGE);
		
		Maw maw = (Maw)recipient;
		boolean doAlliance;
		int numberOfFreeBros;
		
		// Check number of free children
		// If yes Answer with number of Bros > 0
		// if No answer with number of Bros == 0
		
		// If maw is already doing the agent of alliance interest, deny
		if(maw.getCurrentTask() != null &&
				maw.getCurrentTask().getInformation().getAgent() == task.getInformation().getAgent()){
			numberOfFreeBros = 0;
		}
		else{
			//numberOfFreeBros = maw.getNumberOfFreeChildren();
			numberOfFreeBros = maw.getNumberOfChildren();
		}
			
		doAlliance = numberOfFreeBros > 0;

		if(doAlliance) {
			// Add him to cache, so don't this Maw does not start other tasks with this Agent
			Information currInfo = task.getInformation();
			Information newInfo = new Information(currInfo.getAgent(), currInfo.getType(), 
								currInfo.getTickCount(), currInfo.getGridPoint());
			
			maw.getKnowledgeBase().addToCache(task.getInformation().getAgent());
			//maw.getKnowledgeBase().addInformation(newInfo);
			
			SmartConsole.Print("Agent #" + recipient.getID() + " Agreed For Alliance with " + numberOfFreeBros + " Mobiles", DebugModes.MESSAGE);
		}
		else
			SmartConsole.Print("Agent #" + recipient.getID() + " Decliend Alliance", DebugModes.MESSAGE);
		
		if(task instanceof WarTask){
			WarTask warTask = (WarTask)this.task;
			warTask.Answer(numberOfFreeBros, maw.getPlayerID());
		}
		else if(task instanceof EnemyTask){
			EnemyTask enemyNotifyTask = (EnemyTask)this.task;
			enemyNotifyTask.Answer(numberOfFreeBros, maw.getPlayerID());
		}		
	}

}
