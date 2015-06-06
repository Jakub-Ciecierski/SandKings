package schedules;

import map.Food;
import schedules.tasks.Task;
import schedules.tasks.mobile.InformEnemyTask;
import schedules.tasks.mobile.InformFoodTask;
import schedules.tasks.mobile.ReturnFoodTask;
import communication.knowledge.Information;
import communication.knowledge.InformationType;
import communication.knowledge.KnowledgeBase;
import creatures.Agent;
import creatures.CreatureClasses.Mobile;

public class MobileScheduler extends Scheduler{

	private Mobile mobile;
	
	public MobileScheduler(Mobile mobile) {
		this.mobile = mobile;
	}

	/**
	 * Creates a task based on the info
	 * @param info
	 * @return
	 */
	private Task createTask(Information info){
		switch(info.getType()){
			case FOOD:
				
				Agent agent = info.getAgent();
				if(agent instanceof Food){
					Food food = (Food)agent;	
					
					if(food.getWeight() > mobile.getCarryCapacity())
						return new InformFoodTask(info, this.mobile);
					else
						return new ReturnFoodTask(info, this.mobile);

				}
			case ENEMY_CREATURE:
				return new InformEnemyTask(info, mobile);
				
			default:
				return null;
		}
	}
	
	private boolean isFoodTaken(Information info){
		if(info.getAgent() instanceof Food){
			Food food = (Food) info.getAgent();
			if(food == null || food.isPicked())
				return true;
		}
		return false;
	}
	
	@Override
	public void updateSchulder() {
		KnowledgeBase knowledgeBase = mobile.getKnowledgeBase();
		int knowledgeSize = knowledgeBase.getSize();

		for(int i = 0;i < knowledgeSize; i++){
			
			Information info = knowledgeBase.getInformation(i);
			
			if(info == null || !info.isUsefull || isFoodTaken(info))
				continue;
				
			Task currentTask = mobile.getCurrentTask();
			
			// if current task is null or is finished or if new task
			// is more important.
			if(currentTask == null ||
					currentTask.isFinished() || 
					info.getType().getPriority() > currentTask.getInformation().getType().getPriority()){
				
				Task newTask = createTask(info);
				if(newTask != null) {
					
					// If current Task is running, delay it
					if(currentTask != null && !currentTask.isFinished())
						currentTask.delayTask();
					
					mobile.setCurrentTask(newTask);
				}
			}
		}
		
		
	}

}
