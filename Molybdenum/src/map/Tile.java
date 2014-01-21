package map;

import java.awt.Color;

public class Tile implements Cloneable{
	
	public Tile(){}
	public Tile(char character, String desc, Color color, boolean collision){
		this.type = Tile.NORMAL;
		this.character = character;
		this.collision = collision;
		this.colorFG = color;
		description = desc;
	}
	public Tile(char character, String desc, Color colorFG,Color colorBG, boolean collision){
		this(character,desc,colorFG,collision);
		this.colorBG = colorBG;		
	}
	
	public String description = "Default Tile";
	public Color colorFG = Color.WHITE;
	public Color colorBG = Color.BLACK;
	public boolean collision = true;
	public char character = '~';
	
	//Special
	public static final int NORMAL = 0;
	public static final int INFO   = 1;
	public static final int WARP   = 2;
	
	public int type = Tile.NORMAL;
	public String WARP_NAME;
	public int WARP_X;
	public int WARP_Y;
	public String INFO_TEXT;
	
	public Tile clone() throws CloneNotSupportedException {
		Tile cloned = (Tile)super.clone();

		cloned.type = this.type;
		cloned.character = this.character;
		cloned.collision = this.collision;
		cloned.colorFG = this.colorFG;
		cloned.description = this.description;
		
		return cloned;
	}
	
}
