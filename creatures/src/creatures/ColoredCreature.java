/**
 * 
 */
package creatures;

import java.awt.Color;

import creatures.CreatureClasses.MawFinder;
import creatures.CreatureClasses.Mobile;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

/**
 * @author Asmodiel
 *
 */
public class ColoredCreature extends DefaultStyleOGL2D {
	@Override
    public Color getColor(final Object agent) {
		if ( agent instanceof Mobile )
		{
			final Mobile mob = (Mobile) agent;
			
			final int playerID = mob.getPlayerID();
			
			if ( playerID == 1 )
			{
				return new Color( 0xFF, 0x00, 0x00 ); // red
			}
			
			if ( playerID == 2 )
			{
				return new Color( 0x00, 0x00, 0xff ); // blue
			}
			
			if ( playerID == 3 )
			{
				return new Color( 0xff, 0xff, 0xff ); // white
			}
			
			if ( playerID == 4 )
			{
				return new Color( 0x00, 0x00, 0x00 ); // black
			}
			
			return new Color( 0x00, 0x00, 0x00 );
		}
		
		return super.getColor(agent);
	}
	
	@Override
	public float getScale(final Object agent) {
		
		if ( agent instanceof Mobile )
		{
			final Mobile mob = (Mobile) agent;
			return (1 + (MawFinder.Instance().GetMawStrength( mob.getPlayerID() )));	
		}
		
		return 1;
	}
}
