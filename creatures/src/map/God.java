/**
 * 
 */
package map;

import java.util.ArrayList;
import java.util.List;

import creatures.Fightable;
import creatures.CreatureClasses.Alliance;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import schedules.MawScheduler;
import util.GSC;
import util.SmartConsole;
import util.SmartConsole.DebugModes;
import Constants.Constants;
import Enemies.Enemy;


/**
 * @author Viet Ba
 * Class for God
 */
public class God {

	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	private static int deadMawCounter = 0;
	private static List<Integer> mawIDlist = new ArrayList<>();
	private static int pizza = 0, donut = 0, grape = 0, cabbage = 0;
	private static int scorpion = 0, snake = 0, spider = 0;
	private static List<Maw> mawList = new ArrayList();
	public God (ContinuousSpace<Object> space, Grid<Object> grid) { 
		this.space = space;
		this.grid = grid;
		for (int i = 1; i <= 4; i++)
				mawIDlist.add(i);
	}
	
	/*
	 * We can add everything God will be doing here - dropping food, creatures etc.
	 * */
	@ScheduledMethod ( start = Constants.GOD_MODE_START , interval = Constants.GOD_MODE_INTERVAL)
	public void step()
	{
		DropFood();
		DropEnemy();
	}
	
	private void DropEnemy() {		
		
		if (RandomHelper.nextIntFromTo(0, 9) == 0) //10% chance of enemy drop
		{ 
			int enemyID;
			int rand = RandomHelper.nextIntFromTo( 0, 5 );
			if( rand == 5 ) {
				enemyID = 1;
				snake++;
			}
			else if ( rand == 4 || rand == 3 ) {
				enemyID = 2;
				scorpion++;
			}
			else {
				enemyID = 0;
				spider++;
			}
						
			float attack = 0;
			float health = 0;
			int droppedMeat = 0;
			
			//values for increasing health and attack
			float sqrtTickCount = (float)Math.sqrt(RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
			float healthConstant = sqrtTickCount;
			float attackConstant = sqrtTickCount;
			for(int i = 1; i < Constants.HEALTH_CONSTANT; i++)
				healthConstant = (float) Math.sqrt(healthConstant);
			
			for(int i = 1; i < Constants.ATTACK_CONSTANT; i++)
				attackConstant = (float) Math.sqrt(attackConstant);
			
			// super constructor must be first line of constructor.
			// we have to get all variables before running it. :(
			switch(enemyID) {
			case 0: //spider
				attack = Constants.SPIDER_ATTACK * attackConstant;
				health = Constants.SPIDER_HEALTH * healthConstant;
				SmartConsole.Print("Spider. Health: " + health + " Attack: " + attack, DebugModes.GOD);
				droppedMeat = Constants.SPIDER_MEAT_NO;
				break;
			case 1: //snake
				attack = Constants.SNAKE_ATTACK * attackConstant;
				health = Constants.SNAKE_HEALTH * healthConstant;
				droppedMeat = Constants.SNAKE_MEAT_NO;
				SmartConsole.Print("Snake. Health: " + health + " Attack: " + attack, DebugModes.GOD);
				break;
			case 2: //scorpion
				attack = Constants.SCORPION_ATTACK * attackConstant;
				health = Constants.SCORPION_HEALTH * healthConstant;
				droppedMeat = Constants.SCORPION_MEAT_NO;
				SmartConsole.Print("Scorpion. Health: " + health + " Attack: " + attack, DebugModes.GOD);
				break;
			default:
				break;
			}
			
			boolean isCloseToMaw = true;
			int x = 0, y = 0;
			while(isCloseToMaw) {
				x = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
				y = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
				GridPoint thisPoint = new GridPoint(x, y);
				if (grid.getDistance(thisPoint, new GridPoint(5, 5)) > Constants.DISTANCE_FROM_MAW &&
					grid.getDistance(thisPoint, new GridPoint(5, 45)) > Constants.DISTANCE_FROM_MAW &&
					grid.getDistance(thisPoint, new GridPoint(45, 5)) > Constants.DISTANCE_FROM_MAW &&
					grid.getDistance(thisPoint, new GridPoint(45, 45)) > Constants.DISTANCE_FROM_MAW)
					isCloseToMaw = false;				
			}
			Object temp = grid.getObjectAt( x, y );
			
			if(!(temp instanceof Enemy)) //don't add enemy where enemy already is
			{ 
				@SuppressWarnings("unchecked")
				Context<Object> context = ContextUtils.getContext(this);
				Enemy enemy = new Enemy( space, grid, enemyID, attack, health, droppedMeat );
				context.add( enemy );
				space.moveTo( enemy, x, y );
				grid.moveTo( enemy, x, y );
			}
		}			
	}
	
	private void DropFood() {		
		if (RandomHelper.nextIntFromTo( 0, Constants.FOOD_DROP_PROBABILITY) == 0) //probability of food drop
		{ 
			int foodID = 3;
			if(RandomHelper.nextIntFromTo( 0, 10 ) == 5) {
				foodID = 0;
				pizza++;
			}
			else {
				foodID = RandomHelper.nextIntFromTo( 1, 3 );
				if(foodID == 1)
					donut++;
				else if (foodID == 2)
					grape++;
				else
					cabbage++;
			}

			int x = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			int y = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			Object temp = grid.getObjectAt( x, y );
			
			if(!(temp instanceof Food)) //don't add food where food already is
			{ 
				@SuppressWarnings("unchecked")
				Context<Object> context = ContextUtils.getContext( this );
				Food food = new Food( space, grid, foodID );
				context.add( food );
				space.moveTo( food, x, y );
				grid.moveTo( food, x, y );
			}
		}			
	}
	
	public void AddMawToList(Maw maw) {
		mawList.add(maw);
	}

	public static void setDeadMawCounter(int deadMawID) {
		deadMawCounter += 1;
		mawIDlist.remove(Integer.valueOf(deadMawID));

		if(deadMawCounter == 3) {
			int coordinate = (int)Constants.GRID_SIZE/2;
			if (!(GSC.Instance().getGrid().getObjectsAt(coordinate, coordinate) instanceof EndGameInfo)) {
				Context<Object> context = GSC.Instance().getContext();
				if (context != null) {
					EndGameInfo endInfo = new EndGameInfo( mawIDlist.get(0) );
					context.add(endInfo);
					GSC.Instance().getGrid().moveTo(endInfo, coordinate, coordinate);
					GSC.Instance().getSpace().moveTo(endInfo, coordinate, coordinate);
				}
				
			}
			RunEnvironment.getInstance().endRun();
			printStatistics();
		}
	}
	
	private static void printStatistics()
	{
		if(Constants.STATISTICS_DEBUG_MODE)
		{
			Maw winner = MawFinder.Instance().GetMaw(mawIDlist.get(0));

			SmartConsole.Print("**************************************************", DebugModes.STATS);
			SmartConsole.Print("  END OF SIMULATION!!! MAW " + winner.getMawName() + " WON", DebugModes.STATS);
			SmartConsole.Print("        STATISTICS:        ", DebugModes.STATS);
			
			SmartConsole.Print("Tick count: " + RunEnvironment.getInstance().getCurrentSchedule().getTickCount(), DebugModes.STATS);
			SmartConsole.Print("Enemies:\n" + snake + " x Snake\n" + scorpion + " x Scorpion\n" + spider + " x Spider\n", DebugModes.STATS);
			SmartConsole.Print("Food:\n" + pizza + " x Pizza\n" + donut + " x Donut\n" + grape + " x Grape\n" + cabbage + " x Cabbage\n", DebugModes.STATS);
			
			SmartConsole.Print("Alliances Count: " + Alliance.ALLIANCE_COUNT, DebugModes.STATS);
			
			for(int i = 0; i < mawList.size(); i++) {
				Maw maw = mawList.get(i);
				SmartConsole.Print("**************************************************", DebugModes.STATS);
				
				SmartConsole.Print("\n" + mawList.get(i).getMawName() + " Maw\n", DebugModes.STATS);
				SmartConsole.Print("Tick of Death: " + maw.timeOfDeath, DebugModes.STATS);
				SmartConsole.Print("Total number of lost children: " + mawList.get(i).getNumOfLostChildren(), DebugModes.STATS);
				SmartConsole.Print("Greatest total number of children at a time: " + mawList.get(i).getMaxNumOftChildren(), DebugModes.STATS);
				SmartConsole.Print("Eaten food\n" + mawList.get(i).getPizza() + " x Pizza\n" + mawList.get(i).getDonut() + " x Donut\n"
									+ mawList.get(i).getGrape() + " x Grape\n" + mawList.get(i).getCabbage() + " x Cabbage\n"
									+ mawList.get(i).getMeat() + " x Meat\n" + mawList.get(i).getSteak() + " x Steak\n", DebugModes.STATS);
				
				MawScheduler scheduler = maw.getScheduler();
				String mawTaskStr = "Tasks Maw:\n"
						+ "Food: " + scheduler.TASK_COUNT[0] + "\n"
						+ "Enemy Creature: " + scheduler.TASK_COUNT[1] + "\n"
						+ "Enemy Formation: " + scheduler.TASK_COUNT[2] + "\n"
						+ "War: " + scheduler.TASK_COUNT[3] + "\n";
				
				String mobileTaskStr = "Tasks Mobile:\n"
						+ "Food: " + scheduler.TASK_COUNT_MOBILES[0] + "\n"
						+ "Enemy Creature: " + scheduler.TASK_COUNT_MOBILES[1] + "\n"
						+ "Enemy Formation: " + scheduler.TASK_COUNT_MOBILES[2] + "\n";
								
				SmartConsole.Print(mawTaskStr, DebugModes.STATS);
				SmartConsole.Print(mobileTaskStr, DebugModes.STATS);
				
				SmartConsole.Print("**************************************************", DebugModes.STATS);
			}
			SmartConsole.Print("**************************************************", DebugModes.STATS);
		}
	}
}
