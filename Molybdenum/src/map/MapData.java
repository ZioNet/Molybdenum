package map;

public class MapData implements Cloneable{

	public int WIDTH;
	public int HEIGHT;
	public Tile[][] map;
	public int SPAWN_X;
	public int SPAWN_Y;

	public boolean inPak;
	public String pak;

	public String filename;

	public MapData(){
		init(60,30);
	}
	public void init(int W, int H){
		WIDTH = W;
		HEIGHT = H;
		map = new Tile[HEIGHT][WIDTH];
	}
	public boolean tileExists(int x, int y){
		if(x > 0 && x < WIDTH){
			if(y > 0 && y < HEIGHT){
				return true;
			}
		}
		return false;
	}
	
	public boolean tileWalkable(int x, int y){
		return !map[y][x].collision;
	}
	public boolean tileCollidable(int x, int y){
		return map[y][x].collision;
	}
	
	public MapData clone(){
		MapData clone = this;
		try {
			clone = (MapData)super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("Not Cloneable");
		}
		
		clone.WIDTH = WIDTH;
		clone.HEIGHT = HEIGHT;
		clone.map = map;
		clone.SPAWN_X = SPAWN_X;
		clone.SPAWN_Y = SPAWN_Y;

		clone.inPak = inPak;
		clone.pak = pak;

		clone.filename = filename;
		
		return clone;
	}
}
