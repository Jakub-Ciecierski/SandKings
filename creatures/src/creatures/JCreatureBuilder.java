package creatures;

import communication.MessageQueue;

import map.God;
import map.Terrarium;
import Constants.Constants;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.MawFinder;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class JCreatureBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		MessageQueue messageQueue = MessageQueue.Instance();

		context.add(messageQueue);
		
		System.out.println(RandomHelper.getSeed());
		context.setId("creatures");
		
		// init grid and space
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory ( null );
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace ( 
				"space", 
				context, 
				new RandomCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.StrictBorders (), Constants.GRID_SIZE, Constants.GRID_SIZE);
		GridFactory gridFactory = GridFactoryFinder . createGridFactory ( null );
		Grid<Object> grid = gridFactory.createGrid ( "grid", context, new GridBuilderParameters<Object>(new StrictBorders(),
				new SimpleGridAdder<Object>(), true, Constants.GRID_SIZE, Constants.GRID_SIZE));
		
		// add God
		God god = new God(space, grid);

		// create maws for 4 players with random initial power
		Maw player1Maw = new Maw( space, grid, 1, RandomHelper.nextIntFromTo(10, 20) );
		Maw player2Maw = new Maw( space, grid, 2, RandomHelper.nextIntFromTo(10, 20) );
		Maw player3Maw = new Maw( space, grid, 3, RandomHelper.nextIntFromTo(10, 20) );
		Maw player4Maw = new Maw( space, grid, 4, RandomHelper.nextIntFromTo(10, 20) );
		
		// add maws to context
		context.add( god );
		context.add( player1Maw );
		context.add( player2Maw );
		context.add( player3Maw );
		context.add( player4Maw );
		
		//add maws to list
		MawFinder.Instance().AddMaw( player1Maw );
		MawFinder.Instance().AddMaw( player2Maw );
		MawFinder.Instance().AddMaw( player3Maw );
		MawFinder.Instance().AddMaw( player4Maw );
		// place all objects in the context 
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		// init corner coords
		GridPoint bottomleft 	= new GridPoint(  5,  5 );
		GridPoint bottomright	= new GridPoint( Constants.GRID_SIZE - 5,  5 );
		GridPoint topleft		= new GridPoint( Constants.GRID_SIZE - 5, Constants.GRID_SIZE - 5 );
		GridPoint topright 		= new GridPoint(  5, Constants.GRID_SIZE - 5 );
		
		// assign maw coords
		player1Maw.setGridpos(topleft);
		player2Maw.setGridpos(topright);
		player3Maw.setGridpos(bottomleft);
		player4Maw.setGridpos(bottomright);
		
		// move maw 1
		grid.moveTo(player1Maw,  (int)topleft.getX(), (int)topleft.getY() );
		space.moveTo(player1Maw,  (int)topleft.getX(), (int)topleft.getY());
		
		// move maw 2
		grid.moveTo(player2Maw, (int)topright.getX(), (int)topright.getY());
		space.moveTo(player2Maw,  (int)topright.getX(), (int)topright.getY());
		
		// move maw 3
		grid.moveTo(player3Maw,		(int)bottomleft.getX(), (int)bottomleft.getY());
		space.moveTo(player3Maw,	(int)bottomleft.getX(), (int)bottomleft.getY());
		
		// move maw 4
		grid.moveTo(player4Maw,		(int)bottomright.getX(), (int)bottomright.getY());
		space.moveTo(player4Maw,	(int)bottomright.getX(), (int)bottomright.getY());
		
		// create map
		for (int i = 0; i < Constants.GRID_SIZE; i++)
			for (int j = 0; j < Constants.GRID_SIZE; j++) {
				int rand = RandomHelper.nextIntFromTo(0, 1);
				int colour = 0;
				//there is more of colour 4 so it doesn't look too random
				if(rand == 0) {
					 colour = RandomHelper.nextIntFromTo(1, 3);
				}
				else colour = 4;
				
				Terrarium resource = new Terrarium(space, grid, colour);
				context.add( resource );
				NdPoint point = new NdPoint( i, j );
				grid.moveTo(resource, (int)point.getX(), (int)point.getY());
				space.moveTo(resource, (int)point.getX(), (int)point.getY());
			}
		
		
		// don't loop endlessly
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(200);
		}
		
		return context;
	}
}
