package communication.messages;

import schedules.tasks.Task;
import communication.Message;
import creatures.Agent;

public class AllianceMessage extends Message {

	// Task responsible for creating alliance 
	private Task task;
	
	public AllianceMessage(Task task) {
		this.task = task;
	}
	
	@Override
	public void handle(Agent sender, Agent recipient) {
		// Check number of free children
		
		// If yes send AllianceAck
		//boolean
	}

}
