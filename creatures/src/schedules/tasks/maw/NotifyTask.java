package schedules.tasks.maw;
import communication.knowledge.Information;
import creatures.CreatureClasses.Maw;
import schedules.tasks.Task;

public class NotifyTask extends Task {

	private Maw maw;
	
	public NotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
		

		System.out.println("*********************************************************");
		System.out.println("Agent #" + maw.getID() +" New NotifyTask: " + information.getType().toString());
		System.out.println("********************************************************* \n\n");
	
		
	}

	@Override
	public void execute() {
		
		
		isFinished = true;
		information.isUsefull = false;
	}

}
