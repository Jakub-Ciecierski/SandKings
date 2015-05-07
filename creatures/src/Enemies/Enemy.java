package Enemies;

import repast.simphony.parameter.Parameter;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Enemy {
	private int enemyID; //enemy type
	private ContinuousSpace < Object > space; 
	private Grid< Object > grid;
	public Enemy (ContinuousSpace<Object> space, Grid<Object> grid, int enemyID) {
		this.space = space;
		this.grid = grid;
		this.enemyID = enemyID;
	}
	
	@Parameter(displayName = "Enemy", usageName = "enemyID")
	public int getEnemyID() {
		return enemyID;
	}
	
	/**
	 * @param enemyID the enemyID to set
	 */
	public void setEnemyID(int enemyID) {
		this.enemyID = enemyID;
	}
	
	public int getStrength() {
		int strength = 0;
		
		switch(this.enemyID) {
		case 0: //spider
			strength = 200;
			break;
		case 1: //snake
			strength = 1000;
			break;
		default:
			break;
		}
		
		return strength;
	} 
	public int getWeight() {
		int weight = 0;
		
		switch(this.enemyID) {
		case 0: //spider
			weight = 10;
			break;
		case 1: //snake
			weight = 100;
			break;
		default:
			break;
		}
		
		return weight;
	}
}
