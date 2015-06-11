package schedules;

import Constants.Constants;
import gov.nasa.worldwind.formats.tiff.GeoTiff.GCS;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.GridPoint;
import schedules.tasks.Task;
import schedules.tasks.maw.DefendTask;
import schedules.tasks.maw.EnemyTask;
import schedules.tasks.maw.FoodTask;
import schedules.tasks.maw.NotifyTask;
import schedules.tasks.maw.WarTask;
import schedules.tasks.mobile.ReturnFoodTask;
import sun.font.CreatedFontTracker;
import util.GSC;
import util.SimplyMath;
import communication.knowledge.Information;
import communication.knowledge.KnowledgeBase;
import creatures.Formation;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;

public class MawScheduler extends Scheduler{

	private Maw maw;
	
	public MawScheduler(Maw maw) {
		this.maw = maw;
	}
	
	/**
	 * Creates War task
	 * @param info
	 * @return
	 */
	private Task createWarTask(Information info){
		synchronized(WarTask.WAR_MUTEX){
			if(!WarTask.IS_WAR && 
					Constants.WAR_TICK_BLOCKADE < RunEnvironment.getInstance().getCurrentSchedule().getTickCount()){
				
				Maw targetMaw = (Maw)info.getAgent();
				
				Maw mostPowerfullMaw = MawFinder.Instance().getMostPowerfullMaw();
				if(mostPowerfullMaw != null){
					if(targetMaw == mostPowerfullMaw){
						WarTask.IS_WAR = true;
						return new WarTask(info, this.maw);
					}
				}
			}
			return null;
		}
	}
	
	private Task createDefendTask(Information info){
		Formation f = (Formation) info.getAgent();
		
		// Start task only if Formation is close enough
		if(f != null){
			GridPoint fGP = GSC.Instance().getGrid().getLocation(f);
			GridPoint mawGP = GSC.Instance().getGrid().getLocation(this.maw);
			
			if(fGP != null && mawGP != null)
				if(SimplyMath.Distance(fGP, mawGP) < Constants.MOBILE_VICINITY_X){
					return new DefendTask(info, this.maw);
				}
		}
		return null;
	}
	
	/**
	 * Creates a task based on the info
	 * @param info
	 * @return
	 */
	private Task createTask(Information info){
		switch(info.getType()){
			case FOOD:
				return new FoodTask(info, this.maw);
				
			case ENEMY_CREATURE:
				return new EnemyTask(info, this.maw);
			
			case ENEMY_FORMATION:
				return createDefendTask(info);
				
			case MAW:
				return createWarTask(info);
	
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
