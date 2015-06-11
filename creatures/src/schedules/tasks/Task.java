package schedules.tasks;

import communication.knowledge.Information;

import creatures.Agent;

/**
 * The abstract class of Task.
 * Task is responsible for current behaviour of a given Agent.
 * 
 * Task is set up in the Scheduler.
 * 
 * @author Kuba
 *
 */
public abstract class Task {
	
	protected boolean isFinished = false;
	
	protected Information information;
	
	public Task(Information information){
		this.information = information;
	}
	
	public abstract void execute();
	
	public boolean isFinished(){
		return this.isFinished;
	}
	
	public void setFinished(boolean finished){
		this.isFinished = finished;
	}
	
	public Information getInformation(){
		return this.information;
	}
	
	protected void finish(){
		isFinished = true;
		information.isUsefull = false;
	}
	
	public abstract void delayTask();
}
