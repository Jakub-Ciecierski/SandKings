/**
 * 
 */
package Enemies;

import java.io.IOException;

import map.Food;
import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

/**
 * @author masli
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
		final Enemy enemy = (Enemy)resource;
		String projectDir = System.getProperty("user.dir") + "\\icons\\";
		String iconDir = "spider.png";
		final int enemyID = enemy.getEnemyID();
		if (spatial == null) {
			switch (enemyID) {
			case 0:
				iconDir = "spider.png";
				break;
			case 1:
				iconDir = "snake.png";
				break;
			default:
				break;						
			}
			
			try {
				spatial = factory.createImage(projectDir + iconDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return spatial;	
	}
}
