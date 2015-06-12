package schedules.tasks.maw;
import java.util.ArrayList;
import java.util.List;

import Enemies.Enemy;
import map.Food;
import communication.knowledge.Information;
import creatures.Formation;
import creatures.CreatureClasses.*;
import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import schedules.tasks.Task;
import util.SmartConsole;
import util.SmartConsole.DebugModes;

public class FoodTask extends Task {

	private Maw maw;
	
	private Stages stage = Stages.BEGIN;
	
	private enum Stages {
		BEGIN,
		DELIVERY,
		FINISH
	}
	
	public FoodTask(Information information, Maw maw) {
		super(information);
		
		this.maw = maw;
			
		SmartConsole.Print("Agent #" + maw.getID() +" New FoodNotifyTask: Food#" + information.getAgent().getID() + "*************", DebugModes.ADVANCED);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void execute() {
		if ( stage == Stages.FINISH ) 
		{
			// Finish is currently done inside Formation - ActOnArrival - HomeWithFood
			finish();
			return;
		} 
		else if ( stage == Stages.BEGIN )
		{
			// SPAWN FORMATION FOR THE JOB
			
			Context<Object> context = ContextUtils.getContext(maw);
			ContinuousSpace<Object> space = maw.getSpace(); 
			Grid<Object> grid = maw.getGrid();
			
			goForFood( context, space, grid );
		}
		
	}
	
	private void goForFood(Context<Object> context, 
			ContinuousSpace<Object> space, Grid<Object> grid) {

		Food food = (Food) information.getAgent();
		
		if ( food == null ) {
			stage = Stages.FINISH;
			return;
		}
		if(stage == Stages.DELIVERY && food.isDelivered()){
			stage = Stages.FINISH;
			return;
		}
		
		int neededBros = food.getWeight();
		
		FormationCreator formationCreator = new FormationCreator(maw, 
												neededBros, 
												Formation.GoingWhere.ForFood,
												information.getGridPoint());
		//formationCreator.setTask(this);
		
		maw.addPendingFormation(formationCreator);

		stage = Stages.FINISH;
	}
/*
	@Override
	public void finish() {
		this.isFinished = true;
	}
	*/
	@Override
	public void delayTask() {
		// TODO Auto-generated method stub
		
	}

}
