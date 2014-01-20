package entity;

import game.Molybdenum;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import ai.Simple;
import map.MapData;
import map.MapIO;

public class EntityIO {

	public ArrayList<Entity> entities;
	private String tag = "~MoEntity File~";
	private MapIO mio;
	
	File pakFile;
	
	public EntityIO(){
		entities = new ArrayList<Entity>();
		mio = new MapIO();
	}
	
	public Entity addMapData(Entity e, String mapName){
		try {
			MapData mapData = mio.loadPakData(pakFile, mapName);
			e.map = mapData.clone();
		} catch (IOException e1) {
			e1.printStackTrace();
			Molybdenum.exit();
		}
		return e;
	}
	public Creature addMapData(Creature e, String mapName){
		try {
			MapData mapData = mio.loadPakData(pakFile, mapName);
			e.map = mapData.clone();
			((Simple) e.ai).newNode();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			Molybdenum.exit();
		}
		return e;
	}
	public Item addMapData(Item e, String mapName){
		try {
			MapData mapData = mio.loadPakData(pakFile, mapName);
			e.map = mapData.clone();
		} catch (IOException e1) {
			e1.printStackTrace();
			Molybdenum.exit();
		}
		return e;
	}
	public Food addMapData(Food e, String mapName){
		try {
			MapData mapData = mio.loadPakData(pakFile, mapName);
			e.map = mapData.clone();
		} catch (IOException e1) {
			e1.printStackTrace();
			Molybdenum.exit();
		}
		return e;
	}
	public Weapon addMapData(Weapon e, String mapName){
		try {
			MapData mapData = mio.loadPakData(pakFile, mapName);
			e.map = mapData.clone();
		} catch (IOException e1) {
			e1.printStackTrace();
			Molybdenum.exit();
		}
		return e;
	}
	public Armor addMapData(Armor e, String mapName){
		try {
			MapData mapData = mio.loadPakData(pakFile, mapName);
			e.map = mapData.clone();
		} catch (IOException e1) {
			e1.printStackTrace();
			Molybdenum.exit();
		}
		return e;
	}
	
	public void loadPakData(File file) throws IOException{
		pakFile = file;
		String thisfile = "entities";
		@SuppressWarnings("resource")
		ZipFile zipFile = new ZipFile(file);
		ZipEntry zipEntry = zipFile.getEntry(thisfile);
		InputStream in = zipFile.getInputStream(zipEntry);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		ArrayList<Entity> output = BufferedReaderData(input);
		entities = output;
	}
	private ArrayList<Entity> BufferedReaderData(BufferedReader input)
			throws NumberFormatException, IOException{

		ArrayList<Entity> entities = new ArrayList<Entity>();
		String line = "";
		int num = -1;
		while ((line = input.readLine()) != null){
			if(!line.startsWith("//")){
				num++;
				if(num==0){
					if(!line.equals(tag)){
						return null;
					}
				}
				if(num > 0){
					if(line.startsWith("C")){
						//C:XxY:Mister Name:r,g,b:Map.mo
						String[] split = line.split(":");
						String[] XxY = split[1].split("x");
						String name = split[2];
						String[] color = split[3].split(",");
						String mapName = split[4];
						Color co = new Color(Integer.parseInt(color[0]),Integer.parseInt(color[1]),Integer.parseInt(color[2]));
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						Creature c = new Creature(co);
						c.name = name;
						c.x = x;
						c.y = y;
						c = addMapData(c, mapName);
						entities.add(c);
					}
					if(line.startsWith("I")){
						//I:char:XxY:Item Name:Desc:r,g,b:Map.mo
						String[] split = line.split(":");
						char c = split[1].charAt(0);
						String[] XxY = split[2].split("x");
						String name = split[3];
						String desc = split[4];
						String[] color = split[5].split(",");
						String mapName = split[6];
						Color co = new Color(Integer.parseInt(color[0]),Integer.parseInt(color[1]),Integer.parseInt(color[2]));
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						Item i = new Item(co);
						i.name = name;
						i.character = c;
						i.description = desc;
						i.x = x;
						i.y = y;
						i = addMapData(i, mapName);
						entities.add(i);
					}
					if(line.startsWith("F")){
						//F:char:XxY:Item Name:Desc:r,g,b:Map.mo:heal
						String[] split = line.split(":");
						char c = split[1].charAt(0);
						String[] XxY = split[2].split("x");
						String name = split[3];
						String desc = split[4];
						String[] color = split[5].split(",");
						String mapName = split[6];
						int heal = Integer.parseInt(split[7]);
						Color co = new Color(Integer.parseInt(color[0]),Integer.parseInt(color[1]),Integer.parseInt(color[2]));
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						Food i = new Food(co);
						i.name = name;
						i.character = c;
						i.description = desc;
						i.x = x;
						i.y = y;
						i = addMapData(i, mapName);
						i.heal = heal;
						entities.add(i);
					}
					if(line.startsWith("W")){
						//W:char:XxY:Item Name:Desc:r,g,b:Map.mo:minDmg:maxDmg
						String[] split = line.split(":");
						char c = split[1].charAt(0);
						String[] XxY = split[2].split("x");
						String name = split[3];
						String desc = split[4];
						String[] color = split[5].split(",");
						String mapName = split[6];
						int min = Integer.parseInt(split[7]);
						int max = Integer.parseInt(split[8]);
						Color co = new Color(Integer.parseInt(color[0]),Integer.parseInt(color[1]),Integer.parseInt(color[2]));
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						Weapon i = new Weapon(co);
						i.name = name;
						i.character = c;
						i.description = desc;
						i.x = x;
						i.y = y;
						i = addMapData(i, mapName);
						i.damageMin = min;
						i.damageMax = max;
						entities.add(i);
					}
					if(line.startsWith("A")){
						//A:char:XxY:Item Name:Desc:r,g,b:Map.mo:bonus
						String[] split = line.split(":");
						char c = split[1].charAt(0);
						String[] XxY = split[2].split("x");
						String name = split[3];
						String desc = split[4];
						String[] color = split[5].split(",");
						String mapName = split[6];
						int bonus = Integer.parseInt(split[7]);
						Color co = new Color(Integer.parseInt(color[0]),Integer.parseInt(color[1]),Integer.parseInt(color[2]));
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						Armor i = new Armor(co);
						i.name = name;
						i.character = c;
						i.description = desc;
						i.x = x;
						i.y = y;
						i = addMapData(i, mapName);
						i.bonus = bonus;
						entities.add(i);
					}
				}
			}
		}
		return entities;
	}

}
