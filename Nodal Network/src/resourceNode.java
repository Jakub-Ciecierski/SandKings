public class resourceNode extends desireNode
{
   /*************
    * VARIABLES *
	*************/


	private int resourceCount;
	private int resourceDesire;
	
   /*******************
    * GETTERS/SETTERS *
	*******************/

	public int getResourceCount() {
		return resourceCount;
	}
	
	public void setResourceCount(int resourceCount) {
		this.resourceCount = resourceCount;
		recalculateBase();
	}
	
	public int getResourceDesire() {
		return resourceDesire;
	}
	
	public void setResourceDesire(int resourceDesire) {
		this.resourceDesire = resourceDesire;
		recalculateBase();
	}
	


   /****************
    * CONSTRUCTORS *
	****************/
	
	public resourceNode(String name){
		super(name);
	}
	
   /***********
    * Methods *
	***********/
	
	private void recalculateBase(){
		if( resourceDesire == 0 ){
			setBase(-50);
			return;
			}
		//TODO: SET HERE SOME MORE INTERESTING FUNCTION
		setBase((int)(((double)resourceCount/(double)resourceDesire)*100));
	}
	
	public int getDesire(){
		int desire = getInfluenced() + getBase();
		if(desire > 100)
			return 100;
		if(desire < 0)
			return 0;
		return desire;	
	}
}
