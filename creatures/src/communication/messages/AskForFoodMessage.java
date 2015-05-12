package communication.messages;

import communication.Message;
import communication.messages.responses.FoodACK;
import communication.messages.responses.FoodNOPE;
import creatures.Agent;
import creatures.CreatureClasses.Maw;

public class AskForFoodMessage extends Message {
	String content;
	
	public AskForFoodMessage(String content)
	{
		this.content = content;
	}
	
	public String getContent(){
		return this.content;
	}
	
	@Override
	public void handle(Agent sender, Agent recipient) {
		// TODO Auto-generated method stub
		if ( recipient.getClass() != Maw.class )
		{
			return;
		}
		Maw m = (Maw) recipient;
		if ( m.hasFood() )
		{
			FoodACK msg = new FoodACK();
			recipient.sendMessage(sender, msg);
		}
		else
		{
			FoodNOPE msg = new FoodNOPE();
			recipient.sendMessage(sender, msg);
		}
	}

}
