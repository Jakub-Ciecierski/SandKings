package creatures.CreatureClasses;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.GridPoint;

public class MawFinder {
	// singleton
	private static MawFinder instance = new MawFinder();
	public static MawFinder Instance() { return instance; }
	protected MawFinder() 
	{
		initMawRelation();
	}
	
	private List<Maw> _mawList = new ArrayList<Maw>();
	
	public enum MawRelation
	{
		Friend,
		Neutral,
		Enemy
	}
	
	private MawRelation MawRelations[][] = new MawRelation[6][6];
	
	private void initMawRelation(){
		for( int i = 0; i < 6; i++){
			for( int y = 0; y < 6; y++){
				if(i == y || i == 5 || y == 5)
					MawRelations[i][y] = MawRelation.Friend;
				else
					MawRelations[i][y] = MawRelation.Neutral;
			}	
		}
	}
	
	
	public float getBiggestDanger(){
		float maxDanger = 0;
		for(Maw maw : _mawList){
			if(maw.getDanger()> maxDanger)
				maxDanger += maw.getDanger();
		}
		
		return maxDanger;
	}
	public void makeAlliance(int ID1, int ID2){
		MawRelations[ID1][ID2] = MawRelation.Friend;
		MawRelations[ID2][ID1] = MawRelation.Friend;
	}
	public void makeWar(int ID1, int ID2){
		MawRelations[ID1][ID2] = MawRelation.Enemy;
		MawRelations[ID2][ID1] = MawRelation.Enemy;
	}
	public void makeNeutral(int ID1, int ID2){
		MawRelations[ID1][ID2] = MawRelation.Neutral;
		MawRelations[ID2][ID1] = MawRelation.Neutral;
	}
	
	public boolean areWeFriends(int ID1, int ID2)
	{
		return (MawRelations[ID1][ID2] == MawRelation.Friend);
	}
	
	public boolean areWeEnemies(int ID1, int ID2)
	{
		return(MawRelations[ID1][ID2] == MawRelation.Enemy);
	}
	
	public Maw GetMaw( int id )
	{
		for ( Maw m : _mawList ) {
			if ( m.getPlayerID() == id ) return m;
		}
		return null;
	}
	public GridPoint GetMawPosition( int id )
	{
		for ( Maw m : _mawList ) {
			if ( m.getPlayerID() == id ) 
				return m.getGridpos();
		}
		return null;
	}
	public float GetMawStrength( int id )
	{
		for ( Maw m : _mawList ) {
			if ( m.getPlayerID() == id ) 
				return m.getStrength();
		}
		return 0f;
	}
	public double GetDistanceToMaw( int id, int x, int y )
	{
		GridPoint gp = GetMawPosition( id );
		if(gp == null)
			return 0;
		int x1 = gp.getX();
		int x2 = x;
		
		int y1 = gp.getY();
		int y2 = y;
		
		return Math.sqrt( Math.pow(( x2-x1 ), 2) + Math.pow(( y2-y1 ), 2) );
	}
	
	public void AddMaw(Maw m) 
	{	
		_mawList.add(m);
	}
	
	/**
	 * Return agents in given neighborhood
	 * @param extentX
	 * @param extentY
	 */
	public List<Mobile> getFreeAgentsInVicinity(int playerID, int neededSize, int extent) 
	{
		List<Mobile> vicinity = new ArrayList<Mobile>();
		if ( _mawList.size() <= 0 ) return new ArrayList<Mobile>();
		Maw thisMaw = _mawList.get(0);
		for ( Maw m : _mawList )
		{
			if ( m.getPlayerID() == playerID ) 
				thisMaw = m;
		}
		
		// get the grid location of this Human
		GridPoint pt = thisMaw.getGrid().getLocation(thisMaw);
		// use the GridCellNgh class to create GridCells for
		// the surrounding neighborhood .
		GridCellNgh <Mobile> nghCreator = new GridCellNgh <Mobile>(thisMaw.getGrid() , pt,
				Mobile.class , extent , extent);
		
		List <GridCell<Mobile>> gridCells = nghCreator.getNeighborhood(true);
		
		for ( GridCell <Mobile> cell : gridCells ) {
			for(Object obj : thisMaw.getGrid().getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY() )){
				if( obj instanceof Mobile ){
					
					Mobile mobile = (Mobile)obj;
					if ( 
							mobile.getPlayerID() == playerID && 
							!mobile.isInFormation() &&
							!mobile.isGoingSomewhere() &&
							mobile.carriedStuff == null &&
							(mobile.getCurrentTask() == null ||
							mobile.getCurrentTask() != null &&
							mobile.getCurrentTask().isFinished()) &&
							vicinity.size() < neededSize
							//agent.getPlayerID() == playerID && 
						)
						vicinity.add(mobile);
					
				}	
			}
		}
		return vicinity;
	}	

	public List<Maw> getMaws(){
		return this._mawList;
	}
	
	public void removeMaw(Maw maw) {
		this._mawList.remove(maw);
	}
	
	public Maw getMostPowerfullMaw(){

		int maxIndex = 0;
		int max = _mawList.get(maxIndex).getNumberOfChildren();
		int total = 0;
		
		for(int i = 0; i < _mawList.size();i++){
			Maw maw = _mawList.get(i);
			
			int count = maw.getNumberOfChildren();
			
			total += count;
			
			if(max < count){
				maxIndex = i;
				max = count;
			}
		}
		total -= max;
		if(max >= total)
			return _mawList.get(maxIndex);
		else
			return null;
	}
}
