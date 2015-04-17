package communication.rules;

public class RuleSet {
	public boolean Dig;
	
	public boolean Build;

	public boolean Smell;
	
	public boolean SetUpCamp(){
		return Dig && Build;
	}

	public boolean Search(){
		return Dig && Smell;
	}
	
	public RuleSet(){
		
	}
	
	public RuleSet(boolean Dig, boolean Build, boolean Smell){
		this.Dig = Dig;
		this.Build = Build;
		this.Smell = Smell;
	}

}
