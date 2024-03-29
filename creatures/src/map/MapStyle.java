/**
 * 
 */
package map;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author Viet Ba
 *
 */

public class MapStyle extends DefaultStyleOGL2D {
	ShapeFactory2D factory;
	
	@Override
        public void init(ShapeFactory2D factory) {
               this.factory = factory;
        }
           
		@Override
	    public Color getColor(final Object colour) {

				final Terrarium r = (Terrarium)colour;
				
				final int colourID = r.getColourID();
				
				if ( colourID == 1 ) //water
				{
					return new Color( 0xE9, 0xCE, 0x46 );
				}
					
				if ( colourID == 2 ) //stone
				{
					return new Color( 0xB2, 0xB2, 0x75); //grey: ( 0x80, 0x80, 0x80 ); 
				}
				
				if ( colourID == 3 ) //sand
				{
					return new Color ( 0xF0, 0xD4, 0x1C ); //yellow: ( 0xFF,0xFF,0x00 );
				}
				
				if ( colourID == 4 ) //wood
				{
					return new Color( 0xEB, 0xE5, 0x46 );
				}
					
			return super.getColor(colourID);
		}
		
		@Override
		public VSpatial getVSpatial(final Object resource, VSpatial spatial) {
			if (spatial == null)
				spatial = factory.createRectangle(15, 15);
			/**
			 * there is something called factory.createImage(path), so later we could put actual images instead of just shapes
			 * our map could be whole green and we would have images of wood etc.
			 * */
			return spatial;	
		}
}
