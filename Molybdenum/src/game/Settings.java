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
		} catch (FileNotFoundException e1) {
			//e1.printStackTrace();
		}
		
		if(propReader==null){
			createProperties();
			return;
		}
		
		String line = "";
		try {
			int num = 0;
			while ((line = propReader.readLine()) != null){
				num++;
				if(num==1){
					DIR = path(line);
				}
				if(num==2){
					PLAYER_NAME = line;
				}
				if(num==3){
					String[] sc = line.split(",");
					int r,g,b;
					r = Integer.parseInt(sc[0]);
					g = Integer.parseInt(sc[1]);
					b = Integer.parseInt(sc[2]);
					COLOR = new Color(r,g,b);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			Molybdenum.exit();
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
		     if(result) {    
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
		  writer.println(d);
		  writer.println(p);
		  writer.println(c.getRed()+","+c.getGreen()+","+c.getBlue());
		  writer.close();
	}
	
	public String path(String in){
		//System.out.println(in);
		String out = in.replace("|", S);
		return out;
	}
	
}
