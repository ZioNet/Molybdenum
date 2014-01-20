package graphics;

import game.Molybdenum;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class Texture{

	// ID of this texture
	public int id;

	// Width of this texture
	public int width;

	// Height of this texture
	public int height;

	// Private constructor to prevent duplication
	private Texture(int id, int width, int height){
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public static Texture genTexture(BufferedImage bimg){
		int textureID = glGenTextures();

		// Multiply by four for all the four components of the pixel.
		int[] pixels = new int[bimg.getWidth() * bimg.getHeight() * 4];
		// Read all the pixels.
		bimg.getRGB(0, 0, bimg.getWidth(), bimg.getHeight(), pixels, 0, bimg.getWidth());

		ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length);
		
		for (int y=0; y<bimg.getHeight(); y++)
		{
		    for (int x=0; x<bimg.getWidth(); x++)
		    {
		        // Select the pixel
		        int pixel = pixels[y * bimg.getWidth() + x];
		        // Add the RED component
		        buffer.put((byte) ((pixel >> 16) & 0xFF));
		        // Add the GREEN component
		        buffer.put((byte) ((pixel >> 8) & 0xFF));
		        // Add the BLUE component
		        buffer.put((byte) (pixel & 0xFF));
		        // Add the ALPHA component
		        buffer.put((byte) ((pixel >> 24) & 0xFF));
		    }
		}

		// Now flip the buffer to reset the position pointer
		buffer.flip();
		
		// Bind the texture handle to the context
		glBindTexture(GL_TEXTURE_2D, textureID);

		// Set the scaling filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// Send the buffer to OpenGL
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, bimg.getWidth(), bimg.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		return new Texture(textureID, bimg.getWidth(), bimg.getHeight());
	}
	
	public static Texture loadTexture(String name){
		// We need a BufferedImage which, we load with ImageIO
		BufferedImage bimg = null;

		try{
			bimg = ImageIO.read(Texture.class.getClassLoader().getResourceAsStream(name));
		}catch (IOException e){
			e.printStackTrace();
			System.out.println("Unable to load Texture: " + name);
			Molybdenum.exit();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println("Unable to load Texture: " + name);
			Molybdenum.exit();
		}
		return genTexture(bimg);
	}
	
	public void bind(){
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
}
