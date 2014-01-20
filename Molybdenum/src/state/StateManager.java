package state;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public class StateManager {
	
	/*Private Variables*/
	private ArrayList<State> states;
	private int currentState = 0;
	
	/*Static Variables*/
	public static final int HOMESTATE = 0;
	public static final int GAMESTATE = 1;
	public static final int MAPEDITORSTATE = 2;
	public static final int MAPSELECTSTATE = 3;
	public static final int HELPSTATE = 4;
	public static final int PROPSTATE = 5;
	public static final int TESTSTATE = 6;
	
	
	public StateManager(){
		states = new ArrayList<State>();
		states.add(new HomeState());//0
		states.add(new GameState());//1
		states.add(new MapEditorState());//2
		states.add(new MapSelectState());//3
		states.add(new HelpState());//4
		states.add(new PropState());//5
		states.add(new TestState());//6
	}
	
	public void setState(int state){
		currentState = state;
		states.get(state).init();
	}
	public State getState(int state){
		return states.get(state);
	}
	public void update(int delta){
		//Home Button
		if (Keyboard.getEventKey() == Keyboard.KEY_TAB) {
			this.setState(StateManager.HOMESTATE);
		}
		states.get(currentState).update(delta);
	}
	public void render(){
		states.get(currentState).render();
	}
	
}
