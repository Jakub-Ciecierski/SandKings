package map;

import java.awt.Color;

import repast.simphony.valueLayer.ValueLayer;
import repast.simphony.visualizationOGL2D.ValueLayerStyleOGL;

/**
 * @author VietBa
 *	class for map layer
 */
public class mapValueLayer implements ValueLayerStyleOGL {
	private ValueLayer layer = null;

    @Override
    public void init(final ValueLayer layer) {
            this.layer = layer;
    }

    @Override
    public float getCellSize() {
            return 15.0f;
    }

    @Override
    public Color getColor(final double... coordinates) {
            final double coords = layer.get(coordinates);
            if(coords < 0) {
    			throw new IllegalStateException(String.format("Coordinates cannot be negative."));
    		}
            //System.out.printf("\n Coordinates: [%f]", coords);
            final int strength = (int) Math.min(200 * coords, 255);
            //System.out.printf("\n Strength: [%d]", strength);

            return new Color(0, strength, 10); // 0x000000 - black, 0x00FF00 - green
    }

}
