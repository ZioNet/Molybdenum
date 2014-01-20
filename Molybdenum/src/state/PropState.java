package state;

import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

import java.awt.Color;
import java.io.File;

import javax.swing.JFileChooser;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class PropState extends State {

	Texture bg;
	String input_player;
	String input_dir;

	String red;
	String green;
	String blue;

	int item;
	String[] items = {
			"PLAYER NAME:",
			"DIRECTORY:",
			"PLAYER RED: ",
			"PLAYER GREEN: ",
			"PLAYER BLUE: ",
			"FINISH"
	};

	public PropState() {
		init();
	}
	public void init() {
		input_player = Molybdenum.settings.PLAYER_NAME;
		input_dir = Molybdenum.settings.DIR;
		if(Molybdenum.settings.COLOR==null){
			red = "0";
			green = "0";
			blue = "0";
		}else{
			red = Molybdenum.settings.COLOR.getRed()+"";
			green = Molybdenum.settings.COLOR.getGreen()+"";
			blue = Molybdenum.settings.COLOR.getBlue()+"";
		}
		bg = Texture.loadTexture("res/title.png");
	}
	public void update(int delta) {
		input();
	}
	public void render() {
		bg.bind();
		Molybdenum.GLQuad(0, 0, Display.getWidth(), Display.getHeight());

		Molybdenum.setAwtColor(Color.WHITE);
		Molybdenum.getText().drawStringS("Molybdenum | Properties", 16, 16, Text.LEFT, 3f,4);

		Molybdenum.setAwtColor(Color.DARK_GRAY);
		Molybdenum.getText().drawString("Version "+Molybdenum.VERSION, 0, Display.getHeight()-16, Text.LEFT, 1);

		//Render all selections
		Molybdenum.setAwtColor(Color.WHITE);
		for(int i=0;i<items.length;i++){
			String current = items[i];
			int xset = 0;
			if(i==item){
				current = "> "+current;
				xset = (int)(16*2);
			}
			if(i==0){
				current+=" "+input_player;
			}
			if(i==1){
				current+=" "+input_dir;
			}
			if(i==2){
				current+=" "+red;
			}
			if(i==3){
				current+=" "+green;
			}
			if(i==4){
				current+=" "+blue;
			}
			Molybdenum.getText().drawStringS(current, 128-xset, 128+64*i, Text.LEFT, 2f,4);
		}
	}
	boolean single=true;
	public void input() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()) {
				if(single){
					if(Keyboard.getEventKey() == Keyboard.KEY_BACK){
						if(item==0){
							if(input_player.length()>0){
								input_player = input_player.substring(0, input_player.length()-1);
							}
						}
						if(item==1){
							if(input_dir.length()>0){
								input_dir = input_dir.substring(0, input_dir.length()-1);
							}
						}
						if(item==2){
							if(red.length()>0){
								red = red.substring(0, red.length()-1);
							}
						}
						if(item==3){
							if(green.length()>0){
								green = green.substring(0, green.length()-1);
							}
						}
						if(item==4){
							if(blue.length()>0){
								blue = blue.substring(0, blue.length()-1);
							}
						}
					}else{
						char c = Keyboard.getEventCharacter();
						if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
							c = Character.toUpperCase(c);
						}
						//System.out.print(c+"\n");
						if(Character.isLetterOrDigit(c) || c=='|' || c=='/' || c=='\\' || c==':'){
							if(item==0 && input_player.length()<20){
								input_player+=c;
							}
							if(item==1){
								input_dir+=c;
							}
							if(Character.isDigit(c)){
								if(item==2){
									red+=c;
								}
								if(item==3){
									green+=c;
								}
								if(item==4){
									blue+=c;
								}
							}
						}
					}
				}
				if(!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
					single = false;
				}
			}else{
				single = true;
				if (Keyboard.getEventKey() == Keyboard.KEY_DOWN) {
					if(item==items.length-1){
						item = 0;
					}else{
						item++;
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_UP) {
					if(item==0){
						item = items.length-1;
					}else{
						item--;
					}
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
					int r = Integer.parseInt(red);
					int g = Integer.parseInt(green);
					int b = Integer.parseInt(blue);
					if(r>255){
						r = 255;
					}
					if(g>255){
						g = 255;
					}
					if(b>255){
						b = 255;
					}
					if(item==items.length-1){
						Molybdenum.settings.generate(input_player, input_dir, new Color(r,g,b));
						Molybdenum.getStateManager().setState(StateManager.HOMESTATE);
						Molybdenum.settings.load();
					}
					if(item==2){
						Molybdenum.settings.COLOR = new Color(r,Molybdenum.settings.COLOR.getGreen(),Molybdenum.settings.COLOR.getBlue());
					}
					if(item==3){
						Molybdenum.settings.COLOR = new Color(Molybdenum.settings.COLOR.getRed(),g,Molybdenum.settings.COLOR.getBlue());
					}
					if(item==4){
						Molybdenum.settings.COLOR = new Color(Molybdenum.settings.COLOR.getRed(),Molybdenum.settings.COLOR.getGreen(),b);
					}
					if(item==1){
						chooseDir();
					}
				}
			}
		}
	}

	private void chooseDir(){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			input_dir = selectedFile.getAbsolutePath();
		}
	}

}
