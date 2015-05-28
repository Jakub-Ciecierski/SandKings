package communication.knowledge;

import java.util.ArrayList;
import java.util.List;

import Constants.Constants;
import Enemies.Enemy;
import map.Food;
import creatures.Agent;

/**
 * Knowledge base of each agent.
 * Contains a list of information.
 * 
 * @author kuba
 *
 */
public class KnowledgeBase {
	
	private List<Information> knowledge = new ArrayList<Information>();
	
	private int maxInfoCount;
	
	public KnowledgeBase(int maxInfoCount){
		this.maxInfoCount = maxInfoCount;
	}
	
	public void addInformation(Information info){
		// Replace the oldest information with lower priority
		if(knowledge.size() >= maxInfoCount){
			for(int i =0;i < knowledge.size();i++){
				if(info.getType().getPriority() <= info.getType().getPriority()){
					knowledge.set(i, info);
				}
			}
		}
		else
			knowledge.add(info);
	}
	
	/**
	 * Checks if given agent is interesting for knowledge base
	 * and returns its type.
	 * TODO Add any new agent what are supposed to be important.
	 * 
	 * @param agent
	 * @return The type of information that this agent represents 
	 */
	public static InformationType GetInfoType(Agent agent){
		if(agent instanceof Food){
			return InformationType.FOOD;
		}
		if(agent instanceof Enemy){
			return InformationType.ENEMY_CREATURE;
		}
		return InformationType.GARBAGE;
	}
}
