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
 * Starts a War against a target Maw.
 * 
 * An alliance is formed in order to deal as much damage as possible
 * 
 * @author Kuba
 *
 */
public class WarTask extends Task {

	public static int WAR_COUNT = 0;
	
	public static boolean IS_WAR = false;
	public static Object WAR_MUTEX = new Object();
	
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
	private boolean currentMawAnswered = true;
	private int[] mawIDMapper;
	private int[] allianceChecker;
	private int myAllianceIndex;
	
	private int allianceSize = 1;
	
	private Maw targetMaw;
	
	public WarTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
		this.targetMaw = (Maw)information.getAgent();
		
		SmartConsole.Print("Maw #" + maw.getID() +" New WarTask against Maw #" + targetMaw.getID(), DebugModes.WAR);
		
		init();
		
		WAR_COUNT++;
	}
	
	private void init(){
		// Set up alliance stuff
		List<Maw> maws = MawFinder.Instance().getMaws();
		int mawsCount = maws.size();
		
		mawIDMapper = new int[mawsCount];
		allianceChecker = new int[mawsCount];
		
		for(int i = 0;i < mawsCount; i++){
			Maw currMaw = maws.get(i);
			mawIDMapper[i] = currMaw.getPlayerID();
			
			if(targetMaw == currMaw)
				allianceChecker[i] = -2;
			else
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
		if ( targetMaw == null ) {
			stage = Stages.FINISH;
			return;
		}

		int neededBros = neededBros(targetMaw);
		
		allianceChecker[myAllianceIndex] = maw.getNumberOfFreeChildren();
		printCurrentAlliances();
		
		if(!allAnswered())
		{
			askForAlliance(neededBros - getAllianceBrosCount());
			
			return;
		}

		SmartConsole.Print("Maw #" + maw.getPlayerID() + " Finished creating Alliance... Starting Formations", DebugModes.TASK);
		
		createFormations();
		
		// create EvenInfo
		if(targetMaw != null) {
			GridPoint targetPoint = GSC.Instance().getGrid().getLocation(targetMaw);
			
			GSC.Instance().AddEventInfo(EventType.War, Constants.EVENT_ALLIANCE_TIMEOUT , 
					new GridPoint(targetPoint.getX() - Constants.EVENT_DISTANCE, targetPoint.getY() - Constants.EVENT_DISTANCE));
		}

		stage = Stages.FINISH;
	}

	private boolean allAnswered(){
		for(int i = 0;i < allianceChecker.length; i++){
			Maw allianceMaw = MawFinder.Instance().GetMaw(mawIDMapper[i]);
			if(allianceMaw == null){
				allianceChecker[i] = 0;
				continue;
			}
			if(allianceChecker[i] == -1){
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

			Alliance alliance = new Alliance(allianceFormations, fractions, true);
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

	private int neededBros(Maw targetMaw){
		int neededBros = targetMaw.getNumberOfChildren();
		
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
