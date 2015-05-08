package communication;

import communication.messages.QueryMessage;

/**
 * This class is used to handle all the incoming messages
 * 
 * @author Kuba
 *
 */
public class MessageHandler {
	public MessageHandler(){
		
	}
	
	private void handleQuery(QueryMessage msg){
		System.out.println(msg.getContent());
	}
	
	public void handle(Message message){
		if(message instanceof QueryMessage){
			handleQuery((QueryMessage)message);
		}
	}
}
