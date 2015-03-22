import java.util.ArrayList;

public class desireNode
{

   /*************
    * VARIABLES *
	*************/
	private int base;
	private int influenced;
	private ArrayList<influence> influenceList;
	private lookupArrayList lookupTable;
	private String name;

	
   /*******************
    * GETTERS/SETTERS *
	*******************/

	public int getInfluenced() {
		return influenced;
	}

	public void setInfluenced(int influenced) {
		this.influenced = influenced;
	}
	
	public void updateInfluenced(int influenced) {
		this.influenced = influenced;
		recalculateInfluence();
	}

	public ArrayList<influence> getInfluenceList() {
		return influenceList;
	}

	public void addInfluence(desireNode node, double ratio){
		influence relation = new influence(node, this, ratio);
		influenceList.add(relation);
	}
	public String getName() {
		return name;
	}
	
	public int getBase() {
		return base;
	}
	
	public void setBase(int base) {
		this.base = base;
	}
	
	public void updateBase(int base) {
		this.base = base;
		recalculateInfluence();
	}
	
   /****************
    * CONSTRUCTORS *
	****************/
	
   public desireNode( String name) {
		this.influenced = 0;
		this.influenceList = new ArrayList<influence>();
		this.name = name;
		this.base = 0;
		this.lookupTable = new lookupArrayList();
	}
	
   /***********
    * Methods *
	***********/
	
	public int getDesire()
	{
		if(getInfluenced() < 0)
			return 0;
		if(getInfluenced() > 100)
			return 100;
		return getInfluenced();
	}
	
	public void recalculateInfluence()
	{
		for(Triple<desireNode, Double, Integer> apair : lookupTable){
			apair.getLeft().setInfluenced( (
											apair.getLeft().getInfluenced() -
											apair.getRight() +
											(int)((double)getDesire() * apair.getMiddle().doubleValue())
										));
		}
	}
	
	public void recalculateLookupTable()
	{
		Triple<desireNode, Double, Integer> apair =
				new Triple(
						this,
						new Double(1), // Can't be simply 1, as it is cast to Integer, and later it is buggy.
						0
						);
		lookupTable.add(apair);
		addToLookupTable(lookupTable, this);
		lookupTable.remove(apair);
	}
	
	private void addToLookupTable(lookupArrayList lookup, desireNode node)
	{
		for(influence influences : node.getInfluenceList()){
			if(lookup.lookupContains(influences.getReferenceNode())){
				//MOST PROBABLY SOMETHING FUCKY SO WE DISREGARD IT
				//AND ASSUME THAT THE PROGRAMMER DESIGNING THE NODAL
				//NETWORK HAS ABSOLUTELY NO IDEA WHAT IS HE DOING
			}
			else{
				Triple<desireNode, Double, Integer> test =
						lookup.getByFirstElement(influences.getThisNode());
				
				Triple<desireNode, Double, Integer> apair =
						new Triple(
								influences.getReferenceNode(),
								test.getMiddle() * influences.getRatio(),
								0
								);
				lookup.add(apair);
				
				addToLookupTable(lookup, influences.getReferenceNode());
			}
		}
	}
}
