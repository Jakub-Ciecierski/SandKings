package map;

import repast.simphony.parameter.Parameter;

/**
 * @author Viet Ba
 *	class for Terrarium
 */

public class Terrarium {
	
	private int colourID;
	
	public Terrarium (int colourID) {
		this.colourID = colourID;
	}
	
	@Parameter(displayName = "MapColour", usageName = "colourID")
	public int getColourID() {
		return colourID;
	}
	
	/**
	 * @param foodID the colourID to set
	 */
	public void setColourID(int resourceID) {
		this.colourID = resourceID;
	}

}
