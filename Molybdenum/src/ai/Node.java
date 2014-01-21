package ai;

public class Node {
	
	private int x,y;
	
	public Node(int x, int y){
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void setLocation(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getManhattanDistanceTo(Node n){
		int dx = Math.abs(n.getX()-getX());
		int dy = Math.abs(n.getY()-getY());
		return dx+dy;
	}
	public int getLinearDistance(Node n){
		int x = n.x;
		int y = n.y;
		
		int t = (y*y)/(x*x);
		
		return (int) Math.sqrt(t);
	}
	
}
