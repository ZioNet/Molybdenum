package ai;

import map.Tile;
import entity.Creature;
import entity.Entity;
import entity.EntityManager;
import entity.Food;
import entity.Item;
import game.Molybdenum;

public class Simple extends AI {

	public Node node;
	Creature c;

	int wait;
	int waitMax;

	int stuckCount;

	boolean isDetermined;

	public Simple(Creature c){
		super(c);
		Molybdenum.getRandom().nextInt();
		node = new Node(0, 0);
		this.c = c;
		waitMax = Molybdenum.getRandom().nextInt(5)+10;
		isDetermined = false;
	}

	public void generatePath(){
		newNode();
	}

	public void update(int delta){	
		boolean dontdie = !(c.health < c.getMaxHealth()*0.5);

		//Hate the rich
		for(Entity e:c.getEm().getEntities()){
			if(e instanceof Creature && e.map.filename.equals(c.map.filename)){
				Creature n = (Creature)e;
				if(n.getWealth() > c.getWealth() && !n.isDancing){
					c.setRelationsWith(n.name, Creature.RELATION_HATED);
				}else if(n.isDancing){
					c.setRelationsWith(n.name, Creature.RELATION_LIKED);
				}
			}
		}

		//Low Priority: Kill the hated (Only if you aren't dying)
		Node victim = c.getEm().getClosestCreatureWithRelation(c, c.x, c.y, Creature.RELATION_HATED);
		if(victim!=null && dontdie){
			node = victim;
		}

		//Medium Priority: Collect $$$
		Node gold = c.getEm().getClosestItem(c, c.x,c.y,"Gold");
		if(gold!=null && dontdie){
			node = gold;
			pickup();
		}

		//High Priority: Get food to live
		if(c.getFoodItemCount() > 0){
			injured();
		}else{
			Node temp = c.getEm().getClosestFood(c, c.x,c.y);
			if(temp!=null){
				node = temp;
			}
			pickup();
		}
	}
	public void injured(){
		if(c.health < c.getMaxHealth()*0.8){
			for(Item it:c.inventory){
				if(it instanceof Food){
					Food food = (Food)it;
					c.eat(food);
					System.out.println(c.name+" ate "+food.name);//TODO Debug
					break;
				}
			}
		}
	}

	private void pickup(){
		//Item Pickup
		Item i = c.getEm().getItemAt(c,c.x,c.y);
		if(i != null){
			boolean yay = c.addItem(i);
			if(yay){
				c.getEm().getEntities().remove(i);
			}
			System.out.println(c.name+" picked up "+i.name);//TODO Debug
		}
	}

	public void newNode(){
		boolean open = false;
		int nx = Molybdenum.getRandom().nextInt(c.map.WIDTH);
		int ny = Molybdenum.getRandom().nextInt(c.map.HEIGHT);

		while(!open){
			if(!c.map.tileCollidable(nx, ny) || c.map.map[ny][nx].type == Tile.WARP){
				open = true;
			}
			nx = Molybdenum.getRandom().nextInt(c.map.WIDTH);
			ny = Molybdenum.getRandom().nextInt(c.map.HEIGHT);
		}

		node.setLocation(nx, ny);
	}

	public void move(EntityManager em) {
		int nx = c.x;
		int ny = c.y;

		if(c.x < node.getX()){
			nx++;
		}
		if(c.y < node.getY()){
			ny++;
		}
		if(c.x > node.getX()){
			nx--;
		}
		if(c.y > node.getY()){
			ny--;
		}

		c.move(nx, ny);

		if(ny == node.getY() && nx == node.getX()){
			wait++;
			if(wait==waitMax){
				wait=0;
				newNode();
			}
		}else{			
			if(c.x != nx || c.y != ny){
				stuckCount++;
				if(stuckCount == waitMax && !isDetermined){
					stuckCount = 0;
					newNode();
				}
			}
		}
	}

}
