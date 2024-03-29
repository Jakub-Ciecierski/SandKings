package communication.knowledge;

import java.util.ArrayList;
import java.util.List;

import Constants.Constants;
import Enemies.Enemy;
import map.Food;
import creatures.Agent;
import creatures.Formation;

/**
 * Knowledge base of each agent.
 * Contains a list of information.
 * 
 * @author kuba
 *
 */
public class KnowledgeBase {
	
	private List<Information> knowledge = new ArrayList<Information>();
	
	private List<Agent> cache = new ArrayList<Agent>();
	
	private int maxInfoCount;
	
	public KnowledgeBase(int maxInfoCount){
		this.maxInfoCount = maxInfoCount;
	}
	
	private boolean isInCache(Agent agent){
		for(int i = 0; i < cache.size(); i++){
			Agent cachedAgent = cache.get(i);
			if(cachedAgent !=null)
				if(cachedAgent == agent)
					return true;
		}
		return false;
	}
	
	private boolean hasInformation(Information info){
		for(int i =0;i < knowledge.size();i++){
			Information currentInfo = knowledge.get(i);
			if(info.getAgent() == currentInfo.getAgent()){
				currentInfo.setGridPoint(info.getGridPoint());
				currentInfo.setTickCount(info.getTickCount());
				return true;
			}
		}
		return false;
	}
	
	public Information getInformation(int index){
		return knowledge.get(index);
	}
	
	public int getSize(){
		return knowledge.size();
	}
	
	public List<Information> getKnowledge(){
		// TODO copy list
		return knowledge;
	}
		
	/**
	 * Adds information to knowledge base
	 * @param newInfo
	 * @return
	 */
	public boolean addInformation(Information newInfo){
		synchronized (this) {

			// dont add this info if it already exists
			if(this.hasInformation(newInfo)) {
				return false;
			}
			
			if(isInCache(newInfo.getAgent()))
				return false;
			
			// Replace the oldest information with lower priority
			if(knowledge.size() >= maxInfoCount){
				for(int i =0;i < knowledge.size();i++){
					Information info = knowledge.get(i);
					if(info == null ||
							!info.isUsefull || isInCache(info.getAgent()) ||
							newInfo.getType().getPriority() >= info.getType().getPriority()){
						knowledge.set(i, newInfo);
					}
				}
			}
			else
				knowledge.add(newInfo);
			
			cache.add(newInfo.getAgent());
		}
		return true;
	}
	
	public void addToCache(Agent agent){
		this.cache.add(agent);
	}
	
	public void removeInformation(Information info){
		this.knowledge.remove(info);
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
		else if(agent instanceof Enemy){
			return InformationType.ENEMY_CREATURE;
		}
		else if(agent instanceof Formation)
		{
			return InformationType.ENEMY_FORMATION;
		}
		return InformationType.GARBAGE;
	}
}
