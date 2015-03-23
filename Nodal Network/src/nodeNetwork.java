import java.util.ArrayList;


public class nodeNetwork
{
   /*************
    * VARIABLES *
	*************/
	ArrayList<desireNode> nodeList;
	
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
