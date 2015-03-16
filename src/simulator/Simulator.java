package simulator;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import simulator.gui.StartMenu;
import simulator.integrators.LeapFrog;
import simulator.utils.PropertiesManager;

public class Simulator {
	private static Simulation sim = new Simulation();

	public static void main(String args[]) {
		PropertiesManager.load();
		StartMenu menu = null;
		try {
			menu = new StartMenu(Defines.width / 2, Defines.height / 2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (!menu.isSelected()) {
			try {
				Thread.sleep(100); // probably unnecessary and bad. fite me
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		sim.init(PropertiesManager.getProperty("dataFile"));

		while (!Display.isCloseRequested()) {
			sim.update();
		}

		sim.kill();
		System.exit(0);
	}
}
