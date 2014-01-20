package graphics;

import game.Molybdenum;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextureArray {
	
	public static Texture[] loadTextureArray(String res,int cols, int rows){
		BufferedImage img = null;
		
		try{
			img = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(res));
		}catch (IOException e){
			e.printStackTrace();
			System.out.println("Unable to load Texture: " + res);
			Molybdenum.exit();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Unable to load Texture: " + res);
			Molybdenum.exit();
		}
		
		int w = img.getWidth()/cols;
		int h = img.getHeight()/rows;
		int num = 0;
		
		BufferedImage images[] = new BufferedImage[w*h];
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < cols; x++) {
				images[num] = new BufferedImage(w, h, img.getType());
				// Tell the graphics to draw only one block of the image
				Graphics2D g = images[num].createGraphics();
				g.drawImage(img, 0, 0, w, h, w*x, h*y, (w*x)+w, (h*y)+h, null);
				g.dispose();
				num++;
			}
		}
		Texture[] texs = new Texture[w*h];
		for(int i=0;i<rows*cols;i++){
			texs[i] = Texture.genTexture(images[i]);
		}
		return texs;
	}
	
}
