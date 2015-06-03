package creatures;

import java.util.List;

import map.Food;
import map.God;
import Constants.Constants;
import Enemies.Enemy;
import repast.simphony.context.Context;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import communication.messages.DamageMessage;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import creatures.CreatureClasses.Mobile;
import creatures.CreatureClasses.Worker;

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
	
	
	public Fightable( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID)
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
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
		updateDanger();
		updateProfit();
	}

	public void dealDamage(float damage){
		this.health -= damage;
		if(this.health <= 0)
			Die();
	}
	
	public void Die()
	{
		@SuppressWarnings("unchecked")
		Context<Object> context = ContextUtils.getContext(this);
		
		if(this == null || context == null)
			return;
		
		if( this instanceof Maw)
		{
			Maw instance = (Maw)this;
			for( Worker worker : instance.getChildren())
			{
				worker.Die();
			}
		     DropFood(5);
		     instance.DropMawFood();
		     God.setDeadMawCounter();
		     context.remove( this );
		}
		
		else if( this instanceof Enemy)
		{
			  DropFood(5);
			  context.remove( this );	
		}
		
		else if( this instanceof Formation)
		{
			  context.remove( this );	
		}
		
		else
	    {
			 Mobile instance = (Mobile)this;
		     DropFood(4);
		     MawFinder.Instance().GetMaw(this.playerID).LostAMobile();
		     if(instance.isInFormation()){
		    	 instance.getMyFormation().soldiers.remove(this);
		     }
		     context.remove( this );	
	    }
	}
	
	private void DropFood(int foodID) {		
			@SuppressWarnings("unchecked")
			Context<Object> context = ContextUtils.getContext( this );

			int x = grid.getLocation ( this ).getX();
			int y = grid.getLocation ( this ).getY();
			
			for( int i = 0; i < droppedMeat; i++)
				dropMeat(foodID, x, y, context);
	}
	
	protected void dropMeat(int foodID, int x, int y, Context<Object> context){
		Food food = new Food( space, grid, foodID );

		context.add( food );
		space.moveTo( food, x, y );
		grid.moveTo( food, x, y );
	}
	
	
	public void Attack(){
		// get the grid location of this Human
		GridPoint pt = grid.getLocation ( this );
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		if(pt == null || grid == null)
		{
			int a = 7;
		}
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid , pt ,
		Mobile . class , 1 , 1);
		
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Fightable && (Fightable)obj != this){
					
					Fightable mobile = (Fightable)obj;
					
					if(MawFinder.Instance().areWeEnemies(mobile.playerID, this.playerID))
					{
						mobile.dealDamage(this.damage);
						isFighting = true;
						return;
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
						mobile.dealDamage(this.damage);
						isFighting = true;
						return;
					}
				}
			}
		}
		
		isFighting = false;
	}

}
