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
public class EventsInfoTypes extends DefaultStyleOGL2D {
	ShapeFactory2D factory;
	
	@Override
    public void init(ShapeFactory2D factory) {
           this.factory = factory;
    }
	
	@Override
	public VSpatial getVSpatial(final Object resource, VSpatial spatial)  {
		final EventsInfo event = (EventsInfo)resource;
		String projectDir = "icons";
		String iconDir = "starvation.png";
		if (spatial == null) {
			switch (event.getEventType()) {
			case Defense:
				iconDir = "shield.png";
				break;
			case Attack:
				iconDir = "sword.png";
				break;
			case Alliance:				
				iconDir = "alliance.png";
				break;
			case Starvation:
				iconDir = "starvation.png";
				break;
			case War:
				iconDir = "war.png";
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
