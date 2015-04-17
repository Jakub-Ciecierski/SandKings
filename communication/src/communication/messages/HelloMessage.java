package communication.messages;

import jzombies.Agent;
import communication.Message;

public class HelloMessage extends Message{
	
	String content;
	
	public HelloMessage(String content){
		this.content = content;
	}

	@Override
	public void handle(Agent sender, Agent recipient) {
		System.out.println("*********************************************************");
		System.out.println("Agent #" + sender.getID() +" To Agent #" +recipient.getID() + " Response \n");
		System.out.println("Content: \n");
		System.out.println(content);
		System.out.println("********************************************************* \n\n");
	}
}
