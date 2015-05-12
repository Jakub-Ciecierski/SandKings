package communication.messages.responses;
import communication.Message;
import creatures.Agent;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.Mobile;
public class FoodACK extends Message {	
	@Override
	public void handle(Agent sender, Agent recipient) {
		if ( recipient instanceof Mobile )
		{
			Mobile a = (Mobile) recipient;
			a.ReceiveFood();
		}
	}
}
