package schedules.tasks.maw;
import communication.knowledge.Information;
import creatures.CreatureClasses.Maw;
import schedules.tasks.Task;

public class FoodNotifyTask extends Task {

	private Maw maw;
	
	public FoodNotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
	}

	@Override
	public void execute() {


		isFinished = true;
		information = null;
	}

}
