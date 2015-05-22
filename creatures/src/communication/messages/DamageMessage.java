package communication.messages;

import communication.Message;
import creatures.Agent;
import creatures.Fightable;
import creatures.CreatureClasses.Mobile;

public class DamageMessage extends Message{

	float damage = 0;
	public DamageMessage(float damage)
	{
		this.damage = damage;
	}
	

	@Override
	public void handle(Agent sender, Agent recipient) {
		Fightable mobile = (Fightable)recipient;
		mobile.dealDamage(damage);
	}
}
