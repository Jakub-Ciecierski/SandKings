package schedules.tasks.maw;
import java.util.ArrayList;
import java.util.List;

import Constants.Constants;
import Enemies.Enemy;
import map.EventType;
import map.Food;
import communication.knowledge.Information;
import communication.messages.AllianceMessage;
import creatures.Formation;
import creatures.CreatureClasses.*;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import schedules.tasks.Task;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import util.GSC;
import util.SmartConsole;
import util.SmartConsole.DebugModes;

/**
 * Task to destroy an Enemy Agent
 * 
 * If given Maw does not have enough children to deal with the problem,
 * an alliance request is done.
 * @author Kuba
 *
 */
public class EnemyTask extends Task {

	private Maw maw;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		FINISH,
		RESTART,
		FORM_ALLIANCE,
		FORM_FORMATION
	}
	
	// ALLIANCE STUFF
	private static int MAW_COUNT = 4;
	private boolean currentMawAnswered = true;
	private int currentAskMawID = -1;
	private int[] mawIDMapper;
	private int[] allianceChecker;
	private boolean doAlliance;
	private int myAllianceIndex;
	
	private int allianceSize = 1;
	
	public EnemyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
		
		SmartConsole.Print("Maw #" + maw.getID() +" New EnemyNotifyTask: " + information.getType().toString(), DebugModes.TASK);
		
		init();
		
	}
	
	private void init(){
		// Set up alliance stuff
		List<Maw> maws = MawFinder.Instance().getMaws();
		int mawsCount = maws.size();
		
		mawIDMapper = new int[mawsCount];
		allianceChecker = new int[mawsCount];
		
		for(int i = 0;i < mawsCount; i++){
			mawIDMapper[i] = maws.get(i).getPlayerID();
			allianceChecker[i] = -1;
			
			if(mawIDMapper[i] == maw.getPlayerID())
				myAllianceIndex = i;
		}
		
		currentMawAnswered = true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		if ( stage == Stages.FINISH ) 
		{
			finish();
			return;
		} 
		else if ( stage == Stages.BEGIN )
		{
			// SPAWN FORMATION FOR THE JOB
			Context<Object> context = ContextUtils.getContext(maw);
			ContinuousSpace<Object> space = maw.getSpace(); 
			Grid<Object> grid = maw.getGrid();
			
			goForWpierdol( context, space, grid );
		}
		
	}

	/**
	 * Main logic of Going For wpierdol task
	 * @param context
	 * @param space
	 * @param grid
	 */
	private void goForWpierdol(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {
		Enemy enemy = (Enemy) information.getAgent();
		
		if ( enemy == null ) {
			stage = Stages.FINISH;
			return;
		}

		int neededBros = neededBros(enemy);
		neededBros = 20; // TODO fix magic number
		
		allianceChecker[myAllianceIndex] = maw.getNumberOfFreeChildren();
		printCurrentAlliances();
		
		if ( neededBros > getAllianceBrosCount() )
		{
			askForAlliance(neededBros - getAllianceBrosCount());
			
			if(allAnswered()){
				SmartConsole.Print("All Maws answered, not enough mobiles, restarting...", DebugModes.ALLIANCE);
				init();
			}

			return;
		}
		
		SmartConsole.Print("Maw #" + maw.getPlayerID() + " Finished creating Alliance... Starting Formations", DebugModes.TASK);
		
		createFormations();

		stage = Stages.FINISH;
	}

	private boolean allAnswered(){
		for(int i = 0;i < allianceChecker.length; i++){
			Maw allianceMaw = MawFinder.Instance().GetMaw(mawIDMapper[i]);
			if(allianceMaw == null){
				allianceChecker[i] = 0;
				continue;
			}
			if(allianceChecker[i] < 0){
				return false;
			}
		}
		return true;
	}
	
	private int getAllianceBrosCount(){
		int count = 0;
		for(int i = 0;i < allianceChecker.length; i++){
			if(allianceChecker[i] > 0){
				count += allianceChecker[i];
			}
		}
		return count;
	}
	
	private void printCurrentAlliances(){
		String alliancesStr = "";
		for(int i = 0 ; i < allianceChecker.length; i++){
			alliancesStr += "\tMaw #" + mawIDMapper[i] + " Army Count: " + allianceChecker[i];
			alliancesStr += "\n";
		}
		SmartConsole.Print("Maw #" + maw.getPlayerID() +" Current Alliances to kill Enemy #:" + 
								information.getAgent().getID() +"\n" + alliancesStr, DebugModes.TASK);
	}

	private boolean askForAlliance(int neededBros){
		// TODO Choose next strongest instead of simply next one
		int mawToAsk;
		for(int i = 0; i < allianceChecker.length; i++){
			Maw allianceMaw = MawFinder.Instance().GetMaw(mawIDMapper[i]);
			
			// if Maw is dead, mark him as 0 and continue looking
			if(allianceMaw == null){
				allianceChecker[i] = 0;
				continue;
			}
			
			// if it is not me && 
			// previously asked Maw has already asked, choose another &&
			// if it was not asked before == -1	
			if(mawIDMapper[i] != maw.getPlayerID() && currentMawAnswered && allianceChecker[i] == -1){
				
				currentMawAnswered = false;
				AllianceMessage allianceMsg = new AllianceMessage(this, neededBros);
				
				this.maw.sendMessage(allianceMaw, allianceMsg);

				SmartConsole.Print("Maw #" + maw.getPlayerID() +" Sending AllianceMessage to Maw #: "+ allianceMaw.getPlayerID(), DebugModes.TASK);
				
				// wait for answer
				return true;
			}
		}
		
		return false;
	}
	
	private boolean createFormations(){
		
		List<Integer> fractions= new ArrayList<Integer>();
		for(int i = 0;i < allianceChecker.length; i++){
			if(allianceChecker[i] > 0) {
				Maw allianceMaw = MawFinder.Instance().GetMaw(mawIDMapper[i]);
				if(allianceMaw == null)
					allianceChecker[i] = -1;
				else
					fractions.add(mawIDMapper[i]);
			}
		}
		
		boolean isAlliance = fractions.size() > 1; 
		
		List<Formation> allianceFormations = null;
		
		if(isAlliance) {
			allianceFormations = new ArrayList<Formation>();

			Alliance alliance = new Alliance(allianceFormations, fractions);
			GSC.Instance().getContext().add(alliance);
		}

		for(int i =0;i < allianceChecker.length; i++){
			if(allianceChecker[i] > 0){
				Maw allianceMaw = MawFinder.Instance().GetMaw(mawIDMapper[i]);

				FormationCreator fCreator = new FormationCreator(allianceMaw,allianceChecker[i], 
																	Formation.GoingWhere.Wpierdol, information.getGridPoint(),
																	allianceFormations);
			
				allianceMaw.addPendingFormation(fCreator);
				
				if(isAlliance){
					GSC.Instance().AddEventInfo(EventType.Alliance, Constants.EVENT_ALLIANCE_TIMEOUT , 
							new GridPoint(allianceMaw.getGridpos().getX() - Constants.EVENT_DISTANCE, allianceMaw.getGridpos().getY() - Constants.EVENT_DISTANCE));
				}
			}
		}
		
		return true;
	}

	private int neededBros(Enemy enemy){
		// TODO fix function
		int neededBros = (int) Math.ceil(
					( ( enemy.getHealth() * enemy.getDamage() ) / 
					( 
							( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
							Constants.MOBILE_HEALTH * Constants.MOBILE_ATTACK 
					) ) * 1.2f
				); 

		SmartConsole.Print(" danger: " + enemy.getHealth() * enemy.getDamage() + "   " + 
											" mobile danger: " +	 
											( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
											Constants.MOBILE_HEALTH * Constants.MOBILE_ATTACK 
											+ "         needed bros: " + neededBros , DebugModes.TASK);
		return neededBros;
	}
	
	public void Answer(int numberOfChildren, int mawID) {
		int mawIndex = -1;
		for(int i = 0;i < mawIDMapper.length; i++){
			if(mawIDMapper[i] == mawID){
				mawIndex = i;
				break;
			}
		}
		if(mawIndex == -1){
			SmartConsole.Print("No such mawID", DebugModes.ERROR);
		}
	
		if(numberOfChildren > 0)
			allianceSize++;
		
		allianceChecker[mawIndex] = numberOfChildren;
		currentMawAnswered = true;
	}

	@Override
	public void delayTask() {
		// TODO Auto-generated method stub
		
	}
}
