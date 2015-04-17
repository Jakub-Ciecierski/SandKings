package communication;

import jzombies.Agent;

/**
 * MessagePacket holds information about the entire message:
 * Its recipient, sender and the actual message content
 * 
 * @author Kuba
 *
 */
public class MessagePacket {
	
	private Agent sender;
	private Agent recipient;
	
	private Message message;
	
	public MessagePacket(Agent sender, Agent recipient, Message message){
		this.sender = sender;
		this.recipient = recipient;
		
		this.message = message;
	}
	
	/********************* GETTERS, SETTERS *********************/
	public Agent getRecipient() {
		return recipient;
	}

	public void setRecipient(Agent recipient) {
		this.recipient = recipient;
	}

	public Agent getSender() {
		return sender;
	}
	
	public void setSender(Agent sender) {
		this.sender = sender;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}
	
	/********************* PUBLIC METHODS *********************/
	public void handle(){
		this.message.handle(this.sender, this.recipient);
	}
}
