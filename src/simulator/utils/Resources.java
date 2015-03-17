package simulator.utils;

import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Resources {
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static Texture get(String path) {
		String format = path.substring(path.length()-3, path.length());
		if(!textures.containsKey(path)){
			Texture tex = null;
			try {
				tex = TextureLoader.getTexture(format, ResourceLoader.getResourceAsStream(path));
			} catch (IOException e) {
				System.out.println("failed to load " + path);
				e.printStackTrace();
			}
			textures.put(path, tex);
		}
		return textures.get(path);
	}
}
