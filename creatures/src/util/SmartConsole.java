package util;

public class SmartConsole {

	private static String PREFIX = " >> ";
	
	static DebugModes[] DEBUG_MDOE = {DebugModes.ADVANCED};  
	
	public enum DebugModes {
		BASIC,
		ADVANCED,
		ERROR
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
			System.out.println(PREFIX + msg);
		}
	}
	
}
