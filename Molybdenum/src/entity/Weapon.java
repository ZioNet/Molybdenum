package entity;

import game.Molybdenum;

import java.awt.Color;

public class Weapon extends Item implements Cloneable{
	
	public int damageMax;
	public int damageMin;
	
	public Weapon(Color color) {
		super(color);
		this.type = "WEAPON";
		this.character = '?';
		this.name = "Fists";
		this.description = "Provides powerful imapacts of force";
		damageMax =  1;
		damageMin = -1;
	}
	
	public int getDamage(){
		int dmg = Molybdenum.getRandom().nextInt(damageMax-damageMin+1)+damageMin;
		if(dmg < 0){
			dmg = 0;
		}
		return dmg;
	}
	
	public Weapon clone(){
		Weapon cloned = new Weapon(color);
		cloned = (Weapon)super.clone();
		
		cloned.damageMax = damageMax;
		cloned.damageMin = damageMin;
		
		return cloned;
	}
	
}
