package entity;

import game.Molybdenum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ai.Node;
import map.MapData;
import map.MapIO;
import map.Tile;
import state.GameState;
import state.StateManager;

public class EntityManager {

	private ArrayList<Entity> entities;

	MapIO mio;

	//Action Timing
	public int speed;
	public int speedCounter;

	public EntityManager(){
		mio = new MapIO();
		entities = new ArrayList<Entity>();
		speed = 200;
	}

	public void addEntity(Entity e){
		entities.add(e);
	}
	public ArrayList<Entity> getEntities(){
		return entities;
	}

	public Creature getPlayer() {
		for(Entity e:entities){
			if(e instanceof Creature){
				Creature c = (Creature)e;
				if(c.name.equals(Molybdenum.settings.PLAYER_NAME)){
					return c;
				}
			}
		}
		return null;
	}
	public boolean playerExists(){
		for(Entity e:entities){
			if(e instanceof Creature){
				Creature c = (Creature)e;
				if(c.name.equals(Molybdenum.settings.PLAYER_NAME)){
					return true;
				}
			}
		}
		return false;
	}
	public void update(int delta){
		
		speedCounter += delta;
		if(speedCounter >= speed){
			speedCounter = 0;
		}
		for(int i=0;i<entities.size();i++){
			Entity e = entities.get(i);
			if(e instanceof Creature){
				Creature c = (Creature)e;
				c.ai.update(delta);
				if(speedCounter == 0){
					c.canAct = true;
				}
				if(c.health <= 0){
					c.health = 0;
					kill(c);
					entities.remove(e);
				}
				updateInfoCounters(c,delta);
				if(c.canAct){
					c.ai.move(this);
					//c.canAct = false;
				}

			}
		}

	}
	public void kill(Creature c){
		Food food = new Food(c.color);
		food.character = 'f';
		food.name = c.creatureType+" flesh";
		food.description = "The flesh of "+c.name;
		food.quantity = (Molybdenum.getRandom().nextInt(c.getMaxHealth())/2)+1;
		food.map = c.map.clone();
		food.x = c.x;
		food.y = c.y;
		
		c.equipArmor(null);
		c.equipArmor(null);
		
		ArrayList<Item> inv = c.inventory;
		for(Item i:inv){
			i.x = c.x;
			i.y = c.y;
			i.map = c.map.clone();
			this.addEntity(i);
		}
		this.addEntity(food);
	}
	public void setEntities(ArrayList<Entity> e){
		entities = e;
	}
	private void updateInfoCounters(Creature c, int delta){
		int maxCount = 5000;

		if(c.info[0] != null){
			c.infoCounter+=delta;
		}
		if(c.infoCounter>=maxCount){
			c.infoCounter = 0;
			String[] temp = new String[c.info.length];
			for(int i=1;i<c.info.length;i++){
				temp[i-1] = c.info[i];
			}
			c.info = temp;
		
		
		}
	}
	public Entity getEntityAt(Entity ent, int x, int y){
		for(Entity e: entities){
			if(e.map.filename.equals(ent.map.filename)){
				if(e.x == x && e.y == y){
					return e;
				}
			}
		}
		return null;
	}
	public Item getItemAt(Entity ent, int x, int y){
		for(Entity e: entities){
			if(e instanceof Item){
				Item i = (Item)e;
				if(i.map.filename.equals(ent.map.filename)){
					if(i.x == x && i.y == y){
						return i;
					}
				}
			}
		}
		return null;
	}
	public boolean isCreatureAt(Entity c, int x, int y){
		for(Entity e: entities){
			if(e.map.filename.equals(c.map.filename)){
				if(e instanceof Creature){
					if(e.x == x && e.y == y){
						return true;
					}
				}
			}
		}
		return false;
	}
	public boolean isItemAt(Entity en, int x, int y){
		for(Entity e: entities){
			if(e.map.filename.equals(en.map.filename)){
				if(e instanceof Item){
					if(e.x == x && e.y == y){
						return true;
					}
				}
			}
		}
		return false;
	}

	public Node getClosestFood(Entity c,int x, int y){
		Node location = new Node(x,y);

		Node best = null;
		int bestD  = 1000;

		for(Entity e: entities){
			if(e.map.filename.equals(c.map.filename)){
				if(e instanceof Food){
					Node current = new Node(e.x,e.y);
					if(location.getManhattanDistanceTo(current) < bestD){
						bestD = location.getManhattanDistanceTo(current);
						best = current;
					}
				}
			}
		}
		return best;
	}
	public Node getClosestCreatureWithRelation(Creature c,int x, int y, int r){
		Node location = new Node(x,y);

		Node best = null;
		int bestD  = 1000;

		for(int index=0;index<entities.size();index++){
			Entity e = entities.get(index);
			if(e.map.filename.equals(c.map.filename)){
				if(e instanceof Creature){
					Creature curC = (Creature)e;
					if(c.relationsWith(curC.name)==r){
						Node current = new Node(e.x,e.y);
						if(location.getManhattanDistanceTo(current) < bestD){
							bestD = location.getManhattanDistanceTo(current);
							best = current;
						}
					}
				}
			}
		}

		return best;
	}
	public Node getClosestItem(Entity c,int x, int y,String name){
		Node location = new Node(x,y);

		Node best = null;
		int bestD  = 1000;

		for(Entity e: entities){
			if(e.map.filename.equals(c.map.filename)){
				if(e instanceof Item){
					Item i = (Item)e;
					if(i.name.equalsIgnoreCase("gold")){
						Node current = new Node(e.x,e.y);
						if(location.getManhattanDistanceTo(current) < bestD){
							bestD = location.getManhattanDistanceTo(current);
							best = current;
						}
					}
				}
			}
		}
		return best;
	}
	public void mapCollisionTrigger(Creature c, int x, int y){
		if(c.map.tileExists(x,y)){
			//Sign
			if(c.map.map[y][x].type == Tile.INFO){
				c.addInfo("Sign", c.map.map[y][x].INFO_TEXT);
			}
			//Player Warp
			if(c.map.map[y][x].type == Tile.WARP){
				((GameState) Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).em.warp(c,c.map.map[y][x]);
			}
		}
	}
	public void warp(Creature c,Tile tile){
		c.x = tile.WARP_X;
		c.y = tile.WARP_Y;
		MapData newMap = new MapData();
		boolean pakMode = c.map.inPak;
		String filename = tile.WARP_NAME;
		String pak = c.map.pak;
		try {
			if(pakMode){
				File file = new File(Molybdenum.settings.DIR+Molybdenum.settings.S+"Maps"+Molybdenum.settings.S+pak);
				newMap = mio.loadPakData(file, filename).clone();
			}else{
				File file = new File(Molybdenum.settings.DIR+Molybdenum.settings.S+"Maps"+Molybdenum.settings.S+filename);
				newMap = mio.loadMapData(file).clone();
			}
		} catch (IOException e) {
			e.printStackTrace();
			Molybdenum.exit();
		}
		c.map = newMap;
	}
}
