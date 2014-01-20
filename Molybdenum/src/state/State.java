package state;

public abstract class State {
	
	protected StateManager sm;
	
	public abstract void init();
	public abstract void update(int delta);
	public abstract void render();
	public abstract void input();
	
}
