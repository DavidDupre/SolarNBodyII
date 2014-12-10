package simulator;

import org.lwjgl.opengl.Display;

public class Simulator {
	private static Simulation sim = new Simulation();
	
	public static void main(String args[]) {
		sim.init();
		
		while (!Display.isCloseRequested()) {
			sim.update();
		}
		
		sim.kill();
		System.exit(0);
	}
}
