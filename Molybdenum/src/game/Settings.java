package game;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import state.StateManager;

public class Settings {

	public String PLAYER_NAME="";
	public String DIR="";
	public String S;
	public String HOME;
	public Color COLOR;

	public int KEY_INVENTORY;
	public int KEY_PICKUP;

	public int KEY_UP;
	public int KEY_DOWN;
	public int KEY_LEFT;
	public int KEY_RIGHT;

	public int KEY_QUICK_THROW;
	public int KEY_QUICK_EQUIP;
	public int KEY_QUICK_EAT;

	public void load(){
		HOME = System.getProperty("user.home");
		S = System.getProperty("file.separator");
		//COLOR = new Color();
		COLOR = Color.YELLOW;
		if(S.equals("\\")){//DOS based
			DIR = HOME+"\\AppData\\Roaming\\.Molybdenum";
		}else{//Unix based
			DIR = HOME+"/.Molybdenum";
		}

		BufferedReader propReader = null;
		try {
			propReader = new BufferedReader(new FileReader(DIR+S+"properties"));

			String line = "";
			int num = 0;
			while ((line = propReader.readLine()) != null){
				num++;
				if(num==1){
					PLAYER_NAME = line;
				}
				if(num==2){
					String[] sc = line.split(",");
					int r,g,b;
					r = Integer.parseInt(sc[0]);
					g = Integer.parseInt(sc[1]);
					b = Integer.parseInt(sc[2]);
					COLOR = new Color(r,g,b);
				}
				if(num==3){//Key UP
					KEY_UP = Integer.parseInt(line);
				}
				if(num==4){//Key DOWN
					KEY_DOWN = Integer.parseInt(line);
				}
				if(num==5){//Key LEFT
					KEY_LEFT = Integer.parseInt(line);
				}
				if(num==6){//Key RIGHT
					KEY_RIGHT = Integer.parseInt(line);
				}
				if(num==7){//Key PICKUP
					KEY_PICKUP = Integer.parseInt(line);
				}
				if(num==8){//Key INVENTORY
					KEY_INVENTORY = Integer.parseInt(line);
				}
				if(num==9){//Key QUICK EAT
					KEY_QUICK_EAT = Integer.parseInt(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			createProperties();
		}
	}
	public void createProperties(){
		Molybdenum.getStateManager().setState(StateManager.PROPSTATE);
	}
	public void generate(String p, String d,Color c){

		File theDir = new File(d);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("Creating new directory: " +d);
			boolean result = theDir.mkdir();  
			if(result){
				System.out.println(d+" created");  
			}
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(d+S+"properties");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Molybdenum.exit();
		}
		writer.println(p);
		writer.println(c.getRed()+","+c.getGreen()+","+c.getBlue());
		writer.println(KEY_UP);
		writer.println(KEY_DOWN);
		writer.println(KEY_LEFT);
		writer.println(KEY_RIGHT);
		
		writer.println(KEY_PICKUP);
		writer.println(KEY_INVENTORY);
		writer.println(KEY_QUICK_EAT);
		
		writer.close();
	}

	public String path(String in){
		String out = in.replace("|", S);
		return out;
	}

}
