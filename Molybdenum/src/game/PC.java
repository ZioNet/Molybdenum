package game;

public class PC {

	public static void main(String[] args){
		Molybdenum mo = new Molybdenum();
		mo.setDrawScale(1f);
		mo.setWidth(800);
		mo.setHeight(600);
		mo.updateWindowSize();
		mo.init();
		mo.gameLoop();
	}

}
