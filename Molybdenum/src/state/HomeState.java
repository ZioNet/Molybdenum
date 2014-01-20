package state;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

public class HomeState extends State {

	Texture bg;
	
	int item;
	String[] items = {
		"Enter Molybdenum",
		"Map Editor",
		"Keys",
		"Settings",
		"Test State",
		"Exit"
	};
	
	public HomeState() {
		init();
	}
	public void init() {
		bg = Texture.loadTexture("res/title.png");
	}
	public void update(int delta) {
		input();
	}
	public void render() {
		bg.bind();
		Molybdenum.GLQuad(0, 0, Display.getWidth(), Display.getHeight());
		
		Molybdenum.setAwtColor(Color.WHITE);
		Molybdenum.getText().drawStringS("Molybdenum | Home", 16, 16, Text.LEFT, 3f,4);
		
		//Render all selections
		for(int i=0;i<items.length;i++){
			String current = items[i];
			int xset = 0;
			int s = 0;
			float f = 0;
			if(i==item){
				current = "> "+current;
				xset = Molybdenum.getText().getWidth(">  ", 2.5f);
				s = 1;
				f = 0.2f;
			}
			Molybdenum.getText().drawStringS(current, 256-xset, 128+64*i, Text.LEFT, 2.5f+f,4+s);
		}
		
		Molybdenum.setAwtColor(Color.DARK_GRAY);
		Molybdenum.getText().drawString("Version "+Molybdenum.VERSION, 0, Display.getHeight()-16, Text.LEFT, 1);
		
	}
	public void input() {
		while(Keyboard.next()){
		    if(Keyboard.getEventKeyState()) {
		        
		    }else{
		    	if (Keyboard.getEventKey() == Keyboard.KEY_DOWN || Keyboard.getEventKey() == Keyboard.KEY_S) {
		        	if(item==items.length-1){
		        		item = 0;
		        	}else{
		        		item++;
		        	}
		        }
		    	if (Keyboard.getEventKey() == Keyboard.KEY_UP || Keyboard.getEventKey() == Keyboard.KEY_W) {
		        	if(item==0){
		        		item = items.length-1;
		        	}else{
		        		item--;
		        	}
		        }
		    	if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
		        	if(item==0){
		        		Molybdenum.getStateManager().setState(StateManager.MAPSELECTSTATE);
		        	}
		        	if(item==1){
		        		Molybdenum.getStateManager().setState(StateManager.MAPEDITORSTATE);
		        	}
		        	if(item==2){
		        		Molybdenum.getStateManager().setState(StateManager.HELPSTATE);
		        	}
		        	if(item==3){
		        		Molybdenum.getStateManager().setState(StateManager.PROPSTATE);
		        	}
		        	if(item==4){
		        		Molybdenum.getStateManager().setState(StateManager.TESTSTATE);
		        	}
		        	if(item==items.length-1){
		        		Molybdenum.exit();
		        	}
		        }
		    }
		}
	}

}
