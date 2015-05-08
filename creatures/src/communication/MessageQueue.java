package communication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import creatures.CreatureClasses.MawFinder;
import repast.simphony.engine.schedule.ScheduledMethod;
/**
 * Keeps track of all messages in the queue
 * @author Kuba
 *
 */
public class MessageQueue {
	private static MessageQueue instance = new MessageQueue();
	public static MessageQueue Instance() { return instance; }
	private MessageQueue() { }
	
	Queue<MessagePacket> messageQueue = new LinkedList<MessagePacket>();
	
	public void addPacket(MessagePacket packet){
		synchronized(messageQueue){
			messageQueue.add(packet);	
		}
	}
	
	/**
	 * TODO remove busy waiting - step into function
	 * only when message are waiting
	 */
	@ScheduledMethod ( start = 1 , interval = 10)
	public void step(){
		MessagePacket packet = messageQueue.poll();
		if(packet!=null){
			packet.handle();
		}
	}
}
