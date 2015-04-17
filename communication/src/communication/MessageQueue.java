package communication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import repast.simphony.engine.schedule.ScheduledMethod;
/**
 * Keeps track of all messages in the queue
 * @author Kuba
 *
 */
public class MessageQueue {
	Queue<MessagePacket> messageQueue = new LinkedList<MessagePacket>();

	public MessageQueue(){
		
	}
	
	public void addPacket(MessagePacket packet){
		messageQueue.add(packet);
	}
	
	/**
	 * TODO remove busy waiting - step into function
	 * only when message are waiting
	 */
	@ScheduledMethod ( start = 1 , interval = 1)
	public void step(){
		MessagePacket packet = messageQueue.poll();
		if(packet!=null){
			packet.handle();
		}
	}
}
