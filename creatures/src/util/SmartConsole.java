package util;

public class SmartConsole {

	private static String PREFIX = " >> ";
	
	private static String SUFFIX = "\n";
	
	static DebugModes[] DEBUG_MDOE = {DebugModes.ADVANCED, DebugModes.TASK};  
	
	public enum DebugModes {
		BASIC,
		ADVANCED,
		TASK,
		ERROR,
		MESSAGE,
		KB,
		SCHEDULER,
		FORMATION,
		GOD;
		
		public String toString(){
			if(this == ADVANCED){
				return "Advanced";
			}
			if(this == BASIC){
				return "Basic";
			}
			if(this == TASK){
				return "Task";
			}
			if(this == ERROR){
				return "Error";
			}
			if(this == MESSAGE){
				return "Message";
			}
			if(this == KB){
				return "KB";
			}
			if(this == SCHEDULER){
				return "Scheduler";
			}
			if(this == FORMATION){
				return "Formation";
			}
			if(this == GOD){
				return "God";
			}
			return "";
		}
	}
	
	public static void Print(String msg, DebugModes debugMode){
		
		boolean doPrint = false;
		
		for(DebugModes workingDebugMode : DEBUG_MDOE){
			if( workingDebugMode == debugMode){
				doPrint = true;
				break;
			} 
		}
		
		if(doPrint){
			System.out.println("["+debugMode.toString()+ "]"+ PREFIX + msg + SUFFIX);
		}
	}
	
}
