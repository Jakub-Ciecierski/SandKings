/**
 * 
 */
package creatures;

import java.io.File;
import java.io.IOException;

import creatures.CreatureClasses.Maw;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author Viet Ba
 *
 */
public class ColoredMaws extends DefaultStyleOGL2D {
	ShapeFactory2D factory;
	
	@Override
    public void init(ShapeFactory2D factory) {
           this.factory = factory;
    }
	
	@Override
	public VSpatial getVSpatial(final Object resource, VSpatial spatial)  {
		final Maw maw = (Maw)resource;
		String projectDir = "icons";
		String iconDir = "home.png";

		if (spatial == null) {
			switch (maw.getPlayerID()) {
			case 1:
				iconDir = "home-red.png";
				break;
			case 2:
				iconDir = "home-blue.png";
				break;
			case 3:
				iconDir = "home-white.png";
				break;
			case 4:
				iconDir = "home-black.png";
				break;
			default:
				break;						
			}
			
			try {
				spatial = factory.createImage(projectDir + File.separator + iconDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return spatial;	
	}
}
