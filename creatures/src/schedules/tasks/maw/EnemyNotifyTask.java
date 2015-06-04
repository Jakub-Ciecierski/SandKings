package schedules.tasks.maw;
import java.util.ArrayList;
import java.util.List;

import Enemies.Enemy;
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
import util.SmartConsole;
import util.SmartConsole.DebugModes;

public class EnemyNotifyTask extends Task {

	private Maw maw;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		FINISH,
		ALLIANCE
	}
	
	// ALLIANCE STUFF
	private static int MAW_COUNT = 4;
	private boolean currentMawAnswered = true;
	private int currentAskMawID = -1;
	private int[] mawIDMapper;
	private int[] allianceChecker;
	private boolean doAlliance;
	private int myAllianceIndex;
	
	public EnemyNotifyTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
		
		SmartConsole.Print("Maw #" + maw.getPlayerID() +" New EnemyNotifyTask: " + information.getType().toString(), DebugModes.TASK);
		
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
	
	/***************************************************/
	/******************* WPIERDOL  *********************/
	/***************************************************/

	private void goForWpierdol(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {
		Enemy enemy = (Enemy) information.getAgent();
		
		if ( enemy == null ) {
			stage = Stages.FINISH;
			return;
		}
		
		// TODO fix function
		int neededBros = (int) Math.ceil(
					( ( enemy.getHealth() * enemy.getDamage() ) / 
					( 
							( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
							Constants.Constants.MOBILE_HEALTH * Constants.Constants.MOBILE_ATTACK 
					) ) * 1.2f
				); 

		SmartConsole.Print(" danger: " + enemy.getHealth() * enemy.getDamage() + "   " + 
											" mobile danger: " +	 
											( Math.pow( 1 + maw.getStrength(), 2 ) ) * 
											Constants.Constants.MOBILE_HEALTH * Constants.Constants.MOBILE_ATTACK 
											+ "         needed bros: " + neededBros , DebugModes.TASK);
		
		neededBros = 20; // TODO fix magic number
		
		allianceChecker[myAllianceIndex] = maw.getNumberOfFreeChildren();
		
		printCurrentAlliances();
		
		if ( neededBros > getAllianceBrosCount() )
		{
			askForAlliance(neededBros - getAllianceBrosCount());
			
			return;
		}
		
		SmartConsole.Print("Maw #" + maw.getPlayerID() + " Finished creating Alliance... Starting Formations", DebugModes.TASK);
		
		/******* CRAETE FORMATION SECTION *********/
		
		List<Mobile> agents = new ArrayList<Mobile>();
		int max = Constants.Constants.BIGGEST_DISTANCE;
		int extent = 5;
		while ( agents.size() < neededBros )
		{
			agents = MawFinder.Instance().getFreeAgentsInVicinity(maw.getPlayerID(), neededBros, extent);
			extent += 1;
			
			if(extent > max)
				return;
			SmartConsole.Print("maw [" + agents.size() + "/" + neededBros + "]    c4attack bros in " + extent, DebugModes.BASIC);
		}
		
		GridPoint enemyPoint = grid.getLocation( enemy );
		if(enemyPoint == null) {
			stage = Stages.FINISH;
			return;
		}

		Formation f = new Formation( space, grid, maw.getPlayerID());
		
		context.add(f);
		f.setNeededSize( neededBros );
		NdPoint spacePt = space.getLocation(maw);
		GridPoint gridPt = grid.getLocation(maw);
		
		space.moveTo( f, spacePt.getX(), spacePt.getY());
		grid.moveTo(  f, gridPt.getX(),  gridPt.getY() );
		
		for ( Mobile m : agents )
		{
			f.addToFormation(m);
		}
		SmartConsole.Print("maw [" + agents.size() + "/" + neededBros + "]    added bros " + agents.size() + " to f #" + f.getID(), DebugModes.BASIC);
		
		f.setGoingSomewhere(true);
		f.setGoingWhere( Formation.GoingWhere.Wpierdol ); // what's the formation doing?
		f.setGoingPoint( enemyPoint ); // where's the food?

		SmartConsole.Print("atck formation " + f.getID() + " created at " + gridPt.getX() + ":" + gridPt.getY() + " for " + f.getNeededSize() + ".", DebugModes.BASIC);
		
		stage = Stages.FINISH;
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
		SmartConsole.Print("Maw #" + maw.getPlayerID() +" Current Alliances: \n" + alliancesStr, DebugModes.TASK);
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
		
		allianceChecker[mawIndex] = numberOfChildren;
		currentMawAnswered = true;
	}
}
