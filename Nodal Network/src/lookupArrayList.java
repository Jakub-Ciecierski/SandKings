import java.util.ArrayList;


public class lookupArrayList extends ArrayList<Triple<desireNode, Double, Integer>>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7724358519888895180L;

	public boolean lookupContains(desireNode node)
	{
		for(Triple<desireNode, Double, Integer> apair : this){
			if(apair.getLeft().equals(node))
				return true;
		}
		return false;
	}
	
	public Triple<desireNode, Double, Integer> getByFirstElement(desireNode node)
	{
		for(Triple<desireNode, Double, Integer> apair : this){
			if(apair.getLeft().equals(node))
				return apair;
		}
		return null;
	}
}
