package map;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import game.Molybdenum;

public class MapIO {

	private String tag = "~MoMap File~";
	private MapDictionary dict;

	private MapData mapData;

	public MapIO(){
		mapData = new MapData();
		dict = new MapDictionary();
	}

	public MapData createBasicMap(){
		for(int y=0;y<mapData.HEIGHT;y++){
			for(int x=0;x<mapData.WIDTH;x++){
				mapData.map[y][x] = dict.getTile('.');
				if(x == 0 || x == mapData.WIDTH-1 || y == 0 || y == mapData.HEIGHT-1){
					mapData.map[y][x] = dict.getTile('#');
				}
			}
		}
		return mapData;
	}
	public MapData getMapData(){
		return mapData;
	}

	public void saveMap(MapData mapData){
		this.mapData = mapData;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Molybdenum.settings.path(Molybdenum.settings.DIR+"|Maps")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MoMap Files", "mo", "momap");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				saveMapData(selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
				Molybdenum.exit();
			}
		}
	}

	private void saveMapData(File file) throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		
		output.write(tag);
		output.newLine();
		output.write(mapData.WIDTH+"");
		output.newLine();
		output.write(mapData.HEIGHT+"");
		output.newLine();

		for(int y=0;y<mapData.HEIGHT;y++){
			String line = "";
			for(int x=0;x<mapData.WIDTH;x++){
				line += mapData.map[y][x].character;
			}
			output.write(line);
			output.newLine();	
		}

		output.close();
	}
	public MapData loadPakData(File file) throws IOException{
		return loadPakData(file, "0x0.mo");
	}
	public MapData loadPakData(File file, String mapname) throws IOException{
		mapData.filename = mapname;
		@SuppressWarnings("resource")
		ZipFile zipFile = new ZipFile(file);
		ZipEntry zipEntry = zipFile.getEntry(mapname);
		InputStream in = zipFile.getInputStream(zipEntry);
		BufferedReader input = new BufferedReader(new InputStreamReader(in));
		MapData output = BufferedReaderData(input);
		output.inPak = true;
		output.pak = file.getName();
		return output;
	}
	public MapData loadMap(){
		mapData = null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(Molybdenum.settings.path(Molybdenum.settings.DIR+"|Maps")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MoMap Files and MoPak Files", "mo", "momap", "mopak");
		fileChooser.setFileFilter(filter);
		int returnValue = fileChooser.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			try {
				mapData = loadMapData(selectedFile);
			} catch (IOException e) {
				e.printStackTrace();
				Molybdenum.exit();
			}
		}
		return mapData;
	}
	public MapData loadMapData(File file) throws IOException{
		mapData = new MapData();
		mapData.filename = file.getName();
		BufferedReader input = new BufferedReader(new FileReader(file));
		return BufferedReaderData(input);
	}
	private MapData BufferedReaderData(BufferedReader input)
			throws NumberFormatException, IOException{
		String line = "";
		int num = -1;
		int W=0,H=0;
		while ((line = input.readLine()) != null){
			if(!line.startsWith("//")){
				num++;
				if(num==0){
					if(!line.equals(tag)){
						return null;
					}
				}
				if(num==1){
					W = Integer.parseInt(line);
				}
				if(num==2){
					H = Integer.parseInt(line);
					mapData.init(W,H);
				}
				if(num>2 && num-3<H){
					mapData.map[num-3] = toTileArray(line);
				}
				if(num-3>=H && num > 2){
					if(line.startsWith("SPAWN:")){
						String[] split = line.split(":");
						String[] XxY = split[1].split("x");
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						mapData.SPAWN_X = x;
						mapData.SPAWN_Y = y;
					}
					if(line.startsWith("T-")){
						String[] split = line.split("-");
						String[] XxY = split[1].split("~");
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						int type = Integer.parseInt(split[2]);
						int warpX = 0,warpY = 0;
						String filename = "";
						String warpname = null;
						if(type == Tile.WARP){
							filename = split[3];
							String[] wXxY = split[4].split("~");
							warpX = Integer.parseInt(wXxY[0]);
							warpY = Integer.parseInt(wXxY[1]);
							if(split.length==6){
								warpname = split[5];
							}
						}
						Tile tile = new Tile();
						tile.character = mapData.map[y][x].character;
						tile.collision = mapData.map[y][x].collision;
						tile.color = mapData.map[y][x].color;
						tile.description = mapData.map[y][x].description;
						tile.type = type;
						tile.WARP_NAME = filename;
						tile.WARP_X = warpX;
						tile.WARP_Y = warpY;
						//tile.WARP_PAK = warpname;
						mapData.map[y][x] = tile;
					}
					if(line.startsWith("I-")){
						String[] split = line.split("-");
						String[] XxY = split[1].split("x");
						int x = Integer.parseInt(XxY[0]);
						int y = Integer.parseInt(XxY[1]);
						String info = split[2];
						Tile tile = new Tile();
						tile.character = mapData.map[y][x].character;
						tile.collision = mapData.map[y][x].collision;
						tile.color = mapData.map[y][x].color;
						tile.description = mapData.map[y][x].description;
						tile.type = Tile.INFO;
						tile.INFO_TEXT = info;
						mapData.map[y][x] = tile;
					}
				}
			}
		}
		return mapData;
	}
	private Tile[] toTileArray(String in){
		char[] temp = in.toCharArray();
		Tile[] tiles= new Tile[temp.length];
		for(int i=0;i<temp.length;i++){
			tiles[i] = dict.getTile(temp[i]);
		}
		return tiles;
	}

}
