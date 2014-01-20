package map;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import state.GameState;
import state.StateManager;
import entity.Creature;
import entity.Entity;
import entity.EntityIO;
import game.Molybdenum;

public class WorldIO {

	String tag = "~MoWorld File~";

	EntityIO eio;
	MapIO mio;
	ArrayList<Entity> ents;
	public File pakFile;
	
	public boolean isLoaded;

	private String spawnFile;

	public WorldIO(){
		eio = new EntityIO();
		mio = new MapIO();
		ents = new ArrayList<Entity>();
	}

	@SuppressWarnings("resource")
	public void loadPak(File file, boolean spawnPlayer){
		pakFile = file;
		String thisfile = "properties";
		try {
			ZipFile zipFile = new ZipFile(file);
			ZipEntry zipEntry = zipFile.getEntry(thisfile);
			InputStream in = zipFile.getInputStream(zipEntry);
			BufferedReader input = new BufferedReader(new InputStreamReader(in));
			propReader(input);
		} catch (ZipException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			eio.loadPakData(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(spawnPlayer){
			boolean playerExists=false;
			for(Entity e: eio.entities){
				if(e instanceof Creature){
					Creature c = (Creature)e;
					if(c.name.equals(Molybdenum.settings.PLAYER_NAME)){
						playerExists=true;
					}
				}
			}
			if(playerExists){
				((GameState)Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).em.setEntities(eio.entities);
				Molybdenum.getStateManager().setState(StateManager.GAMESTATE);
			}else{
				spawnPlayer();
			}
		}
		
	}
	public void spawnPlayer(){
		try{
			((GameState)Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).em.setEntities(eio.entities);
			MapData spawn = mio.loadPakData(pakFile, spawnFile);
			((GameState)Molybdenum.getStateManager().getState(StateManager.GAMESTATE)).spawnPlayer(spawn);
			Molybdenum.getStateManager().setState(StateManager.GAMESTATE);
		}catch(IOException e1){
			e1.printStackTrace();
			Molybdenum.exit();
		}
	}
	private void propReader(BufferedReader input) throws IOException{
		String line = "";
		int num = -1;
		while ((line = input.readLine()) != null){
			if(!line.startsWith("//")){
				num++;
				if(num==0){
					if(!line.equals(tag)){
						return;
					}
				}
				if(num==1){
					spawnFile = line;
				}
				isLoaded = true;
			}
		}
	}
	public MapData getSpawnMapData(){
		try {
			return mio.loadPakData(pakFile, spawnFile);
		} catch (IOException e) {
			e.printStackTrace();
			Molybdenum.exit();
		}
		return null;
	}
	public String getSpawnFile() {
		return spawnFile;
	}
	//SET
	public void setSpawnFile(String spawnFile) {
		this.spawnFile = spawnFile;
	}
}
