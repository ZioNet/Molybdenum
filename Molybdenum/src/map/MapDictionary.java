package map;

import java.awt.Color;
import java.util.LinkedHashMap;

public class MapDictionary {
	
	LinkedHashMap<Character, Tile> tiles;
	public MapDictionary(){
		tiles = new LinkedHashMap<Character, Tile>();
		createMap();
	}
	public Tile getTile(char tile){
		return tiles.get(tile);
	}
	public void createMap(){
		Color brown = new Color(128,128,16);
		//Walkable
		tiles.put('.', new Tile('.',"Grass", Color.GREEN, false));
		tiles.put(',', new Tile(',',"Dirt Path", brown.darker(), false));
		tiles.put('%', new Tile('%',"Shallow Water", Color.BLUE, false));
		tiles.put(' ', new Tile(' ',"Void", Color.WHITE, false));
		tiles.put('-', new Tile('-',"Wood Floor", Color.orange, false));
		
		//Collide
		tiles.put('X', new Tile('X',"Water", Color.BLUE, true));
		tiles.put('#', new Tile('#',"Stone Wall", Color.GRAY, true));
		tiles.put('+', new Tile('+',"Wood Wall", brown, true));
		tiles.put('*', new Tile('*',"Forest", Color.GREEN.darker().darker(), true));
		tiles.put('_', new Tile('_',"Wood Door", brown.brighter(), true));
		tiles.put('=', new Tile('=',"Sign", brown, true));
	}
	public LinkedHashMap<Character, Tile> getMap(){
		return tiles;
	}
	
}
