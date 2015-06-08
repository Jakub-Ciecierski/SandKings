package creatures;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;


import map.EventType;
import map.Food;
import map.God;
import map.GraveStone;
import Constants.Constants;
import Enemies.Enemy;
import repast.simphony.context.Context;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import util.GSC;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import creatures.CreatureClasses.Mobile;

/**
 *	The base class of all fightable agents
 * @author wojtek
 *
 */
public abstract class Fightable extends Agent{
	// simulation props
	protected ContinuousSpace < Object > space; 
	public Grid< Object > grid;
	protected int playerID = 0;
	protected boolean isFighting = false;
	
	private Map<Integer, Float> damageDone = new HashMap<Integer, Float>();
	
	private float damage = 0;
	private float health = 0;
	private int droppedMeat = 0;
	
	public void updateDanger(){
		danger = damage * health;
	}
	public void updateProfit(){
		profit = droppedMeat * Constants.STEAK_CALORIES;
	}
	public float getRatio()
	{
		return profit/danger;
	}
	
	public float getDamage() {
		return damage;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	public int getDroppedMeat() {
		return droppedMeat;
	}
	public void setDroppedMeat(int droppedMeat) {
		this.droppedMeat = droppedMeat;
		updateProfit();
	}
	
	public float getHealth() {
		return health;
	}
	public void setHealth(float health) {
		this.health = health;
		updateDanger();
	}
	
	 private void initDmgDone()
	 {
		 for ( int i = 0; i <= 4; i++ )
			 damageDone.put( i, 0f);
	 }

	
	public Fightable( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID)
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
		initDmgDone();
	}
	
	public Fightable( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID,
			float damage, float health, int droppedMeat)
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
		this.damage = damage;
		this.health = health;
		this.droppedMeat = droppedMeat;
		initDmgDone();
		updateDanger();
		updateProfit();
	}

	public void dealDamage(float damage){
		int rnd = ( RandomHelper.nextIntFromTo(0, 100) );
		if ( rnd <= 10 )
			damage = 0;
		if ( rnd >= 90 )
			damage += damage;
		
		this.health -= damage;
		if(this.health <= 0)
			Die();
	}
	
	public void dealDamage(float damage, int fractionID) {
		int rnd = ( RandomHelper.nextIntFromTo(0, 100) );
		if ( rnd <= 10 )
			damage = 0;
		if ( rnd >= 90 )
			damage += damage;
		
		damageDone.put(fractionID, damage);
		
		this.health -= damage;
		if(this.health <= 0)
			Die();
	}
	
	public void Die()
	{
		Context<Object> context = GSC.Instance().getContext();

		if( this instanceof Maw)
		{
			Maw instance = (Maw)this;
			int size = instance.getChildren().size();
			for ( int i = size - 1; i > 0; i-- ) {
				instance.getChildren().get( i ).Die();
			}
			 DropFoodWithOwnerID(Constants.STEAK_ID);//DropFood(Constants.STEAK_ID);
		     instance.DropMawFood();

		     //create grave stone on the map
		     GraveStone grave = new GraveStone(this.space, this.grid);
		     GridPoint gp = instance.getGridpos();
		     context.add(grave);
		     
		     grid.moveTo(grave, (int)gp.getX(), (int)gp.getY());
		     space.moveTo(grave,  (int)gp.getX(), (int)gp.getY());
		     
		     God.setDeadMawCounter(this.playerID);
		     MawFinder.Instance().removeMaw(instance);
		     context.remove( this );
		     
		}
		
		if( this instanceof Enemy)
		{
			  DropFoodWithOwnerID(Constants.STEAK_ID);//DropFood(Constants.STEAK_ID);
			  context.remove( this );	
		}
		
		if( this instanceof Formation)
		{
			Formation formation = (Formation) this;
			for( Food f : formation.getCarriedStuff() )
			{
				f.setPicked(false);
			}
			context.remove( this );	
		}
		
		if (this instanceof Mobile)
	    {
			 Mobile instance = (Mobile)this;
			 DropFoodWithOwnerID(Constants.MEAT_ID);//DropFood(Constants.MEAT_ID);
		     Maw myMaw = MawFinder.Instance().GetMaw(this.playerID);
		     if ( myMaw == null ) { context.remove ( this ); return; }
		     myMaw.LostAMobile( instance );
		     if(instance.isInFormation()){
		    	 //instance.getMyFormation().kickOut(instance);
		    	 instance.getMyFormation().soldiers.remove(this);
		     }
		     if ( instance.carriedStuff != null )
		    	 instance.carriedStuff.setPicked(false);
		     context.remove( this );	
	    }
	}
	
	private void DropFoodWithOwnerID( int foodID )
	{
		if ( grid.getLocation( this ) == null ) return;
		
		int x = grid.getLocation ( this ).getX();
		int y = grid.getLocation ( this ).getY();
		
		// generate food to be dropped
		List<Food> droppedFood = new ArrayList<Food>();
		for( int i = 0; i < droppedMeat; i++)
			droppedFood.add( new Food( space, grid, foodID ) );
		
		// distribute food ownerIDs
		// calculate each ratio: damageDone[i] vs total dmgDone
		float totalDmg = 0f;
		for ( Entry<Integer, Float> entry : damageDone.entrySet() ) 
			totalDmg += entry.getValue(); // calculate total dmg dealt
		
		// every fraction has floor ( droppedMeat / ratio ) pieces of food
		int lastFoodCount = 0;
		for ( Entry<Integer, Float> entry : damageDone.entrySet() ) 
		{
			int foodCount = lastFoodCount + (int) Math.floor( droppedMeat * ( entry.getValue() / totalDmg ) );
			if ( foodCount > droppedFood.size() ) continue;
			for ( int i = lastFoodCount; i < foodCount; i ++  ) // partition droppedFood
			{
				// set food fraction ID
				droppedFood.get(i).setOwnerID( entry.getKey() );
			}
			lastFoodCount = foodCount;
		}
		
		// add foods to context
		for ( Food food : droppedFood )
		{
			GSC.Instance().getContext().add( food );
			space.moveTo( food, x, y );
			grid.moveTo( food, x, y );
		}

	}
	
	private void DropFood(int foodID) {
		if ( grid.getLocation ( this ) == null ) return;
		
		int x = grid.getLocation ( this ).getX();
		int y = grid.getLocation ( this ).getY();
		for( int i = 0; i < droppedMeat; i++)
			dropMeat(foodID, x, y, GSC.Instance().getContext());
	}
		
	protected void dropMeat(int foodID, int x, int y, Context<Object> context){
		Food food = new Food( space, grid, foodID );
		
		if ( context == null ) { System.out.println("context null"); return; }
		
		context.add( food );
		space.moveTo( food, x, y );
		grid.moveTo( food, x, y );
	}
	
	
	public boolean Attack(){
		// get the grid location of this Human
		GridPoint pt = grid.getLocation ( this );
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		if(pt == null || grid == null) return false;

		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid , pt ,
		Mobile . class , 1 , 1);
		
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Fightable && (Fightable)obj != this){
					
					Fightable f = (Fightable)obj;
					
					if(MawFinder.Instance().areWeEnemies(f.playerID, this.playerID))
					{
						GSC.Instance().AddEventInfo(EventType.Attack, 6 , 
								new GridPoint(pt.getX() - 1, pt.getY() - 1));
						f.dealDamage(this.damage, this.playerID);
						isFighting = true;
						return true;
					}
				}
			}
		}
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Fightable && (Fightable)obj != this){
					
					Fightable mobile = (Fightable)obj;
					
					if(!MawFinder.Instance().areWeFriends(mobile.playerID, this.playerID))
					{
						GSC.Instance().AddEventInfo(EventType.Attack, 6 , 
								new GridPoint(pt.getX() - 1, pt.getY() - 1));
						mobile.dealDamage(this.damage, this.playerID);
						isFighting = true;
						return true;
					}
				}
			}
		}
		
		isFighting = false;
		return false;
	}

}
