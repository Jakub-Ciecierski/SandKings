package communication.messages;

import schedules.tasks.Task;
import schedules.tasks.maw.EnemyNotifyTask;
import util.SmartConsole;
import util.SmartConsole.DebugModes;
import communication.Message;
import communication.messages.responses.AllianceResponseMessage;
import creatures.Agent;

public class AllianceMessage extends Message {

	// Task responsible for creating alliance 
	private EnemyNotifyTask task;
	
	public AllianceMessage(EnemyNotifyTask task) {
		this.task = task;
		
	}
	
	@Override
	public void handle(Agent sender, Agent recipient) {
		SmartConsole.Print("Agent #" + recipient.getID() + " received AllianceMessage", DebugModes.MESSAGE);
		
		boolean doAlliance;
		
		// Check number of free children
		// ... doAlliance = true ? false
		// If yes send AllianceAck
		
		doAlliance = true;
		
		if(doAlliance)
			SmartConsole.Print("Agent #" + recipient.getID() + " Agreed For Alliance", DebugModes.MESSAGE);
		else
			SmartConsole.Print("Agent #" + recipient.getID() + " Decliend Alliance", DebugModes.MESSAGE);
		
		// this.task.Notify
		
	}

}
