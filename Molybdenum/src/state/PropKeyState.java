package state;

import game.Molybdenum;
import graphics.Text;
import graphics.Texture;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class PropKeyState extends State {

	Texture bg;

	String cUp;
	String cDown;
	String cLeft;
	String cRight;

	String cPickup;
	String cInv;
	String cEat;

	int item;
	String[] items = {
			"UP:         ",//0
			"DOWN:       ",//1
			"LEFT:       ",//2
			"RIGHT:      ",//3
			"Pickup:     ",//4
			"Inventory:  ",//5
			"Quick Eat:  ",//6
			"FINISH"//END
	};

	public PropKeyState() {
		init();
	}
	public void init() {
		item = 0;
		if(Molybdenum.settings.KEY_UP != 0){
			cUp = Keyboard.getKeyName(Molybdenum.settings.KEY_UP);
			cDown = Keyboard.getKeyName(Molybdenum.settings.KEY_DOWN);
			cLeft = Keyboard.getKeyName(Molybdenum.settings.KEY_LEFT);
			cRight = Keyboard.getKeyName(Molybdenum.settings.KEY_RIGHT);
			cPickup = Keyboard.getKeyName(Molybdenum.settings.KEY_PICKUP);
			cInv = Keyboard.getKeyName(Molybdenum.settings.KEY_INVENTORY);
			cEat = Keyboard.getKeyName(Molybdenum.settings.KEY_QUICK_EAT);
		}else{
			cUp = Keyboard.getKeyName(Keyboard.KEY_W);
			cDown = Keyboard.getKeyName(Keyboard.KEY_S);
			cLeft = Keyboard.getKeyName(Keyboard.KEY_A);
			cRight = Keyboard.getKeyName(Keyboard.KEY_D);
			cPickup = Keyboard.getKeyName(Keyboard.KEY_SPACE);
			cInv = Keyboard.getKeyName(Keyboard.KEY_E);
			cEat = Keyboard.getKeyName(Keyboard.KEY_Q);
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

		Molybdenum.getText().drawStringS("Press enter to confirm a choice!", 64, 64, Text.LEFT, 2f,4);

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
				current+=" "+cUp;
			}
			if(i==1){
				current+=" "+cDown;
			}
			if(i==2){
				current+=" "+cLeft;
			}
			if(i==3){
				current+=" "+cRight;
			}
			if(i==4){
				current+=" "+cPickup;
			}
			if(i==5){
				current+=" "+cInv;
			}
			if(i==6){
				current+=" "+cEat;
			}
			Molybdenum.getText().drawStringS(current, 128-xset, 128+(32*i), Text.LEFT, 2f,4);
		}
	}
	boolean single=true;
	public void input() {
		while(Keyboard.next()){
			if(Keyboard.getEventKeyState()) {
				if(single){
					if(Keyboard.getEventKey() == Keyboard.KEY_BACK){
						//Backspace keys
					}else{
						int k = Keyboard.getEventKey();
						if(Keyboard.getEventKey() != Keyboard.KEY_RETURN){
							if(item==0){
								cUp = Keyboard.getKeyName(k);
							}
							if(item==1){
								cDown = Keyboard.getKeyName(k);
							}
							if(item==2){
								cLeft = Keyboard.getKeyName(k);
							}
							if(item==3){
								cRight = Keyboard.getKeyName(k);
							}
							if(item==4){
								cPickup = Keyboard.getKeyName(k);
							}
							if(item==5){
								cInv = Keyboard.getKeyName(k);
							}
							if(item==6){
								cEat = Keyboard.getKeyName(k);
							}
						}else{
							if(item == items.length-1){}
							item++;
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
					if(item==items.length-1){
						Molybdenum.settings.KEY_UP = Keyboard.getKeyIndex(cUp);
						Molybdenum.settings.KEY_DOWN = Keyboard.getKeyIndex(cDown);
						Molybdenum.settings.KEY_LEFT = Keyboard.getKeyIndex(cLeft);
						Molybdenum.settings.KEY_RIGHT = Keyboard.getKeyIndex(cRight);

						Molybdenum.settings.KEY_PICKUP = Keyboard.getKeyIndex(cPickup);
						Molybdenum.settings.KEY_INVENTORY = Keyboard.getKeyIndex(cInv);

						Molybdenum.settings.KEY_QUICK_EAT = Keyboard.getKeyIndex(cEat);

						Molybdenum.getStateManager().setState(StateManager.PROPSTATE);
					}
				}
			}
		}
	}

}
