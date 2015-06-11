package schedules;

import communication.knowledge.KnowledgeBase;
import schedules.tasks.Task;

/**
 * Scheduler distributes Tasks to Agent based on its Knowledge Base.
 * 
 * @author Kuba
 *
 */
public abstract class Scheduler {
	
	public Scheduler(){
	}
	
	/**
	 * Updates current task.
	 * Changes current task if task is null or
	 * other task has higher priority
	 */
	public abstract void updateSchulder();

}

