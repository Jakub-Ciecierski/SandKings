package creatures;

import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import communication.Message;
import communication.MessagePacket;
import communication.MessageQueue;
import communication.messages.AskForFoodMessage;
import communication.messages.DamageMessage;
import communication.messages.KillMessage;
import communication.messages.QueryMessage;
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
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	private int playerID = 0;
	
	private float damage = 0;
	private float health = 0;
	
	public float getDamage() {
		return damage;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	public void dealDamage(float damage){
		this.health -= damage;
		if(this.health <= 0)
			Delete();
	}
	
	public void Delete()
	{
		Context<Object> context = ContextUtils.getContext(this);
		  if(this != null && context != null)
		  {
			  MawFinder.Instance().GetMaw(this.playerID).LostAMobile();
			  context.remove( this );	
		  }
	}
	
	public float getHealth() {
		return health;
	}
	public void setHealth(float health) {
		this.health = health;
	}
	
	
	public Fightable( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID)
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
	}
	
	public Fightable( ContinuousSpace < Object > space, Grid< Object > grid, int setPlayerID,
			float damage, float health)
	{
		this.space = space;
		this.grid = grid;
		this.playerID = setPlayerID;
		this.damage = damage;
		this.health = health;
	}

	
	public void attack(){
		// get the grid location of this Human
		GridPoint pt = grid.getLocation ( this );
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>( grid , pt ,
		Mobile . class , 1 , 1);
		
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood ( true );
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : grid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if(obj instanceof Fightable && (Fightable)obj != this){
					
					Fightable mobile = (Fightable)obj;
					
					if(!MawFinder.Instance().areWeFriends(mobile.playerID, this.playerID))
					{
						DamageMessage damageMessage = new DamageMessage(this.damage);
						sendMessage( mobile, damageMessage );
					}
				}
			}
			
		}
	}

}
