package schedules;

import schedules.tasks.Task;
import schedules.tasks.maw.EnemyNotifyTask;
import schedules.tasks.maw.NotifyTask;
import schedules.tasks.mobile.ReturnFoodTask;
import communication.knowledge.Information;
import communication.knowledge.KnowledgeBase;
import creatures.CreatureClasses.Maw;

public class MawScheduler extends Scheduler{

	private Maw maw;
	
	public MawScheduler(Maw maw) {
		this.maw = maw;
	}
	
	/**
	 * Creates a task based on the info
	 * @param info
	 * @return
	 */
	private Task createTask(Information info){
		switch(info.getType()){
			case FOOD:
				return new NotifyTask(info, this.maw);
			case ENEMY_CREATURE:
				return new EnemyNotifyTask(info, this.maw);
				
			default:
				return null;
		}
	}

	@Override
	public void updateSchulder() {
		KnowledgeBase knowledgeBase = maw.getKnowledgeBase();
		int knowledgeSize = knowledgeBase.getSize();

		for(int i = 0;i < knowledgeSize; i++){
			
			Information info = knowledgeBase.getInformation(i);
			
			if(info == null || !info.isUsefull)
				continue;
				
			Task currentTask = maw.getCurrentTask();
			
			// if current task is null or is finished or if new task
			// is more important.
			if(currentTask == null ||
					currentTask.isFinished() || 
					info.getType().getPriority() > currentTask.getInformation().getType().getPriority()){
				
				Task newTask = createTask(info);
				if(newTask != null)
					maw.setCurrentTask(newTask);
			}
		}
	}

}
