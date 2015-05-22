package communication.knowledge;

import communication.MessageQueue;
import creatures.Agent;

public abstract class Interpreter {
	/**
	 * Interprets all information
	 * @param info
	 * @param agent
	 */
	public abstract void interpret(Information info, Agent agent);
}
