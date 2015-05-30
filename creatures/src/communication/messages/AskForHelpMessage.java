/**
 * 
 */
package communication.messages;

import repast.simphony.space.grid.GridPoint;
import communication.Message;
import creatures.Agent;
import creatures.CreatureClasses.Mobile;

/**
 * @author masli
 *
 */
public class AskForHelpMessage extends Message {
	String content;
	GridPoint gp;
	
	public AskForHelpMessage(String content, GridPoint pt){
		this.content = content;
		this.gp = pt;
	}

	@Override
	public void handle(Agent sender, Agent recipient) {
		Mobile mobile = (Mobile)recipient;
		if (gp != null) {
			//mobile.moveTowardsBro(gp);
		}
		System.out.println("*********************************************************");
		System.out.println("Agent #" + sender.getID() +" To Agent #" +recipient.getID() + "\n");
		System.out.println("Help Message: \n");
		System.out.println(content);
		System.out.println("********************************************************* \n\n");
	}
}
