package graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import game.Molybdenum;

import java.awt.Color;
import java.util.HashMap;

import state.GameState;
import state.StateManager;

public class Text {
	
	public int tw,th;
	
	private HashMap<String,Texture> map;
	
	public static final int LEFT = 0;
	public static final int CENTER = 1;
	public static final int RIGHT = 2;

	public Text(){
		map = new HashMap<String, Texture>();
		Texture[] texs = TextureArray.loadTextureArray("res/fontset.png", 16, 8);
		th = texs[0].height;
		tw = texs[0].width;
		sort(texs);
	}
	public void drawStringS(String str, int x, int y, int type, float scale,int l){
		Color first = Molybdenum.getLastAwtColor();
		Color next = first;
		for(int i=1;i<l;i++){
			next = next.darker();
		}
		for(int i=l;i>0;i--){
			Molybdenum.setAwtColor(next);
			drawString(str,x-i,y+i,type,scale);
			next = next.brighter();
		}
		
		Molybdenum.setAwtColor(first);
	}
	public void drawStringToWidth(String str, int x,int y, int type, float scale, int setWidth){
		if(str.length() > setWidth){
			str = str.substring(0, setWidth-4);
			str+="...";
		}
		drawString(str,x,y,type,scale);
	}
	public void drawStringB(String str,int x,int y,int type,float scale,Color bg, Color fg){
		glDisable(GL_TEXTURE_2D);
		Molybdenum.setAwtColor(bg);
		int tw = ((GameState) Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).tw;
		int th = ((GameState) Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).th;
		
		Molybdenum.GLQuad(x, y, tw*str.length(), th,scale);
		glEnable(GL_TEXTURE_2D);
		Molybdenum.setAwtColor(fg);
		
		drawString(str,x,y,type,scale);
	}
	public void drawString(String str,int x,int y,int type,float scale){
		if(str==null){
			return;
		}
		for(int i=0;i<(str.length());i++){	
			Texture t = map.get("A");
			try {
				t = map.get(str.charAt(i)+"");
				t.bind();
			} catch (Exception e) {
				System.err.println("Image load crash on symbol \""+str.charAt(i)+"\"");
				t = map.get("?");
				t.bind();
			}
			int w = t.width;
			int h = t.height;
			
			int dw = w-tw;
			int dh = h-th;
			
			switch(type){
			case LEFT:
				Molybdenum.GLQuad(x+((w-dw)*scale*i), y, w, h, scale);
				break;
			case CENTER:
				int xsize = (int) (w*scale*str.length())/2;
				int ysize = (h-dh);
				Molybdenum.GLQuad(x+((w)*scale*i)-xsize, y-ysize, w, h, scale);
				break;
			case RIGHT:
				int nw = (int) ((str.length()*tw*scale));
				//int dh = (int) (Display.getHeight()-(h*scale));
				Molybdenum.GLQuad(((w-dw)*scale*i)+x-nw, y, w, h, scale);
				break;
			}
		}
	}
	public int getWidth(String str,float scale){			
			return (int) (tw*scale*str.length());
	}
	public Texture get(char ch){
		return map.get(ch+"");
	}
	private void sort(Texture[] array){
		map.put("\u4200", array[00]);
		map.put(" ", array[32]);
		map.put("!", array[33]);
		map.put("\"",array[34]);
		map.put("#", array[35]);
		map.put("$", array[36]);
		map.put("%", array[37]);
		map.put("&", array[38]);
		map.put("'", array[39]);
		map.put("(", array[40]);
		map.put(")", array[41]);
		map.put(":", array[58]);
		map.put("|", array[22]);
		map.put("?", array[63]);
		map.put(",", array[44]);
		map.put(".", array[46]);
		map.put("<", array[60]);
		map.put("=", array[61]);
		map.put(">", array[62]);
		map.put("*", array[42]);
		map.put("+", array[43]);
		map.put("_", array[95]);
		
		map.put("-", array[45]);
		map.put("/", array[47]);
		map.put("[", array[91]);
	   map.put("\\", array[92]);
		map.put("]", array[93]);

		map.put("0", array[48]);
		map.put("1", array[49]);
		map.put("2", array[50]);
		map.put("3", array[51]);
		map.put("4", array[52]);
		map.put("5", array[53]);
		map.put("6", array[54]);
		map.put("7", array[55]);
		map.put("8", array[56]);
		map.put("9", array[57]);

		map.put("A", array[65]);
		map.put("B", array[66]);
		map.put("C", array[67]);
		map.put("D", array[68]);
		map.put("E", array[69]);
		map.put("F", array[70]);
		map.put("G", array[71]);
		map.put("H", array[72]);
		map.put("I", array[73]);
		map.put("J", array[74]);
		map.put("K", array[75]);
		map.put("L", array[76]);
		map.put("M", array[77]);
		map.put("N", array[78]);
		map.put("O", array[79]);
		map.put("P", array[80]);
		map.put("Q", array[81]);
		map.put("R", array[82]);
		map.put("S", array[83]);
		map.put("T", array[84]);
		map.put("U", array[85]);
		map.put("V", array[86]);
		map.put("W", array[87]);
		map.put("X", array[88]);
		map.put("Y", array[89]);
		map.put("Z", array[90]);

		map.put("a", array[65+32]);
		map.put("b", array[66+32]);
		map.put("c", array[67+32]);
		map.put("d", array[68+32]);
		map.put("e", array[69+32]);
		map.put("f", array[70+32]);
		map.put("g", array[71+32]);
		map.put("h", array[72+32]);
		map.put("i", array[73+32]);
		map.put("j", array[74+32]);
		map.put("k", array[75+32]);
		map.put("l", array[76+32]);
		map.put("m", array[77+32]);
		map.put("n", array[78+32]);
		map.put("o", array[79+32]);
		map.put("p", array[80+32]);
		map.put("q", array[81+32]);
		map.put("r", array[82+32]);
		map.put("s", array[83+32]);
		map.put("t", array[84+32]);
		map.put("u", array[85+32]);
		map.put("v", array[86+32]);
		map.put("w", array[87+32]);
		map.put("x", array[88+32]);
		map.put("y", array[89+32]);
		map.put("z", array[90+32]);
	}

}
