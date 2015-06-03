/**
 * 
 */
package map;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
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
	
	public God (ContinuousSpace<Object> space, Grid<Object> grid) { 
		this.space = space;
		this.grid = grid;
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
			@SuppressWarnings("unchecked")
			Context<Object> context = ContextUtils.getContext(this);
			int enemyID;
			int rand = RandomHelper.nextIntFromTo( 0, 5 );
			if( rand == 5 )
				enemyID = 1;
			else if ( rand == 4 || rand == 3 )
				enemyID = 2;
			else 
				enemyID = 0;	
						
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
			
			// super constructor must be first line of contructor.
			// we have to get all variables before running it. :(
			switch(enemyID) {
			case 0: //spider
				attack = Constants.SPIDER_ATTACK * attackConstant;
				health = Constants.SPIDER_HEALTH * healthConstant;
				System.out.println("*** Health: " + health + " Attack: " + attack + "***");
				droppedMeat = Constants.SPIDER_MEAT_NO;
				break;
			case 1: //snake
				attack = Constants.SNAKE_ATTACK * attackConstant;
				health = Constants.SNAKE_HEALTH * healthConstant;
				droppedMeat = Constants.SNAKE_MEAT_NO;
				System.out.println("*** Health: " + health + " Attack: " + attack + "***");
				break;
			case 2: //scorpion
				attack = Constants.SCORPION_ATTACK * attackConstant;
				health = Constants.SCORPION_HEALTH * healthConstant;
				droppedMeat = Constants.SCORPION_MEAT_NO;
				System.out.println("*** Health: " + health + " Attack: " + attack + "***");
				break;
			default:
				break;
			}
			
			Enemy enemy = new Enemy( space, grid, enemyID, attack, health, droppedMeat );
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
				context.add( enemy );
				space.moveTo( enemy, x, y );
				grid.moveTo( enemy, x, y );
			}
		}			
	}
	
	private void DropFood() {		
		int rand = RandomHelper.nextIntFromTo( 0, 2 );
		if (rand == 0) //33% chance of food drop
		{ 
			int foodID = 3;
			if(RandomHelper.nextIntFromTo( 0, 10 ) == 5)
				foodID = 0;
			else
				foodID = RandomHelper.nextIntFromTo( 1, 3 );
			@SuppressWarnings("unchecked")
			Context<Object> context = ContextUtils.getContext( this );
			Food food = new Food( space, grid, foodID );
			int x = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			int y = RandomHelper.nextIntFromTo( 2, Constants.GRID_SIZE - 2 );
			Object temp = grid.getObjectAt( x, y );
			
			if(!(temp instanceof Food)) //don't add food where food already is
			{ 
				context.add( food );
				space.moveTo( food, x, y );
				grid.moveTo( food, x, y );
			}
		}			
	}

	public static void setDeadMawCounter() {
		deadMawCounter += 1;
		System.out.println("**************************************************");
		System.out.println("***                                            ***");
		System.out.println("***                                            ***");
		System.out.println("***     MAW DIEDED						       ***");
		System.out.println("***                                            ***");
		System.out.println("***                                            ***");
		System.out.println("**************************************************");
		if(deadMawCounter == 3) {
			RunEnvironment.getInstance().endRun();
			if(Constants.DEBUG_MODE)
			{
				System.out.println("**************************************************");
				System.out.println("***                                            ***");
				System.out.println("***                                            ***");
				System.out.println("***     END OF SIMULATION!!! 3 MAWS DIEDED     ***");
				System.out.println("***                                            ***");
				System.out.println("***                                            ***");
				System.out.println("**************************************************");

			}
		}
	}
}
