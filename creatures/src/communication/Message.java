package communication;

import creatures.Agent;

/**
 * Base class of messages
 * @author Kuba
 *
 */
public abstract class Message {
	public abstract void handle(Agent sender, Agent recipient);
}
