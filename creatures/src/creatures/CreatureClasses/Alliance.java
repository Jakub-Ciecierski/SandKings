package creatures.CreatureClasses;

import java.util.ArrayList;
import java.util.List;

import creatures.Formation;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import schedules.tasks.maw.WarTask;
import util.GSC;
import util.SmartConsole;
import util.SmartConsole.DebugModes;
import Constants.Constants;

/**
 * Class responsible for Alliance Logic.
 * Controls who is still alive in alliance.
 * Controls when the Alliance is finished. 
 * 
 * @author Kuba
 *
 */
public class Alliance {
	
	private static int allianceCount = 0; 

	private double timeoutStart = -1;
	
	private List<Formation> allianceFormations = new ArrayList<Formation>();
	
	private List<Integer> fractions = new ArrayList<Integer>();
	
	private boolean isWar = false;
	
	public Alliance (List<Formation> allianceFormations, List<Integer> fractions) {
		this.allianceFormations = allianceFormations;
		this.fractions = fractions;
		
		makeAlliance();
		
		printStatistics();
	}
	
	public Alliance (List<Formation> allianceFormations, List<Integer> fractions, boolean isWar) {
		this.allianceFormations = allianceFormations;
		this.fractions = fractions;
		
		this.isWar = isWar;
		
		makeAlliance();
		
		printStatistics();
	}
	
	private static void printStatistics(){
		SmartConsole.Print("Current Total Alliances Count: " + allianceCount++, DebugModes.STDOUT);
	}
	
	/*
	 * We can add everything God will be doing here - dropping food, creatures etc.
	 * */
	@ScheduledMethod ( start = Constants.ALLIANCE_START , interval = Constants.ALLIANCE_TICK)
	public void step()
	{
		//SmartConsole.Print("Alliance Tick", DebugModes.ALLIANCE);
		if(isAllianceDone() && timeoutStart == -1) {
			timeoutStart = 	RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
			SmartConsole.Print("Alliance Timeout tick: " + timeoutStart , DebugModes.ALLIANCE);
		}
		
		if(timeoutStart > 0){
			if(RunEnvironment.getInstance().getCurrentSchedule().getTickCount() > Constants.ALLIANCE_TIMEOUT + timeoutStart ){
				stopAlliance();
				
				GSC.Instance().getContext().remove(this);
			}
		}
		
	}
	
	private void makeAlliance() {
		
		String msg = "Making an Alliances. Fractions: \n";
		for(int i =0;i < fractions.size(); i++){
			msg += fractions.get(i) + "\n";
		}
		
		for(int i =0;i < fractions.size(); i++){
			for(int j =0; j < fractions.size(); j++){
				if(fractions.get(i) != fractions.get(j) ){
					MawFinder.Instance().makeAlliance(fractions.get(i), fractions.get(j));
				}
			}
		}
		
		SmartConsole.Print(msg, DebugModes.ALLIANCE);
	}
	
	private void stopAlliance() {
		
		if(isWar){
			synchronized (WarTask.WAR_MUTEX) {
				WarTask.IS_WAR = false;				
			}
			
			SmartConsole.Print("War Finished", DebugModes.WAR);
		}
		SmartConsole.Print("Alliance Finished", DebugModes.ALLIANCE);
		
		for(int i =0;i < fractions.size(); i++){
			for(int j =0; j < fractions.size(); j++){
				if(fractions.get(i) != fractions.get(j) ){
					MawFinder.Instance().makeWar(fractions.get(i), fractions.get(j));
				}
			}
		}
	}

	private boolean isAllianceDone(){
		synchronized(allianceFormations){
			int disbandCount = 0;
			
			for(int i =0;i < allianceFormations.size(); i++){
				Formation f = allianceFormations.get(i);
				if(f == null || f.isDisbanded())
					disbandCount++;
					
			}
			
			if(disbandCount == allianceFormations.size())
				return true;
			else
				return false;
		}
	}
	
}
