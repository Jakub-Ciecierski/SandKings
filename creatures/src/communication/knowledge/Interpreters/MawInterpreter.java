package communication.knowledge.Interpreters;

import communication.knowledge.Information;
import communication.knowledge.Interpreter;
import creatures.Agent;

public class MawInterpreter extends Interpreter{
	
	/*************** SINGLETON LOGIC ***************/
	private static MawInterpreter instance = new MawInterpreter();
	public static MawInterpreter Instance() { return instance; }
	private MawInterpreter() { }
	
	@Override
	public void interpret(Information info, Agent agent) {
		// TODO Auto-generated method stub
		
	}
}
