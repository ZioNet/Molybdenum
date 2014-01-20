package entity;

import java.awt.Color;

public class Food extends Item {
	
	public int heal;
	
	public Food(Color color) {
		super(color);
		heal = 1;
		type = "FOOD";
	}

}
