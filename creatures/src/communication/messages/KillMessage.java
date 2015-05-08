package communication.messages;

import communication.Message;
import creatures.Agent;
import creatures.CreatureClasses.Mobile;

public class KillMessage extends Message{

	
	public KillMessage()
	{
	}
	

	@Override
	public void handle(Agent sender, Agent recipient) {
		System.out.println("*********************************************************");
		System.out.println("Agent #" + sender.getID() +" To Agent #" +recipient.getID() + " KillMessage \n");
		System.out.println("DEATH \n");
		System.out.println("********************************************************* \n\n");

		Mobile mobile = (Mobile)recipient;
		mobile.Delete();
	}
}
