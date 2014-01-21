package state;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

public class TestState extends State {

	Texture bg;
	
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
		Molybdenum.getText().drawStringS("Molybdenum | Test State", 16, 16, Text.LEFT, 3f,4);
		
		Molybdenum.getText().drawStringS("(There doesn't seem to be anything worth testing here...)", Display.getWidth()/2, Display.getHeight()/2-64, Text.CENTER, 1.7f, 4);
		Molybdenum.getText().drawStringS("Press TAB to return!", Display.getWidth()/2, Display.getHeight()/2, Text.CENTER, 2f, 5);
	}
	public void input() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()) {

			}else{
				
			}
		}
	}

}
