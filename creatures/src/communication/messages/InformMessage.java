package communication.messages;

import communication.Message;
import communication.knowledge.Information;
import creatures.Agent;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.Mobile;

public class InformMessage extends Message {

	private Information info;
	
	public InformMessage(Information info){
		this.info = info;
	}
	
	@Override
	public void handle(Agent sender, Agent recipient) {
		
		if(recipient instanceof Maw ){
			Maw maw = (Maw)recipient;
			
			maw.getKnowledgeBase().addInformation(info);
		}
		else if(recipient instanceof Mobile ){
			Mobile mobile = (Mobile)recipient;
			
			mobile.getKnowledgeBase().addInformation(info);
		}
		
	}

}
