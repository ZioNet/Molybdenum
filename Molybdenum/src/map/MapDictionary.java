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
		tiles.put('.', new Tile('.',"Grass", Color.GREEN,setHalfOpacity(Color.GREEN.darker(),8), false));
		tiles.put(',', new Tile(',',"Dirt Path", brown.darker(),setHalfOpacity(brown.darker(),4), false));
		tiles.put('%', new Tile('%',"Shallow Water", Color.BLUE,setHalfOpacity(Color.BLUE,4), false));
		tiles.put(' ', new Tile(' ',"Void", Color.WHITE, false));
		tiles.put('-', new Tile('-',"Wood Floor", Color.ORANGE,setHalfOpacity(Color.ORANGE,4), false));
		
		//Collide
		tiles.put('X', new Tile('X',"Water", Color.BLUE,setHalfOpacity(Color.BLUE,4), true));
		tiles.put('#', new Tile('#',"Stone Wall", Color.GRAY,setHalfOpacity(Color.GRAY,4), true));
		tiles.put('+', new Tile('+',"Wood Wall", brown,setHalfOpacity(brown,8), true));
		tiles.put('*', new Tile('*',"Forest", Color.GREEN.darker().darker(),setHalfOpacity(Color.GREEN.darker().darker(),4), true));
		tiles.put('_', new Tile('_',"Wood Door", brown.brighter(),setHalfOpacity(brown.brighter(),4), true));
		tiles.put('=', new Tile('=',"Sign", brown,setHalfOpacity(brown,2), true));
	}
	public LinkedHashMap<Character, Tile> getMap(){
		return tiles;
	}
	private Color setHalfOpacity(Color in,int s){
		Color out = new Color(in.getRed(),in.getGreen(),in.getBlue(),in.getAlpha()/s);
		return out;
	}
	
}
