/**
 * 
 */
package creatures;

import java.awt.Color;

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
				return new Color( 0x00, 0xff, 0x00 ); // green
			}
			
			if ( playerID == 4 )
			{
				return new Color( 0xFF, 0x99, 0x00 ); // something
			}
			
			return new Color( 0x00, 0x00, 0x00 );
		}
		
		return super.getColor(agent);
	}
}
