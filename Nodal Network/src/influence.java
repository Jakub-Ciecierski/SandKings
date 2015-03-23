public class influence 
{
   /*************
    * VARIABLES *
    *************/
     
    private desireNode referenceNode;
 
    private int threshold;
    private boolean desireBiggerThanThreshold;
    private double ratio;
     
    private int oldValue;
     
     
   /*******************
    * GETTERS/SETTERS *
    *******************/
    public desireNode getReferenceNode() 
    {
        return referenceNode;
    }
     
   /****************
    * CONSTRUCTORS *
    ****************/
     
   public influence(desireNode referenceNode, int threshold,
            boolean desireBiggerThanThreshold, double ratio)
   {
        this.referenceNode = 				referenceNode;
        this.threshold = 					threshold;
        this.desireBiggerThanThreshold = 	desireBiggerThanThreshold;
        this.ratio = 						ratio;
        this.oldValue = 					0;
    }
     
    /***********
     * METHODS *
     ***********/
   
   public void changeInfuence(int desire)
   {
   	int newInfluence = 0;
       // If the threshold condition is met
          if((desireBiggerThanThreshold && desire > threshold) || 
              !desireBiggerThanThreshold && desire < threshold) {
              desire -= threshold;
              newInfluence = (int)(desire * ratio);
              referenceNode.setStepInfluencedValue(
              		referenceNode.getInfluencedValue() + newInfluence - oldValue
              		);
              oldValue = newInfluence;
          }
          else {
              referenceNode.setStepInfluencedValue(
              		referenceNode.getInfluencedValue() - oldValue
              		); // nullify influence from this rule
              oldValue = 0;
          }
   }
}