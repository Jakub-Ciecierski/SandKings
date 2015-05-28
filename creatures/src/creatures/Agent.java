package creatures;

import communication.Message;
import communication.MessagePacket;
import communication.MessageQueue;

/**
 *	The base class of all agents
 *	In order to communicate, each agent
 *	has to inherit from this class
 * @author kuba
 *
 */
public abstract class Agent {
	static private int nextID = 0;
	protected int id; 
	
	public Agent(){
		this.id = nextID++;
		//System.out.println("Agent #"+this.id+" created");
	}
	
	public int getID(){
		return id;
	}
	
	public void sendMessage(Agent agent, Message message){
		MessagePacket packet = new MessagePacket(this, agent, message);
		MessageQueue.Instance().addPacket(packet);	
		
	}

}
