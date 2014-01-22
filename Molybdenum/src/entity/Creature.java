package entity;

import game.Molybdenum;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import ai.AI;
import ai.Simple;

public class Creature extends Entity {

	public AI ai;
	
	//RP Related Variables
	public Weapon equiped_weapon;
	public Armor equiped_armor;
	//
	
	public int health;
	private int maxHealth;

	public boolean canAct;
	
	public boolean isSelected;

	public int danceLevel;
	public boolean isDancing;

	public String name;
	public String creatureType;

	public int infoCounter;
	public String[] info;

	public ArrayList<Item> inventory;
	public final int invMax = 48;

	public final static int RELATION_NUETRAL = 0;
	public final static int RELATION_HATED = 1;
	public final static int RELATION_LIKED = 2;

	public HashMap<String,Integer> relations;

	public Creature(Color color){
		this.character = 'H';
		this.color = color;

		this.creatureType = "Human";

		this.health = 10;
		this.maxHealth = 10;

		info = new String[5];
		infoCounter = 0;
		inventory = new ArrayList<Item>();

		relations = new HashMap<String,Integer>();
		
		equiped_weapon = new Weapon(color);
		equiped_armor = new Armor(color);
		
		ai = new Simple(this);
	}
	public int getJustMaxHealth(){
		return maxHealth;
	}
	public int getMaxHealth(){
		return maxHealth+equiped_armor.bonus;
	}
	public void addInfo(String sender, String newText){
		newText = "["+sender+"] - "+newText;
		for(int i=0;i<info.length;i++){
			if(info[i]==null){
				info[i] = newText;
				return;
			}else if(info[i]!=null && i==info.length-1){
				String[] temp = new String[info.length];
				for(int c=1;c<info.length;c++){
					temp[c-1] = info[c];
				}
				info = temp;
				info[i] = newText;
			}
		}
	}
	public void move(int x, int y){
		canAct = false;
		//Check for Map Collision
		if(map.tileExists(x, y)){
			if(map.tileWalkable(x, y)){
				if(!getEm().isCreatureAt(this, x,y)){
					this.x = x;
					this.y = y;
				}else if(getEm().isCreatureAt(this, x,y)){
					if(getEm().getEntityAt(this, x, y) instanceof Creature && getEm().getEntityAt(this, x, y)!=this){
						Creature victim = (Creature)getEm().getEntityAt(this, x, y);
						if(name.equals(Molybdenum.settings.PLAYER_NAME) || relationsWith(victim.name)==Creature.RELATION_HATED){
							strike(victim);
						}
					}
				}
			}else{
				getEm().mapCollisionTrigger(this, x, y);
			}
		}
	}
	public void eat(Food item){
		addHealth(item.heal);
		if(item.name.contains("Candy") || item.name.contains("candy")){
			this.color = item.color;
		}
		if(inventory.contains(item)){
			removeItem(inventory.indexOf(item),1);
		}
	}
	public int getWealth(){
		int wealth = 0;
		for(Item i:inventory){
			if(i.name.equals("Gold")){
				wealth+=i.quantity;
			}
		}
		return wealth;
	}
	public int getFoodItemCount(){
		int foods = 0;
		for(Item i:inventory){
			if(i instanceof Food){
				foods++;
			}
		}
		return foods;
	}
	public void addHealth(int heal){
		if(health+heal > getMaxHealth()){
			health = getMaxHealth();
		}else{
			health += heal;
		}
	}
	public boolean addItem(Item item){
		for(Item i:inventory){
			if(i.name.equals(item.name) && i.description.equals(item.description)){
				i.quantity+=item.quantity;
				return true;
			}
		}
		if(inventory.size() >= invMax){
			return false;
		}
		inventory.add(item);
		return true;
	}
	public void strike(Creature c){
		int dmg = this.equiped_weapon.getDamage();
		if(dmg == 0){
			addInfo("COMBAT", "You missed "+c.name+"!");
		}else{
			addInfo("COMBAT", "You hit "+c.name+" for "+dmg+" damage!");
		}
		c.gotHit(this, dmg);
		if(c.health==0){
			addInfo("COMBAT", "You killed "+c.name+"!");
		}
	}
	public void gotHit(Creature attacker,int dmg){
		ai.injured();
		health -= dmg;
		setRelationsWith(attacker.name, Creature.RELATION_HATED);
		if(dmg == 0){
			addInfo("COMBAT", attacker.name+ " missed!");
		}else{
			addInfo("COMBAT", attacker.name+" hit you for "+dmg+" damage!");
		}
	}
	public void dropItem(int item){
		Item i = inventory.get(item).clone();
		i.x = x;
		i.y = y;
		i.quantity = 1;
		i.map = map.clone();

		if(inventory.get(item).quantity > 1){
			inventory.get(item).quantity--;
		}else{
			inventory.remove(item);
		}

		getEm().addEntity(i);
	}
	public void removeItem(int item, int quantity){
		if(inventory.get(item).quantity > 1){
			inventory.get(item).quantity-=quantity;
		}else{
			inventory.remove(item);
		}
	}
	public boolean equip(Item item){
		if(item instanceof Weapon){
			equipWeapon((Weapon)item);
			return true;
		}
		if(item instanceof Armor){
			equipArmor((Armor)item);
			return true;
		}
		return false;
	}
	public void equipWeapon(Weapon weapon){
		if(!equiped_weapon.name.equals("Fists")){
			equiped_weapon.quantity=1;
			addItem(equiped_weapon.clone());
		}
		if(weapon==null){
			weapon = new Weapon(color);
			weapon.map = map.clone();
		}else{
			removeItem(inventory.indexOf(weapon),1);
		}
		equiped_weapon = weapon.clone();
	}
	public void equipArmor(Armor armor){
		if(!equiped_armor.name.equals("Nude")){
			equiped_armor.quantity=1;
			addItem(equiped_armor.clone());
		}
		if(armor==null){
			armor = new Armor(color);
			armor.map = map.clone();
		}else{
			removeItem(inventory.indexOf(armor),1);
		}
		equiped_armor = armor.clone();
	}
	public int relationsWith(String name){
		if(relations.get(name)==null){
			return Creature.RELATION_NUETRAL;
		}
		return relations.get(name);
	}
	public void setRelationsWith(String name,int r){
		relations.put(name, r);
	}
}
