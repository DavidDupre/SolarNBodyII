package simulator;

import java.util.HashMap;

import simulator.utils.Astrophysics;

public class Defines {
	public final static int width = 1200, height = 660;
	
	public final static String PHYSICS = "Physics";
	public final static String RENDER = "Render";

	public static final int FOCUS_BODY = 3;
	
	//In seconds per physics tick
	public static final double SECONDS_PER_TICK = 1.0;
	public static final int TIMESTEP = (int) (SECONDS_PER_TICK / Math.sqrt(Astrophysics.G)); //TODO make this less static
	
	public enum BodyType {
		STAR, PLANET, DWARF_PLANET, MOON, ASTEROID, CRAFT
	}
	
	public static HashMap<BodyType, float[]> colors;
	
	public Defines() {
		colors = new HashMap<BodyType, float[]>();
		colors.put(BodyType.STAR, new float[]{1f, 1f, 0f});
		colors.put(BodyType.PLANET, new float[]{1f, 0f, 0f});
		colors.put(BodyType.DWARF_PLANET, new float[]{1f, 0f, 0f});
		colors.put(BodyType.MOON, new float[]{0f, 1f, 0f});
		colors.put(BodyType.ASTEROID, new float[]{1f, 1f, 1f});
	}
}
