package state;

import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class HelpState extends State {

	Texture bg;


	public HelpState() {
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
		Molybdenum.getText().drawStringS("Molybdenum | Keys", 16, 16, Text.LEFT, 3f,4);

		Molybdenum.setAwtColor(Color.DARK_GRAY);
		Molybdenum.getText().drawString("Version "+Molybdenum.VERSION, 0, Display.getHeight()-16, Text.LEFT, 1);
		
		//Keys
		int s = 4;
		Molybdenum.setAwtColor(Color.WHITE);
		Molybdenum.getText().drawStringS("[TAB]   - Return to the Home menu", 16, 96, Text.LEFT, 1.75f,s);
		Molybdenum.getText().drawStringS("[WSAD]  - Move Character", 16, 96+(32*1), Text.LEFT, 1.75f,s);
		Molybdenum.getText().drawStringS("[SPACE] - Pick up item below player", 16, 96+(32*2), Text.LEFT, 1.75f,s);
		Molybdenum.getText().drawStringS("[Q]     - Auto-Eat food", 16, 96+(32*3), Text.LEFT, 1.75f,s);
		Molybdenum.getText().drawStringS("[E]     - Opens inventory", 16, 96+(32*4), Text.LEFT, 1.75f,s);
		Molybdenum.getText().drawStringS("[Z]     - Toggle Dance Mode [TEMP MAYBE]", 16, 96+(32*5), Text.LEFT, 1.75f,s);
		Molybdenum.getText().drawStringS("[G]     - Gives gold to the player [TEMP]", 16, 96+(32*6), Text.LEFT, 1.75f,s);

	}
	public void input() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()) {

			}else{
				
			}
		}
	}

}
