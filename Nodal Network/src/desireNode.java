import java.util.ArrayList;
 
public class desireNode
{
 
   /*************
    * VARIABLES *
    *************/
    private int baseValue;
    private int influencedValue;
    private int stepInfluencedValue;
    
    private ArrayList<influence> influenceList;
    private String name;
 
     
   /*******************
    * GETTERS/SETTERS *
    *******************/
 
    public int getInfluencedValue()
    {
        return influencedValue;
    }
 
    public void setInfluencedValue(int influencedValue)
    {
        this.influencedValue = influencedValue;
    }
 
    public int getStepInfluencedValue() {
		return stepInfluencedValue;
	}

	public void setStepInfluencedValue(int stepInfluencedValue) {
		this.stepInfluencedValue = stepInfluencedValue;
	}

	public ArrayList<influence> getInfluenceList()
    {
        return influenceList;
    }
 
    public void addInfluence(desireNode node, int treshold, boolean desireBiggerThanThreshold, double ratio)
    {
    	influence relation = new influence(node, treshold, desireBiggerThanThreshold, ratio);
        influenceList.add(relation);
    }
    public String getName()
    {
        return name;
    }
     
    public int getBaseValue()
    {
        return baseValue;
    }
     
    public void setBaseValue(int baseValue)
    {
        this.baseValue = baseValue;
    }
     
   /****************
    * CONSTRUCTORS *
    ****************/
     
    public desireNode( String name)
    {
         this.baseValue =				0;
         this.influencedValue = 		0;
         this.stepInfluencedValue = 	0;
         this.influenceList = 		new ArrayList<influence>();
         this.name = 				name;
     }
    
    public desireNode( desireNode Copy )
    {
         this.baseValue =				Copy.baseValue;
         this.influencedValue = 		Copy.influencedValue;
         this.stepInfluencedValue = 	Copy.stepInfluencedValue;
         this.influenceList = 			Copy.influenceList;
         this.name = 					Copy.name;
     }
     
   /***********
    * Methods *
    ***********/
     
    public int getDesire()
    {
        if(getInfluencedValue() < 0)
            return 0;
        if(getInfluencedValue() > 100)
            return 100;
        return getInfluencedValue();
    }
    
    public void singleStep()
    {
    	for(influence relation : influenceList){
            relation.changeInfuence(getDesire());
        }	
    }
    
    public void updateStep()
    {
    	this.influencedValue = this.stepInfluencedValue;
    }
}