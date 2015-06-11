package communication.knowledge;

public enum InformationType {
	MAW(6),
	ENEMY_FORMATION(5),
	FOOD(4),
	ENEMY_CREATURE(3),
	ENEMY_MOBILE(1), 
	FRIEDLY_MOBILE(0),
	GARBAGE(-1);
	
	/**
	 * Says how important is given information.
	 * Lower value means that it will be forget easier.
	 * Negative values are not important at all thus
	 * are never remembered.
	 */
	private int priority;
	
	InformationType(int priority){
		this.priority = priority;
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public String toString(){
		if(this == InformationType.ENEMY_CREATURE){
			return "Enemy creature";
		}
		else if(this == InformationType.FOOD){
			return "Food";
		}
		else if(this == InformationType.ENEMY_MOBILE){
			return "Enemy mobile";
		}
		else if(this == InformationType.FRIEDLY_MOBILE){
			return "Friendly mobile";
		}
		else if(this == InformationType.MAW){
			return "Maw";
		}
		else if(this == InformationType.ENEMY_FORMATION){
			return "Enemy Formation";
		}
		else
			return "Garbage";
	}

}
