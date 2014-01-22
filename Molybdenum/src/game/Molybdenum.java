package game;

import static org.lwjgl.opengl.GL11.*;
import graphics.Text;

import java.awt.Color;
import java.util.Random;

import map.WorldIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import state.StateManager;

public class Molybdenum {
	
	/*Static Variables*/
	public static final String VERSION = "a1.20.0";
	public static Settings settings;
	
	/*Private Variables*/
	private int width;
	private int height;
	private float drawScale;
	private boolean run;
	private static boolean prettyColorMode;
	/*Managers*/
	private static StateManager sm;
	private static Text text;
	private static Random random;
	
	private static WorldIO wio;
	
	/*Public Methods*/
	public void init(){
		random = new Random();
		random.setSeed(System.currentTimeMillis());
		settings = new Settings();
		run = true;
		Display.setTitle("Molybdenum | Version "+VERSION);
		//Setup OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		
		glEnable(GL_TEXTURE_2D);//Allows Textures
		
		glEnable(GL_BLEND); glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);//Enables transparency
		
		glMatrixMode(GL_MODELVIEW);
		glOrtho(0, 800, 600, 0, 1, -1);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		Molybdenum.setText(new Text());
		Molybdenum.getText().th = 14;
		Molybdenum.getText().tw = 10;
		
		sm = new StateManager();
		settings.load();
		wio = new WorldIO();
	}
	public void update(int delta){
		sm.update(delta);
		Display.update();
	}
	public void render(){
		glColor4f(1,1,1,1);
		sm.render();
	}
	public long getTime(){
		return System.nanoTime() / 1000000;
	}
	public void gameLoop(){		
		long lastTime = getTime();
		long currentTime = getTime();
		int delta;
		
		while(run){
			currentTime = getTime();
			delta = (int) (currentTime -lastTime);
			update(delta);
			render();
			
			run = !Display.isCloseRequested();
			
			lastTime = currentTime;
		}
		exit();
		
		
	}
	
	public void updateWindowSize(){
		if(Display.isCreated()){
			Display.destroy();
		}
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			System.err.print("The requested mode "+width+"x"+height+" is not supported.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	/*Static Methods*/
	public static void exit(){
		Display.destroy();
		System.exit(0);
	}
	public static void GLQuad(float x, float y, float w, float h, float scale){
		//float x2 = x+w;
		//float y2 = y+h;
		glBegin(GL_QUADS);
		{
			// Top left corner of the texture
			if(getPrettyColorMode()){setAwtColor(Color.RED);}
			glTexCoord2f(0, 0);
			glVertex2f(x, y);

			// Top right corner of the texture
			if(getPrettyColorMode()){setAwtColor(Color.BLUE);}
			glTexCoord2f(1, 0);
			glVertex2f(x+w*scale, y);

			// Bottom right corner of the texture
			if(getPrettyColorMode()){setAwtColor(Color.GREEN);}
			glTexCoord2f(1, 1);
			glVertex2f(x+w*scale, y+h*scale);

			// Bottom left corner of the texture
			if(getPrettyColorMode()){setAwtColor(Color.WHITE);}
			glTexCoord2f(0, 1);
			glVertex2f(x, y+h*scale);
		}
		glEnd();
	}
	public static void GLQuad(int x, int y, int w, int h){
		GLQuad(x,y,w,h,1f);
	}
	static float red;
	static float green;
	static float blue;
	static float alpha;
	public static Color getLastAwtColor(){
		Color c = new Color(red, green, blue, alpha);
		return c;
	}
	public static void setAwtColor(Color c){
		red   = c.getRed() / 255f;
		green = c.getGreen() / 255f;
		blue  = c.getBlue() / 255f;
		alpha = c.getAlpha() / 255f;
		glColor4f(red, green, blue, alpha);
	}
	
	/*Getters and Setters*/
	public int getWidth() {return width;}
	public void setWidth(int width) {this.width = width;}
	public int getHeight(){return height;}
	public void setHeight(int height){this.height = height;}
	public float getDrawScale() {return drawScale;}
	public void setDrawScale(float drawScale) {this.drawScale = drawScale;}
	
	public static boolean getPrettyColorMode() {return prettyColorMode;}
	public static void setPrettyColorMode(boolean p) {prettyColorMode = p;}
	public static StateManager getStateManager(){return sm;}
	public static Text getText() {return text;}
	public static void setText(Text text) {Molybdenum.text = text;}
	public static WorldIO getWorldIO() {return wio;}
	public void setWorldIO(WorldIO wio) {Molybdenum.wio = wio;}
	public static Random getRandom() {return random;}
	public static void setRandom(Random random) {Molybdenum.random = random;}
}