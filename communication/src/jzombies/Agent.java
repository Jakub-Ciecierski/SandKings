package jzombies;

import communication.Message;
import communication.MessageHandler;
import communication.MessagePacket;
import communication.MessageQueue;
import repast.simphony.space.grid.GridPoint;

public abstract class Agent {
	protected int id; 
	
	protected MessageQueue messageQueue;
	
	public int getID(){
		return id;
	}
	
	public void sendMessage(Agent agent, Message message){
		MessagePacket packet = new MessagePacket(this, agent, message);
		
		messageQueue.addPacket(packet);
	}

}
