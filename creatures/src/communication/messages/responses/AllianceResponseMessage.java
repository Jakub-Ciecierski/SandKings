package communication.messages.responses;

import schedules.tasks.Task;
import communication.Message;
import creatures.Agent;

public class AllianceResponseMessage extends Message {

	private Task task;
	
	private boolean doAlliance;
	
	public AllianceResponseMessage(Task task, boolean doAlliance){
	
		this.task = task;
		this.doAlliance = doAlliance;
		
	}
	
	@Override
	public void handle(Agent sender, Agent recipient) {
		
	}

}
