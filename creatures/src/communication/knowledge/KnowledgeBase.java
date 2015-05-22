package communication.knowledge;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {
	
	List<Information> knowledge = new ArrayList<Information>();
	
	public KnowledgeBase(){
		
	}
	
	public void addInformation(Information info){
		knowledge.add(info);
	}
	
}
