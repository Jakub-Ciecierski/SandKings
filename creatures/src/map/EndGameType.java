/**
 * 
 */
package map;

import java.io.File;
import java.io.IOException;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author Viet Ba
 *
 */
public class EndGameType extends DefaultStyleOGL2D {
	ShapeFactory2D factory;
	
	@Override
    public void init(ShapeFactory2D factory) {
           this.factory = factory;
    }
	
	@Override
	public VSpatial getVSpatial(final Object resource, VSpatial spatial)  {
		final EndGameInfo info = (EndGameInfo)resource;
		String projectDir = "icons";
		String iconDir = "pizza.png";
		final int infoID = info.getEndGameID();
		if (spatial == null) {
			switch (infoID) {
			case 1:
				iconDir = "win-red.png";
				break;
			case 2:
				iconDir = "win-blue.png";
				break;
			case 3:
				iconDir = "win-white.png";
				break;
			case 4:
				iconDir = "win-black.png";
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
