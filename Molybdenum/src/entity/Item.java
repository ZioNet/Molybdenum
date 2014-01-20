package entity;

import java.awt.Color;


public class Item extends Entity implements Cloneable{
	
	public String name;
	public String description;
	
	public String type;
	
	public int quantity;
	
	public Item(Color color){
		type = "ITEM";
		this.character = '?';
		this.quantity = 1;
		this.color = color;
		this.name = "Item";
		this.description = "Lorem Ipsum";
	}
	
	public Item clone(){
		Item cloned = new Item(color);
		try {
			cloned = (Item)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		cloned.type = this.type;
		cloned.character = this.character;
		cloned.color = this.color;
		cloned.description = this.description;
		cloned.x = x;
		cloned.y = y;
		cloned.map = map.clone();
		cloned.quantity = quantity;
		
		return cloned;
	}
	
}
