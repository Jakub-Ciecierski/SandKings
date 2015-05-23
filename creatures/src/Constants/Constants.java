package Constants;


/**
 * @author Viet Ba
 *	class for constants
 */
public final class Constants {
	// GENERAL
	public static final int GRID_SIZE = 50;
	
	// INTERVALS
	public static final int MOVE_START = 1;
	public static final int GOD_MODE_START = 1000;
	public static final int GOD_MODE_INTERVAL = 2000;
	public static final int ENEMIES_MOVE_INTERVAL = 1000;
	public static final int MOBILE_SPAWN_INTERVAL = 2000;
	public static final int CREATURES_MOVE_INTERVAL = 100;
	
	// MAW CONSTANTS
	public static final float CREATURES_SIZE = 1;
	public static final int CHILDREN_PER_POWER = 5;
	public static final int MAW_FOOD_DESIRE_THRESHOLD = 5;
	public static final int FOOD_PER_SPAWN = 1;
	
	// MOBILE CONSTANTS
	public static final int MOBILE_SIZE_MULTIPLIER = 2;
	public static final int MOBILE_STARTING_FOOD = 500; // 100 steps possibru
	public static final int MOBILE_HEALTH = 100;
	public static final int MOBILE_CARRY_CAPACITY = 5;
	// on 50*50 grid this is [0-70] + [-inf - MOBILE_STARTING_FOOD]
	public static final int MOBILE_GO_HOME_THRESHOLD = 500;  
	
	// ENEMIES CONSTANTS
	public static final int ENEMIES_MARGIN = 3;
	public static final int MAX_NUMBER_OF_ENEMIES = 10;
	
	public static final int SPIDER_ATTACK = 50;
	public static final int SPIDER_HEALTH = 400;
	public static final int SPIDER_CALORIES = 50;
	public static final int SPIDER_WEIGHT = 20;
	
	public static final int SNAKE_ATTACK = 100;
	public static final int SNAKE_HEALTH = 1000;
	public static final int SNAKE_CALORIES = 75;
	public static final int SNAKE_WEIGHT = 50;
	
	public static final int SCORPION_ATTACK = 75;
	public static final int SCORPION_HEALTH = 800;
	public static final int SCORPION_CALORIES = 60;
	public static final int SCORPION_WEIGHT = 35;
	
	// FOOD CONSTANTS
	public static final int PIZZA_CALORIES = 20;
	public static final int DONUT_CALORIES = 15;
	public static final int GRAPE_CALORIES = 10;
	public static final int CABBAGE_CALORIES = 5;
	public static final int MEAT_CALORIES = 5;
	
	public static final int PIZZA_WEIGHT = 20;
	public static final int DONUT_WEIGHT = 15;
	public static final int GRAPE_WEIGHT = 10;
	public static final int CABBAGE_WEIGHT = 5;
	public static final int MEAT_WEIGHT = 5;
	
	// NUMBER OF DROPPED MEAT
	public static final int SCORPION_MEAT_NO = SCORPION_CALORIES / MEAT_CALORIES;
	public static final int SNAKE_MEAT_NO = SNAKE_CALORIES / MEAT_CALORIES;
	public static final int SPIDER_MEAT_NO = SPIDER_CALORIES / MEAT_CALORIES;
	public static final int MOBILE_MEAT_NO = 1;

	/**
	 * Hidden constructor to ensure no instances are created.
	 */
	private Constants() {

	}
}
