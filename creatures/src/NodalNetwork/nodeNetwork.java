package NodalNetwork;

import java.util.ArrayList;


public class nodeNetwork
{
   /*************
    * VARIABLES *
	*************/
	private ArrayList<desireNode> nodeList;
	
   /*****************
    * NODAL NETWORK *
	*****************/
	// resources
	private resourceNode food = new resourceNode("food");
	private resourceNode numOfKids = new resourceNode("numOfKids");
	private resourceNode strengthOfEnemies = new resourceNode("strengthOfEnemies");
	
	// desires
	private desireNode war = new desireNode("war");
	private desireNode goHome = new desireNode("goHome");
	private desireNode explore = new desireNode("explore");
	private desireNode aggression = new desireNode("aggression");	
	
   /*****************
    * LIST ACCESORS *
	*****************/
	
	/**
	 * @return the nodeList
	 */
	public ArrayList<desireNode> getNodeList() {
		return nodeList;
	}

	public void addToList(desireNode newNode)
	{
		for( desireNode node : nodeList ){
		if(node.getName().equals(newNode.getName())){
			return;
			}
		}	
		nodeList.add(newNode);
	}


   /****************
    * CONSTRUCTORS *
	****************/
	
	public nodeNetwork()
	{
		nodeList = new ArrayList<desireNode>();
		initNodeNetworkForPlayer();
	}
	
	public nodeNetwork(nodeNetwork Copy)
	{
		nodeList = new ArrayList<desireNode>();
		nodeList = (ArrayList<desireNode>) Copy.nodeList.clone();
	}
	
   /***********
    * Methods *
	***********/
	
	public void initNodeNetworkForPlayer()
	{
		// the smaller the food, the bigger the explore
		food.addInfluence(explore, 0, true, -0.5);
		
		// the smaller the food, the bigger the aggression
		food.addInfluence(aggression, 0, true, -0.5);
		
		// the bigger the food, the bigger the number of kids
		food.addInfluence(numOfKids, 0, true, 0.5);
		
		// the bigger the strength of enemies, the bigger the desire to go home
		strengthOfEnemies.addInfluence(goHome, 0, true, 0.5);
		
		// the more war the more we want to go home
		war.addInfluence(goHome, 0, true, 0.5);	
		
		addToList(goHome);
		addToList(war);
		addToList(aggression);
		addToList(explore);
		addToList(strengthOfEnemies);
		addToList(numOfKids);
		addToList(food);
	}
	
	public void initNodeNetworkForUnit()
	{
		
	}
	
	public int getElementDesire(String name)
	{
		for( desireNode node : nodeList ){
			if(node.getName().equalsIgnoreCase(name)){
				return node.getDesire();
			}
		}
		return 0;
	}
	
	public void doStep()
	{
		for( desireNode node : nodeList){
			node.singleStep();
		}
		for( desireNode node : nodeList){
			node.updateStep();
		}
		
	}
}
