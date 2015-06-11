package util;

public class SmartConsole {

	private static String PREFIX = " >> ";
	
	private static String SUFFIX = "\n";
	
	//static DebugModes[] DEBUG_MDOE = {DebugModes.ADVANCED, DebugModes.TASK, DebugModes.ALLIANCE};  
	static DebugModes[] DEBUG_MDOE = {DebugModes.WAR};
	
	public enum DebugModes {
		STDOUT,
		BASIC,
		ADVANCED,
		TASK,
		TASK_FOOD,
		ERROR,
		MESSAGE,
		KB,
		SCHEDULER,
		FORMATION,
		GOD,
		ALLIANCE,
		WAR,
		STATS;
		
		public String toString(){
			if(this == STDOUT){
				return "stdout";
			}
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
			if(this == ALLIANCE){
				return "Alliance";
			}
			if(this == TASK_FOOD){
				return "Task Food";
			}
			if(this == WAR){
				return "War";
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
		if(debugMode == DebugModes.STDOUT || debugMode == DebugModes.STATS){
			doPrint = true;
		}
		
		if(doPrint){
			if(debugMode == DebugModes.STATS)
				System.out.println(msg + SUFFIX);
			else
				System.out.println("["+debugMode.toString()+ "]"+ PREFIX + msg + SUFFIX);
		}
		
	}
	
}
