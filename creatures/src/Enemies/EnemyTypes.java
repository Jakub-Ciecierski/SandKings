/**
 * 
 */
package Enemies;

import java.io.File;
import java.io.IOException;

import map.Food;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author Viet Ba
 *
 */
public class EnemyTypes extends DefaultStyleOGL2D {

	ShapeFactory2D factory;
	
	@Override
    public void init(ShapeFactory2D factory) {
           this.factory = factory;
    }
	
	@Override
	public VSpatial getVSpatial(final Object resource, VSpatial spatial)  {
		if (spatial == null) {
		final Enemy enemy = (Enemy)resource;
		String projectDir = "icons";
		String iconDir = "";
		final int enemyID = enemy.getEnemyID();
		
			switch (enemyID) {
			case 0:
				iconDir = "spider.png";
				break;
			case 1:
				iconDir = "snake.png";
				break;
			case 2:
				iconDir = "scorpion.png";
				break;
			default:
				break;						
			}
			
			try {
				System.out.println("ENEMY: " + iconDir + "**********************************");
				spatial = factory.createImage(projectDir + File.separator + iconDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return spatial;	
	}
}
