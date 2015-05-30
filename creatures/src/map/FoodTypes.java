/**
 * 
 */
package map;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author Viet Ba
 *
 */
public class FoodTypes extends DefaultStyleOGL2D {
ShapeFactory2D factory;
	
		@Override
        public void init(ShapeFactory2D factory) {
               this.factory = factory;
        }
		
		@Override
		public VSpatial getVSpatial(final Object resource, VSpatial spatial)  {
			final Food food = (Food)resource;
			String projectDir = "icons";
			String iconDir = "pizza.png";
			final int foodID = food.getFoodID();
			if (spatial == null) {
				switch (foodID) {
				case 0:
					iconDir = "pizza.png";
					break;
				case 1:
					iconDir = "donut.png";
					break;
				case 2:
					iconDir = "grape.png";
					break;
				case 3:
					iconDir = "cabbage.png";
					break;
				case 4:
					iconDir = "meat.png";
					break;
				case 5:
					iconDir = "steak.png";
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
