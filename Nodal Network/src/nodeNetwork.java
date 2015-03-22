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

	public void addToList(desireNode newNode){
		for( desireNode node : nodeList ){
		if(node.getName().equals(newNode.getName())){
			return;
			}
		}
		
		nodeList.add(newNode);
	}
	
	public void init(){
		for(desireNode node : nodeList){
			node.recalculateLookupTable();
		}
		for(desireNode node : nodeList){
			node.recalculateInfluence();
		}		
	}


   /****************
    * CONSTRUCTORS *
	****************/
	
	public nodeNetwork(){
		nodeList = new ArrayList<desireNode>();
	}
	
   /***********
    * Methods *
	***********/
	
	public void initNodeNetworkForPlayer(){
		
	}
	
	public void initNodeNetworkForUnit(){
		
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
	
	// Check if added node creates loops.
	// We don't wanna loops.
	private boolean checkForLoopsFromNode(desireNode checkedNode){
		ArrayList<Boolean> wasVisited = new ArrayList<Boolean>();
		for (int i = 0; i < nodeList.size(); i++){
			wasVisited.add(false);
		}
		
		return hasCycle(nodeList.indexOf(checkedNode), wasVisited);
	}
	
	private boolean hasCycle(int start, ArrayList<Boolean> wasVisited )
	{
		wasVisited.set(start, true);
	    for (influence relation : nodeList.get(start).getInfluenceList()) {
	    	if(!nodeList.contains(relation.getReferenceNode()))
	    		continue;
	    	int index = nodeList.indexOf(relation.getReferenceNode());
	        if (wasVisited.get(index)  ||  hasCycle(index, wasVisited))
	            return true;
	    }
	    return false;
	}
}
