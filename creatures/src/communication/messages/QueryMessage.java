package communication.messages;

import creatures.Agent;
import communication.Message;

public class QueryMessage extends Message{
	
	String content;
	
	public QueryMessage(String content)
	{
		this.content = content;
	}
	
	public String getContent(){
		return this.content;
	}

	@Override
	public void handle(Agent sender, Agent recipient) {
		System.out.println("*********************************************************");
		System.out.println("Agent #" + sender.getID() +" To Agent #" +recipient.getID() + " QueryMessage \n");
		System.out.println("Content: \n");
		System.out.println(content);
		System.out.println("********************************************************* \n\n");
		
		// send back a response
		HelloMessage helloMessage = new HelloMessage("Yes, I do love you");
		recipient.sendMessage(sender, helloMessage);
	}
}
