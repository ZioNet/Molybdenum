package state;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import map.MapData;
import map.MapIO;
import map.Tile;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import ai.Player;
import entity.Armor;
import entity.Creature;
import entity.Entity;
import entity.EntityManager;
import entity.Food;
import entity.Item;
import entity.Weapon;
import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

public class GameState extends State{

	MapIO mio;

	int tw;//Buffer of +2
	int th;//Buffer of -2

	double playerBounceOffset;
	int counter;
	int tileX = 0;
	int tileY = 0;

	int mouseX;
	int mouseY;
	boolean mouseLeft;
	boolean mouseRight;

	boolean invViewing;
	boolean death;
	final int wait = 10000;//10 Seconds
	int respawnWait = wait;

	int rotation;

	MapData privateMap = new MapData();

	public EntityManager em;

	public GameState() {
		em = new EntityManager();
		init();
	}	

	public void init() {
		mio = new MapIO();
		invViewing = false;
		tw = Molybdenum.getText().tw;
		th = Molybdenum.getText().th;
	}

	public void update(int delta) {
		input();
		em.update(delta);
		//Bouncing
		counter += delta;
		playerBounceOffset = Math.sin(counter/300)*4;
		if(counter >= 10000){
			counter = 0;
		}
		rotation += delta;
		//DEATH
		if(em.getPlayer()==null){
			death = true;
			respawnWait-=delta;
		}else{
			death = false;
		}

		if(death && respawnWait <= 1000){ 
			MapData spawn = Molybdenum.getWorldIO().getSpawnMapData();
			respawnWait = wait;
			spawnPlayer(spawn);
		}
	}
	public void render() {
		//Draw black square
		glDisable(GL_TEXTURE_2D);
		Molybdenum.setAwtColor(Color.BLACK);
		Molybdenum.GLQuad(0, 0, Display.getWidth(), Display.getHeight());
		glEnable(GL_TEXTURE_2D);
		Molybdenum.setAwtColor(Color.WHITE);
		//Render Map
		int w = map().WIDTH;
		int h = map().HEIGHT;
		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				Tile currentTile = map().map[y][x];
				Molybdenum.setAwtColor(currentTile.color);
				Molybdenum.getText().drawString(currentTile.character+"", x*tw, y*th, Text.LEFT, 1);
			}
		}
		//Render Entities
		//Molybdenum.setAwtColor(em.getPlayer().color);
		//Molybdenum.getText().drawString(em.getPlayer().character+"", em.getPlayer().x*tw, em.getPlayer().y*th, Text.LEFT, 1);
		for(Entity e:em.getEntities()){
			if(e.map.filename.equals(map().filename)){
				Molybdenum.setAwtColor(e.color);
				boolean dance = false;
				if(e instanceof Creature){
					Creature c = (Creature)e;
					if(c.isDancing){
						dance = true;
					}

				}
				if(dance){
					Molybdenum.setPrettyColorMode(true);
					glPushMatrix();
					glTranslatef(e.x*tw+tw/2, e.y*th+th/2, 0);
					glRotatef(rotation, 0, 0, 1);
					glTranslatef(-(e.x*tw+tw/2), -(e.y*th+th/2), 0);
				}
				Molybdenum.getText().drawString(e.character+"", e.x*tw, e.y*th, Text.LEFT, 1);
				if(dance){
					glPopMatrix();
					Molybdenum.setPrettyColorMode(false);
				}
			}
		}


		//Side Divider
		for(int d=0;d<Display.getHeight()/th+1;d++){
			Molybdenum.setAwtColor(Color.BLUE);
			Molybdenum.getText().drawString("|", w*tw, d*th, Text.LEFT, 1);
		}
		for(int d=h+1;d<Display.getHeight()/th+1;d++){
			Molybdenum.setAwtColor(Color.BLUE);
			Molybdenum.getText().drawString("|", 0*tw, d*th, Text.LEFT, 1);
		}
		//Bottom Divider
		for(int d=0;d<60;d++){
			Molybdenum.setAwtColor(Color.BLUE);
			Molybdenum.getText().drawString("=", d*tw, (h+1)*th, Text.LEFT, 1);
			Molybdenum.getText().drawString("=", d*tw, (42)*th, Text.LEFT, 1);
		}

		//X and Y
		Molybdenum.setAwtColor(Color.GRAY);
		Molybdenum.getText().drawString("Mouse - x:"+mouseX/tw+" y:"+mouseY/th, 0*tw, 30*th, Text.LEFT, 1);

		//PLAYER~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		if(!death){
			//Action Speed
			Molybdenum.setAwtColor(Color.GRAY);
			Molybdenum.getText().drawString("Speed - "+em.speed, 60*tw, 30*th, Text.RIGHT, 1);
			//Move Bar
			int sb = 0;
			try{
				sb = 100*em.speedCounter/em.speed;
			}catch(ArithmeticException e){}
			renderBar(Color.WHITE,69,6,8,sb);


			//Info
			Molybdenum.setAwtColor(Color.WHITE);
			for (int i = 0; i < em.getPlayer().info.length; i++) {
				Molybdenum.getText().drawString(em.getPlayer().info[i], 1 * tw,
						(h + 2 + i) * th, Text.LEFT, 1);
			}
			//Tile Under Player
			Tile tile = map().map[em.getPlayer().y][em.getPlayer().x];
			Molybdenum.setAwtColor(tile.color);
			this.renderTrapazoidTile(tile.character, (w + 2) * tw, 2 * th);
			//Tile Desc
			Molybdenum.setAwtColor(Color.GRAY);
			Molybdenum.getText().drawString(tile.description, 61 * tw, 8 * th,Text.LEFT, 1);
			//Player (w/bouncing)
			Molybdenum.setAwtColor(em.getPlayer().color);
			Molybdenum.getText().drawString(em.getPlayer().character + "",
					(w + 2) * tw + (2 * tw / 3),
					(int) ((1 * th) + playerBounceOffset), Text.LEFT, 4);
			//Player Name
			Molybdenum.setAwtColor(em.getPlayer().color);
			Molybdenum.getText().drawString(em.getPlayer().name, 67 * tw,
					1 * th, Text.LEFT, 1);
			//Creatures on Map
			Molybdenum.setAwtColor(Color.BLUE);
			Molybdenum.getText().drawString("Entities", 61 * tw, 11 * th,
					Text.LEFT, 1f);
			for (int d = 60; d < 80; d++) {
				Molybdenum.getText().drawString("=", d * tw, 12 * th,
						Text.LEFT, 1);
			}
			int pi = 0;
			for (int i=0;i<em.getEntities().size();i++) {
				Entity e = em.getEntities().get(i);
				if(e instanceof Creature && !((Creature) e).name.equals(em.getPlayer().name) && e.map.filename.equals(map().filename)){
					Creature c = (Creature)e;
					Molybdenum.setAwtColor(c.color);
					Molybdenum.getText().drawString(c.character+":", tw*61, (13+pi)*th, Text.LEFT, 1);
					Molybdenum.setAwtColor(Color.GRAY);
					if(c.relationsWith(em.getPlayer().name)==Creature.RELATION_LIKED){
						Molybdenum.setAwtColor(Color.GREEN);
					}
					if(c.relationsWith(em.getPlayer().name)==Creature.RELATION_HATED){
						Molybdenum.setAwtColor(Color.RED);
					}
					Molybdenum.getText().drawString(c.name, tw*63, (13+pi)*th, Text.LEFT, 1);
					pi++;
				}
			}
			//Health Numbers
			int health = em.getPlayer().health;
			int mhealth = em.getPlayer().getMaxHealth();
			Molybdenum.setAwtColor(Color.RED.darker());
			Molybdenum.getText().drawString(health + "/" + mhealth, 69 * tw,
					3 * th - 4, Text.LEFT, 1);
			int hb = 0;
			try{
				hb = 100*em.getPlayer().health/em.getPlayer().getMaxHealth();
			}catch(ArithmeticException e){}
			renderBar(Color.RED,69,4,8,hb);

			//Inventory Mode
			if (invViewing) {
				glDisable(GL_TEXTURE_2D);
				Molybdenum.setAwtColor(Color.BLACK);
				Molybdenum.GLQuad(7 * tw, 6 * th, 61 * tw, 30 * th);
				glEnable(GL_TEXTURE_2D);

				Molybdenum.setAwtColor(Color.WHITE);
				Molybdenum.getText().drawString("Inventory", 8 * tw, 7 * th,Text.LEFT, 1);

				//78x42
				Molybdenum.setAwtColor(Color.BLUE);
				//Horizontal Frames
				for (int d = 1; d < 60; d++) {
					Molybdenum.getText().drawString("=", (d + 7) * tw,
							(6) * th, Text.LEFT, 1);
					Molybdenum.getText().drawString("=", (d + 7) * tw,
							(29 + 6) * th, Text.LEFT, 1);
					Molybdenum.getText().drawString("=", (d + 7) * tw,
							(29 + 4) * th, Text.LEFT, 1);
					Molybdenum.getText().drawString("=", (d + 7) * tw,
							(8) * th, Text.LEFT, 1);
				}
				//Vertical Frames
				for (int d = 3; d < 27; d++) {
					Molybdenum.getText().drawString("+", (19 + 8) * tw,(d + 6) * th, Text.LEFT, 1);
					Molybdenum.getText().drawString("+", (39 + 8) * tw,(d + 6) * th, Text.LEFT, 1);
				}
				for (int d = 1; d < 29; d++) {
					Molybdenum.getText().drawString("+", (7) * tw,(d + 6) * th, Text.LEFT, 1);
					Molybdenum.getText().drawString("+", (60 + 7) * tw,(d + 6) * th, Text.LEFT, 1);
				}
				//Corners
				Molybdenum.getText().drawString("*", 7 * tw, 6 * th,
						Text.LEFT, 1);
				Molybdenum.getText().drawString("*", 7 * tw, 35 * th,
						Text.LEFT, 1);
				Molybdenum.getText().drawString("*", 67 * tw, 6 * th,
						Text.LEFT, 1);
				Molybdenum.getText().drawString("*", 67 * tw, 35 * th,
						Text.LEFT, 1);

				//TODO Equip
				{//WEAPON
					Molybdenum.setAwtColor(em.getPlayer().equiped_weapon.color);
					Molybdenum.getText().drawString(em.getPlayer().equiped_weapon.character + "",
							(8)*tw,// + (2 * tw / 3),
							(int) ((9 * th) + playerBounceOffset), Text.LEFT, 3);
					Molybdenum.setAwtColor(Color.GRAY);
					Molybdenum.getText().drawString("Weapon -", 11*tw, 9*th, Text.LEFT, 1);
					if(!em.getPlayer().equiped_weapon.name.equals("Fists")){
						Molybdenum.setAwtColor(Color.WHITE.darker());
						Molybdenum.getText().drawString("UNEQUIP", 20*tw, 9*th, Text.LEFT, 1);
					}
					Molybdenum.setAwtColor(Color.GRAY);
					Molybdenum.getText().drawString("*", 11*tw, 10*th, Text.LEFT, 1);
					Molybdenum.getText().drawString("*", 11*tw, 11*th, Text.LEFT, 1);
					Molybdenum.setAwtColor(em.getPlayer().equiped_weapon.color);
					Molybdenum.getText().drawString(em.getPlayer().equiped_weapon.name, 13*tw, 10*th, Text.LEFT, 1);
					Molybdenum.getText().drawString("1 - "+em.getPlayer().equiped_weapon.damageMax+" damage", 13*tw, 11*th, Text.LEFT, 1);
				}
				{//ARMOR
					Molybdenum.setAwtColor(em.getPlayer().equiped_armor.color);
					Molybdenum.getText().drawString(em.getPlayer().equiped_armor.character + "",
							(8)*tw,// + (2 * tw / 3),
							(int) ((14 * th) + playerBounceOffset), Text.LEFT, 3);
					Molybdenum.setAwtColor(Color.GRAY);
					Molybdenum.getText().drawString("Armor  -", 11*tw, 14*th, Text.LEFT, 1);
					if(!em.getPlayer().equiped_armor.name.equals("Nude")){
						Molybdenum.setAwtColor(Color.WHITE.darker());
						Molybdenum.getText().drawString("UNEQUIP", 20*tw, 14*th, Text.LEFT, 1);
					}
					Molybdenum.setAwtColor(Color.GRAY);
					Molybdenum.getText().drawString("*", 11*tw, 15*th, Text.LEFT, 1);
					Molybdenum.getText().drawString("*", 11*tw, 16*th, Text.LEFT, 1);
					Molybdenum.setAwtColor(em.getPlayer().equiped_armor.color);
					Molybdenum.getText().drawString(em.getPlayer().equiped_armor.name, 13*tw, 15*th, Text.LEFT, 1);
					Molybdenum.getText().drawString("+"+em.getPlayer().equiped_armor.bonus+" health", 13*tw, 16*th, Text.LEFT, 1);
				}


				//Items
				int row = 0;
				for (int i = 0; i < em.getPlayer().inventory.size(); i++) {
					if(i==24){
						row++;
					}
					if(row == 0){
						Item item = em.getPlayer().inventory.get(i);
						Molybdenum.setAwtColor(item.color);
						Molybdenum.getText().drawStringToWidth(item.character + ":" + item.name, 28 * tw,(i + 9) * th, Text.LEFT, 1,16);
						Molybdenum.getText().drawString("[x" + item.quantity+"]", (47) * tw,(i + 9) * th, Text.RIGHT, 1);
					}
					if(row == 1){
						int m=24;
						Item item = em.getPlayer().inventory.get(i);
						Molybdenum.setAwtColor(item.color);
						Molybdenum.getText().drawStringToWidth(item.character + ":" + item.name, 48 * tw,(i-m + 9) * th, Text.LEFT, 1,16);
						Molybdenum.getText().drawString("[x" + item.quantity+"]", (67) * tw,(i-m + 9) * th, Text.RIGHT, 1);
					}
				}

				//Hover Item
				boolean itemDisplayed = false;
				if (em.getPlayer().inventory.size() > 0) {
					int i = (mouseY / th) - 9;
					int r = -1;
					if (mouseX / tw > 27 && mouseX / tw < 48) {
						r = 0;
					}
					if (mouseX / tw > 47 && mouseX / tw < 68) {
						r = 1;
					}
					if(r != -1){
						if (mouseY / th > 8 && mouseY / th < 29 + 4) {
							i+=24*r;
							if (em.getPlayer().inventory.size() > i){
								Item item = em.getPlayer().inventory.get(i);
								Molybdenum.setAwtColor(item.color);
								Molybdenum.getText().drawString(
										item.type + " - " + item.description
										+ " [x" + item.quantity + "]",
										8 * tw, 34 * th, Text.LEFT, 1);
								itemDisplayed = true;
							}
						}
					}
				}
				if (!itemDisplayed) {
					Molybdenum.setAwtColor(Color.GRAY);
					Molybdenum.getText().drawString(
							"Hover over an item to see a description", 8 * tw, 34 * th, Text.LEFT, 1);
				}
			}

		}else{
			int lw = 21;
			int lh = 5;
			int lx = (Display.getWidth()/tw/2) - (lw/2);
			int ly = (Display.getHeight()/th/2) - (lh/2)-3;
			glDisable(GL_TEXTURE_2D);
			Molybdenum.setAwtColor(new Color(0,0,0,0.85f));
			Molybdenum.GLQuad(0, 0, Display.getWidth(), Display.getHeight());
			Molybdenum.setAwtColor(Color.BLACK);
			Molybdenum.GLQuad(lx*tw, ly*th, (lw)*tw, (lh)*th);
			glEnable(GL_TEXTURE_2D);
			Molybdenum.setAwtColor(Color.RED);
			for(int d=0;d<lw;d++){
				Molybdenum.getText().drawString("=", (d+lx)*tw, (ly)*th, Text.LEFT, 1);
				Molybdenum.getText().drawString("=", (d+lx)*tw, (lh+ly-1)*th, Text.LEFT, 1);
			}
			//Vertical Frames
			for(int d=1;d<lh-1;d++){
				Molybdenum.getText().drawString("+", (lx)*tw, (d+ly)*th, Text.LEFT, 1);
				Molybdenum.getText().drawString("+", (lw+lx-1)*tw, (d+ly)*th, Text.LEFT, 1);
			}
			Molybdenum.getText().drawStringS("YOU ARE DEAD", (int) ((lx+3)*tw), (ly+1)*th, Text.LEFT, 1.3f,3);
			Molybdenum.getText().drawString(respawnWait/1000+"", (int) ((lx+lw/2)*tw), (ly+3)*th, Text.LEFT, 1);
		}
	}

	public void input() {
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		mouseLeft = Mouse.isButtonDown(0);
		mouseRight = Mouse.isButtonDown(1);

		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){}else{
				if(!death){
					if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
						if (em.isItemAt(em.getPlayer(), em.getPlayer().x,em.getPlayer().y)) {
							boolean yay = em.getPlayer().addItem(em.getItemAt(em.getPlayer(), em.getPlayer().x,em.getPlayer().y));
							if(yay){
								em.getEntities().remove(em.getItemAt(em.getPlayer(), em.getPlayer().x,em.getPlayer().y));
							}
						}
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_EQUALS) {
						em.speed += 100;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_MINUS) {
						em.speed -= 100;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_E) {
						invViewing = !invViewing;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
						for(Item i:em.getPlayer().inventory){
							if(i instanceof Food){
								Food food = (Food)i;
								em.getPlayer().eat(food);
								break;
							}
						}
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_Z) {
						em.getPlayer().isDancing = !em.getPlayer().isDancing;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_H) {
						em.getPlayer().health-=5;
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_G) {
						Item gold = new Item(new Color(255,255,64));
						gold.name = "Gold";
						gold.description = "Some funny looking gold";
						gold.quantity = 1;
						gold.map = map();
						gold.character = 'g';

						em.getPlayer().addItem(gold);
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_I) {
						for (Entity e : em.getEntities()) {
							if (e instanceof Creature) {
								Creature c = (Creature) e;
								System.out.println(c.name+" - Health:"+c.health);//TODO Debug
							}
						}
						System.out.println();
					}
					if (Keyboard.getEventKey() == Keyboard.KEY_K) {
						for (Entity e : em.getEntities()) {
							if (e instanceof Creature) {
								Creature c = (Creature)e;
								if(c.map.filename.equals(map().filename)){
									if(!c.name.equals(em.getPlayer().name)){
										c.health--;
										System.out.println(c.name+" has been injured");//TODO Debug
									}
								}
							}
						}
					}
				}
			}
		}
		//Mouse
		while (Mouse.next()){
			if (Mouse.getEventButtonState()) {
				//if (Mouse.getEventButton() == 0) {}
			}else{
				if (Mouse.getEventButton() == 0) {
					if (mouseY/th==9 && (mouseX / tw > 19 && mouseX / tw < 27)) {
						em.getPlayer().equipWeapon(null);
					}
					if (mouseY/th==14 && (mouseX / tw > 19 && mouseX / tw < 27)) {
						em.getPlayer().equipArmor(null);
					}
					if(em.getPlayer().inventory.size() > 0){
						int i = (mouseY / th) - 9;
						int r = -1;
						if (mouseX / tw > 27 && mouseX / tw < 47) {
							r = 0;
						}
						if (mouseX / tw > 47 && mouseX / tw < 67) {
							r = 1;
						}
						if(r != -1){
							if (mouseY / th > 8 && mouseY / th < 29 + 4) {
								i+=24*r;
								if (em.getPlayer().inventory.size() > i){
									Item item = em.getPlayer().inventory.get(i);
									if(item.type.equals("FOOD")){
										if(item instanceof Food){
											Food f = (Food)item;
											em.getPlayer().eat(f);
										}
									}
									if(item.type.equals("WEAPON")){
										if(item instanceof Weapon){
											Weapon f = (Weapon)item;
											em.getPlayer().equip(f);
										}
									}
									if(item.type.equals("ARMOR")){
										if(item instanceof Armor){
											Armor f = (Armor)item;
											em.getPlayer().equip(f);
										}
									}
								}
							}
						}
					}
				}
				if (Mouse.getEventButton() == 1) {
					if(em.getPlayer().inventory.size() > 0){
						int i = (mouseY / th) - 9;
						int r = -1;
						if (mouseX / tw > 27 && mouseX / tw < 48) {
							r = 0;
						}
						if (mouseX / tw > 47 && mouseX / tw < 68) {
							r = 1;
						}
						if(r != -1){
							if (mouseY / th > 8 && mouseY / th < 29 + 4) {
								i+=24*r;
								if (em.getPlayer().inventory.size() > i){
									em.getPlayer().dropItem(i);
								}
							}
						}

					}
				}
			}
		}
	}

	public void spawnPlayer(MapData mapData){
		Creature c = new Creature(Molybdenum.settings.COLOR);
		c.ai = new Player(c);
		c.name = Molybdenum.settings.PLAYER_NAME;
		c.x = mapData.SPAWN_X;
		c.y = mapData.SPAWN_Y;
		c.map = mapData;

		em.addEntity(c);
	}

	public void setMap(MapData mapData){
		this.em.getPlayer().map = mapData;
	}

	public void getMap(String mapName){
		MapData newMap = new MapData();
		try {
			if(em.getPlayer().map.inPak){
				File file = new File(Molybdenum.settings.DIR+Molybdenum.settings.S+"Maps"+Molybdenum.settings.S+em.getPlayer().map.pak);
				newMap = mio.loadPakData(file, mapName);
			}else{
				File file = new File(Molybdenum.settings.DIR+Molybdenum.settings.S+"Maps"+Molybdenum.settings.S+mapName);
				newMap = mio.loadMapData(file);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Molybdenum.exit();
		}
		em.getPlayer().map = newMap;
	}
	private MapData map(){
		if(em.getPlayer()!=null && em.getPlayer().map!=null){
			if(!privateMap.equals(em.getPlayer().map)){
				privateMap = em.getPlayer().map;
			}
			return em.getPlayer().map;
		}else{
			return privateMap;
		}
	}

	private void renderBar(Color c, int x, int y, int w, int percent){
		glDisable(GL_TEXTURE_2D);
		Molybdenum.setAwtColor(c);
		Molybdenum.GLQuad(x*tw-1, y*th-1, (tw*w)+2, (th)+2);
		Molybdenum.setAwtColor(Color.BLACK);
		Molybdenum.GLQuad(x*tw, y*th, (tw*w), (th));
		Molybdenum.setAwtColor(c);
		int nw = (percent*w*tw)/100;
		Molybdenum.GLQuad(x*tw, y*th, nw, (th));
		glEnable(GL_TEXTURE_2D);
	}

	//Other
	private void renderTrapazoidTile(char tile,int x,int y){
		Color old = Molybdenum.getLastAwtColor();
		Texture tileTex = Molybdenum.getText().get(tile);
		int w=tileTex.width,h=tileTex.height;
		float scale = 6;
		int squish = 6;
		//Outline
		glDisable(GL_TEXTURE_2D);
		Molybdenum.setAwtColor(Color.WHITE);
		glBegin(GL_QUADS);
		{
			// Top left corner of the texture
			glTexCoord2f(0, 0);
			glVertex2f(x+squish-1, y+(h*scale/4)-1);

			// Top right corner of the texture
			glTexCoord2f(1, 0);
			glVertex2f((x+w*scale)-squish+1, y+(h*scale/4)-1);

			// Bottom right corner of the texture
			glTexCoord2f(1, 1);
			glVertex2f((x+w*scale)+squish+1, (y+h*scale)-(h*scale/4)+1);

			// Bottom left corner of the texture
			glTexCoord2f(0, 1);
			glVertex2f(x-squish-1, (y+h*scale)-(h*scale/4)+1);
		}
		glEnd();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		Molybdenum.setAwtColor(old);
		//Tile
		tileTex.bind();
		int r = Molybdenum.getLastAwtColor().getRed(),
				g = Molybdenum.getLastAwtColor().getGreen(),
				b = Molybdenum.getLastAwtColor().getBlue(),
				a = Molybdenum.getLastAwtColor().getAlpha()/4;
		Molybdenum.setAwtColor(new Color(r,g,b,a));

		glBegin(GL_QUADS);
		{
			// Top left corner of the texture
			glTexCoord2f(0, 0);
			glVertex2f(x+squish, y+(h*scale/4));

			// Top right corner of the texture
			glTexCoord2f(1, 0);
			glVertex2f((x+w*scale)-squish, y+(h*scale/4));

			// Bottom right corner of the texture
			glTexCoord2f(1, 1);
			glVertex2f((x+w*scale)+squish, (y+h*scale)-(h*scale/4));

			// Bottom left corner of the texture
			glTexCoord2f(0, 1);
			glVertex2f(x-squish, (y+h*scale)-(h*scale/4));
		}
		glEnd();

		glEnable(GL_BLEND);
	}
}