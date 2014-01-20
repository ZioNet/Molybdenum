package entity;

import java.awt.Color;

public class Armor extends Item {
	
	public int bonus;
	
	public Armor(Color color) {
		super(color);
		this.type = "ARMOR";
		this.character = '?';
		this.name = "Nude";
		this.description = "Provides bodily freedom";
		this.bonus = 0;
	}
	
	public Armor clone(){
		Armor cloned = new Armor(color);
		cloned = (Armor)super.clone();
		
		cloned.bonus = bonus;
		
		return cloned;
	}

}
