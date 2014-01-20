package state;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.Map;

import map.MapData;
import map.MapDictionary;
import map.MapIO;
import map.Tile;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import entity.Creature;
import game.Molybdenum;
import graphics.Text;

public class MapEditorState extends State{

	MapDictionary dictionary;
	MapData mapData;
	MapIO mio;

	int tw = 10;//Buffer of +2
	int th = 14;//Buffer of -2
	
	int playerX = -1;
	int playerY = -1;

	char selectedTileChar = '.';

	public MapEditorState() {
		init();
	}

	public void init() {
		dictionary = new MapDictionary();
		mio = new MapIO();
		mapData = mio.createBasicMap();
	}


	public void update(int delta) {
		input();

		//Map Replace
		if(mouseLeft){
			if(mouseX/tw < mapData.WIDTH && mouseX/tw >= 0){
				if(mouseY/th < mapData.HEIGHT && mouseY/th >= 0){
					mapData.map[mouseY/th][mouseX/tw] = dictionary.getTile(selectedTileChar);//TODO Fillmode
				}
			}
		}
		//Player Location
		int hoverX = mouseX/tw;
		int hoverY = mouseY/th;
		if(mouseRight){
			if(hoverX >= 0 && hoverX < mapData.WIDTH){
				if(hoverY >= 0 && hoverY < mapData.HEIGHT){
					playerX = hoverX;
					playerY = hoverY;
				}
			}
		}
		//Tile Selection
		//Starts at 61x10 is always GRASS
		if(mouseLeft){
			if(mouseY/th == 10){
				int X = mouseX/tw - 61;
				if(X >=0 && X < dictionary.getMap().keySet().toArray().length){
					char s = (char) dictionary.getMap().keySet().toArray()[X];
					selectedTileChar = s;
				}
			}
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
		int w = mapData.WIDTH;
		int h = mapData.HEIGHT;
		for(int x=0;x<w;x++){
			for(int y=0;y<h;y++){
				Tile currentTile = mapData.map[y][x];
				Molybdenum.setAwtColor(currentTile.color);
				Molybdenum.getText().drawString(currentTile.character+"", x*tw, y*th, Text.LEFT, 1);
			}
		}
		//Side Divider
		for(int d=0;d<Display.getHeight()/th+1;d++){
			Molybdenum.setAwtColor(Color.BLUE);
			Molybdenum.getText().drawString("|", w*tw, d*th, Text.LEFT, 1);
		}

		//File Keys
		Molybdenum.setAwtColor(Color.GRAY);
		Molybdenum.getText().drawString(
				"[<Ctrl+S> to save] | "
						+ "[<Ctrl+L> to load] | "
						+ "[Right Click to set Player]", 0, h*th, Text.LEFT, 1);

		//X and Y
		Molybdenum.setAwtColor(Color.GRAY);
		Molybdenum.getText().drawString("x:"+mouseX/tw+" y:"+mouseY/th, 20, Display.getHeight()-26, Text.LEFT, 1);
		
		//Player
		Molybdenum.setAwtColor(Color.YELLOW);
		Molybdenum.getText().drawString("H", playerX*tw, playerY*th, Text.LEFT, 1);
		
		//Selected
		Tile tile = dictionary.getTile(selectedTileChar);
		Molybdenum.setAwtColor(tile.color);
		Molybdenum.getText().drawString(selectedTileChar+"", 2, Display.getHeight()-42, Text.LEFT, 2);

		//Hover
		int hoverX = mouseX/tw;
		int hoverY = mouseY/th;
		if(hoverX >= 0 && hoverX < w){
			if(hoverY >= 0 && hoverY < h){
				Tile hoverTile = mapData.map[hoverY][hoverX];
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_BLEND);
				Molybdenum.setAwtColor(Color.WHITE);
				Molybdenum.GLQuad((w+2)*tw-1, 2*th-1, tw*4+2, th*4+2);
				glEnable(GL_TEXTURE_2D);
				Molybdenum.setAwtColor(hoverTile.color);
				Molybdenum.getText().get(hoverTile.character).bind();;
				Molybdenum.GLQuad((w+2)*tw, 2*th, tw*4, th*4);
				glEnable(GL_BLEND);
				//Hover Desc
				Molybdenum.setAwtColor(Color.GRAY);
				Molybdenum.getText().drawString(hoverTile.description, 62*tw, 6*th, Text.LEFT, 1);
			}
		}

		//Palette
		int nX=61,nY=10;
		Molybdenum.setAwtColor(Color.GRAY);
		Molybdenum.getText().drawString("Palette", (nX)*tw, (nY-2)*th, Text.LEFT, 1);
		for (@SuppressWarnings("rawtypes") Map.Entry entry : dictionary.getMap().entrySet()) {
			char currentChar = (char) entry.getKey();
			Tile currentTile = (Tile) entry.getValue();
			Molybdenum.setAwtColor(currentTile.color);
			Molybdenum.getText().drawString(currentChar+"", nX*tw, nY*th, Text.LEFT, 1);
			nX++;
		}
		for(int d=nX-11;d<(Display.getWidth()/tw);d++){
			Molybdenum.setAwtColor(Color.BLUE);
			Molybdenum.getText().drawString("=", d*tw, (nY-1)*th, Text.LEFT, 1);
		}


	}

	int mouseX,mouseY;
	boolean mouseLeft,mouseRight;
	public void input() {
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		mouseLeft = Mouse.isButtonDown(0);
		mouseRight = Mouse.isButtonDown(1);
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){}else{
				if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					Creature p = new Creature(Color.YELLOW);
					p.x = playerX;
					p.y = playerY;
					p.name = Molybdenum.settings.PLAYER_NAME;
					mio.saveMap(mapData);
				} 
				if (Keyboard.getEventKey() == Keyboard.KEY_L && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					mapData = mio.loadMap();
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
					System.out.print(mapData.map[mouseY/th][mouseX/tw].character);
				}
			}
		}
	}

}
