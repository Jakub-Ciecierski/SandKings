package communication.knowledge.Interpreters;

import communication.knowledge.Information;
import communication.knowledge.Interpreter;
import creatures.Agent;

public class MobileInterpreter extends Interpreter{
	
	/*************** SINGLETON LOGIC ***************/
	private static MobileInterpreter instance = new MobileInterpreter();
	public static MobileInterpreter Instance() { return instance; }
	private MobileInterpreter() { }

	@Override
	public void interpret(Information info, Agent agent) {
		// TODO Auto-generated method stub
		
	}
	
	
}
