public class influence 
{
   /*************
	* VARIABLES *
	*************/
	
	private desireNode referenceNode;
	private desireNode thisNode;

	private double ratio;
	
	private int oldValue;
	
	
   /*******************
    * GETTERS/SETTERS *
	*******************/
	public desireNode getReferenceNode() {
		return referenceNode;
	}
	
	public double getRatio() {
	return ratio;
}

	public desireNode getThisNode() {
		return thisNode;
	}
   /****************
    * CONSTRUCTORS *
	****************/
	
   public influence(desireNode referenceNode, desireNode thisNode, double ratio) {
		this.referenceNode = referenceNode;
		this.thisNode = thisNode;
		this.ratio = ratio;
		this.oldValue = 0;
	}
	
	/***********
	 * METHODS *
	 ***********/
	
	public void recalculateReferenceInfluence(int desire)
	{
		int newInfluence = 0;
		newInfluence = (int)(desire * ratio);
		referenceNode.setInfluenced(referenceNode.getInfluenced() + newInfluence - oldValue);
		oldValue = newInfluence;
	}
}
