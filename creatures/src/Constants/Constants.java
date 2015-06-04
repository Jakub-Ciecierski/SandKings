package Constants;


/**
 * @author Viet Ba
 *	class for constants
 */
public final class Constants {
	// GENERAL
	public static final int GRID_SIZE = 50;
	public static final int BIGGEST_DISTANCE = (int) Math.sqrt(2 * GRID_SIZE * GRID_SIZE);
	public static final int DISTANCE_FROM_MAW = 10;

	// INTERVALS
	public static final int MOVE_START = 1;
	public static final int GOD_MODE_START = 10;
	public static final int GOD_MODE_INTERVAL = 20;
	public static final int ENEMIES_MOVE_INTERVAL = 10;
	public static final int MOBILE_SPAWN_INTERVAL = 20;
	public static final int CREATURES_MOVE_INTERVAL = 1;
	
	// MAW CONSTANTS
	public static final float CREATURES_SIZE = 1;
	public static final int CHILDREN_PER_POWER = 5;
	public static final int MAW_FOOD_DESIRE_THRESHOLD = 5;
	public static final int FOOD_PER_SPAWN = 5;
	public static final int MAW_START_FOOD = 100; 
	public static final float MAW_START_STRENGTH = 0;
	public static final int MAW_BIRTHING_FACTOR = 4;
	public static final int MAW_STRENGTH_FACTOR = 10;

	public static final int MAW_STARVING_FACTOR = 5;
	public static final int MAW_FOOD_DECREASE_VALUE = 1;
	public static final int MAW_EATEN_FOOD_DECREASE_VALUE = 2;
	public static final int MAW_DAMAGE_DECREASE_VALUE = 100;	

	public static final int MAW_DISTANCE_FACTOR = 5;

	public static final int MAW_CHILDPOOP_COUNTER = 1;
	public static final int MAW_STRENGTH_COUNTER = 5;

	public static final float MAW_ATTACK = 0;
	public static final float MAW_HEALTH = 5000;
	public static final int MAW_MEAT_NO = 50;
	
	// MOBILE CONSTANTS
	public static final int MOBILE_SIZE_MULTIPLIER = 2;
	public static final int MOBILE_CARRY_CAPACITY = 1;
	// on 50*50 grid this is [0-70] + [-inf - MOBILE_STARTING_FOOD]
	public static final int MOBILE_GO_HOME_THRESHOLD = 50;  
	public static final int MOBILE_STEPS_PER_FEWD = 150;
	public static final int MOBILE_STOMACH_SIZE = 2;
	public static final int MOBILE_STARTING_FOOD = MOBILE_STOMACH_SIZE * MOBILE_STEPS_PER_FEWD; // 1000 steps possibru
	public static final int MOBILE_STARTVATION_THRESHOLD = 50;
	
	public static final float MOBILE_ATTACK = 5;
	public static final float MOBILE_HEALTH = 100;
	public static final int MOBILE_MEAT_NO = 1;
	
	public static final float STRENGTH_MULTIPLY_FACTOR = (float) 0.2;
	// ENEMIES CONSTANTS
	public static final int ENEMY_MULTIPLIER = 1;
	public static final int ENEMIES_MARGIN = 3;
	public static final int MAX_NUMBER_OF_ENEMIES = 10;
	
	public static final float SPIDER_ATTACK = 10 * ENEMY_MULTIPLIER;
	public static final float SPIDER_HEALTH = 150 * ENEMY_MULTIPLIER;
	public static final int SPIDER_MEAT_NO = 1;
	
	public static final float SNAKE_ATTACK = 40 * ENEMY_MULTIPLIER;
	public static final float SNAKE_HEALTH = 600 * ENEMY_MULTIPLIER;
	public static final int SNAKE_MEAT_NO = 20;
	
	public static final float SCORPION_ATTACK = 20 * ENEMY_MULTIPLIER;
	public static final float SCORPION_HEALTH = 300 * ENEMY_MULTIPLIER;
	public static final int SCORPION_MEAT_NO = 4;
	
	public static final int HEALTH_CONSTANT = 4;
	public static final int ATTACK_CONSTANT = 5;	
	
	// FOOD CONSTANTS
	public static final int FOOD_MULTIPLIER = 2;
	public static final int PIZZA_CALORIES = 50 * FOOD_MULTIPLIER;
	public static final int DONUT_CALORIES = 25 * FOOD_MULTIPLIER;
	public static final int GRAPE_CALORIES = 15 * FOOD_MULTIPLIER;
	public static final int CABBAGE_CALORIES = 5 * FOOD_MULTIPLIER;
	public static final int MEAT_CALORIES = 3;
	public static final int STEAK_CALORIES = 10 * FOOD_MULTIPLIER;

	public static final int PIZZA_WEIGHT = 7;
	public static final int DONUT_WEIGHT = 5;
	public static final int GRAPE_WEIGHT = 3;
	public static final int CABBAGE_WEIGHT = 1;
	public static final int MEAT_WEIGHT = 1;
	
	// EVENT CONSTANTS
	public static final int EVENTINFO_TIMEOUT = 100;
	public static final int EVENT_DISTANCE = 1;
	public static final int STARVATION_TIMEOUT = 200;
	
	// MOBILE VICINITY DISTANCE
	public static final int MOBILE_VICINITY_X = 5;
	public static final int MOBILE_VICINITY_Y = 5;
	
	public static final int MOBILE_MAX_KNOWLEDGE = 10;
	
	public static final int MAW_MAX_KNOWLEDGE = 500;
	
	// DISPLAY ALL DEBUG MESSAGES
	public static final boolean DEBUG_MODE = true;
	public static final int FORMATION_NEEDED_FRACTION = 2;
	
	/**
	 * Hidden constructor to ensure no instances are created.
	 */
	private Constants() {

	}
}
