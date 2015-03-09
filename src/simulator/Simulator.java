package simulator;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import simulator.gui.StartMenu;

public class Simulator {
	private static Simulation sim = new Simulation();
	
	public static void main(String args[]) {
		StartMenu menu = null;
		try {
			menu = new StartMenu(Defines.width/2, Defines.height/2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (!menu.isSelected()) {
			// ayy lmao
		}
		
		sim.init(menu.getSelectedFile().getPath());
		
		while (!Display.isCloseRequested()) {
			sim.update();
		}
		
		sim.kill();
		System.exit(0);
	}
}
