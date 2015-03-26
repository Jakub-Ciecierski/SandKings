package creatures;

import map.Resources;
import creatures.CreatureClasses.Maw;
import creatures.CreatureClasses.Worker;
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
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.valueLayer.GridValueLayer;

public class JCreatureBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		System.out.println(RandomHelper.getSeed());
		context.setId("creatures");
		
		// init grid and space
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory ( null );
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace ( 
				"space", 
				context, 
				new RandomCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.StrictBorders (), 50, 50);
		GridFactory gridFactory = GridFactoryFinder . createGridFactory ( null );
		Grid<Object> grid = gridFactory.createGrid ( "grid", context, new GridBuilderParameters<Object>(new StrictBorders(),
				new SimpleGridAdder<Object>(), true, 50, 50));
		
		// create maws for 4 players with random initial power
		Maw player1Maw = new Maw( space, grid, 1, RandomHelper.nextIntFromTo(10, 20) );
		Maw player2Maw = new Maw( space, grid, 2, RandomHelper.nextIntFromTo(10, 20) );
		Maw player3Maw = new Maw( space, grid, 3, RandomHelper.nextIntFromTo(10, 20) );
		Maw player4Maw = new Maw( space, grid, 4, RandomHelper.nextIntFromTo(10, 20) );
		
		// add maws to context
		context.add( player1Maw );
		context.add( player2Maw );
		context.add( player3Maw );
		context.add( player4Maw );
		
		// place all objects in the context 
		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		
		// init corner coords
		NdPoint bottomleft  = new NdPoint(  5,  5 );
		NdPoint bottomright = new NdPoint( 45,  5 );
		NdPoint topleft		= new NdPoint( 45, 45 );
		NdPoint topright 	= new NdPoint(  5, 45 );
		
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
		
		for (int i = 0; i < 50; i++)
			for (int j = 0; j < 50; j++) {
				int rand = RandomHelper.nextIntFromTo(0, 1);
				int resourceType = 0;
				if(rand == 0) {
					if(i > 3 & i < 47 & j > 3 & j < 47) { //inner part of the map
								resourceType = RandomHelper.nextIntFromTo(1, 3); // + water
					}
					else resourceType = RandomHelper.nextIntFromTo(2, 3); // outter part
				}
				else resourceType = 4; //more wood in general
				
				Resources resource = new Resources(space, grid, resourceType);
				context.add( resource );
				NdPoint point = new NdPoint( i, j );
				grid.moveTo(resource, (int)point.getX(), (int)point.getY());
				space.moveTo(resource, (int)point.getX(), (int)point.getY());
			}
		
		// don't loop endlessly
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(200);
		}
		
		final GridValueLayer valueLayer = new GridValueLayer(Constants.Constants.LAYER_ID,
															true, new WrapAroundBorders(),
														50, 50);
		context.addValueLayer(valueLayer);
		/*for(int i = 0; i < 50; i++)	
			for (int j = 0; i < 50; j++)
					valueLayer.set(50, i, j);*/
		return context;
	}

}
