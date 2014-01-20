package state;

import java.awt.Color;
import java.io.File;
import java.io.FilenameFilter;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

public class MapSelectState extends State {

	Texture bg;

	int selection;
	File[] files;

	int l;
	int r;
	int e;

	public void init() {
		bg = Texture.loadTexture("res/title.png");
		files = getMopaks();
	}
	public void update(int delta) {
		input();
	}
	public void render() {
		//Background
		bg.bind();
		Molybdenum.GLQuad(0, 0, Display.getWidth(), Display.getHeight());

		//Title
		Molybdenum.setAwtColor(Color.WHITE);
		Molybdenum.getText().drawStringS("Molybdenum | Load Map", 16, 16, Text.LEFT, 3f,4);
		Molybdenum.getText().drawStringS("> Use the Left/Right keys to navigate", 32, 64, Text.LEFT, 1.5f,3);
		Molybdenum.getText().drawStringS("> Press enter to select the map", 32, 96, Text.LEFT, 1.5f,3);


		Molybdenum.getText().drawStringS("<", Display.getWidth()/2-256, Display.getHeight()/2, Text.CENTER, 3, 4+l);
		Molybdenum.getText().drawStringS(">", Display.getWidth()/2+256, Display.getHeight()/2, Text.CENTER, 3, 4+r);
		Molybdenum.getText().drawStringS(files[selection].getName(), Display.getWidth()/2, Display.getHeight()/2, Text.CENTER, 3, 4);

		Molybdenum.setAwtColor(Color.GREEN.darker());
		Molybdenum.getText().drawStringS("[LOAD]", Display.getWidth()/2, Display.getHeight()/2+96, Text.CENTER, 3, 4+e);
	}
	public void input() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()){
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT || Keyboard.getEventKey() == Keyboard.KEY_D) {
					r=1;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT || Keyboard.getEventKey() == Keyboard.KEY_A) {
					l=1;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					e=2;
				}
			}else{
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					e=0;
					Molybdenum.getWorldIO().loadPak(files[selection], true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RIGHT || Keyboard.getEventKey() == Keyboard.KEY_D) {
					r=0;
					if(selection==files.length-1){
						selection = 0;
					}else{
						selection++;
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_LEFT || Keyboard.getEventKey() == Keyboard.KEY_A) {
					l=0;
					if(selection==0){
						selection = files.length-1;
					}else{
						selection--;
					}
				}
			}
		}
	}
	private File[] getMopaks(){
		File dir = new File(Molybdenum.settings.DIR+Molybdenum.settings.S+"Maps");
		System.out.println("Reading maps from "+dir.getAbsolutePath());
		File [] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".mopak");
			}
		});
		return files;
	}
}
