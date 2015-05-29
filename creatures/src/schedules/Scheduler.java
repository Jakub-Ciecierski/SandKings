package schedules;

import communication.knowledge.KnowledgeBase;
import schedules.tasks.Task;

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

