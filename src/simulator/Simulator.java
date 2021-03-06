package simulator;

import java.io.IOException;

import org.lwjgl.opengl.Display;

import simulator.gui.StartMenu;
import simulator.utils.PropertiesManager;

/**
 * References: 
 * 
 * [1] Celestia Development Team, Celestia (Version 1.6.1), [Computer
 * Program]. Available at www.shatters.net/celestia/
 * 
 * [2] stackoverflow
 * 
 * @author s-2482153
 *
 */
public class Simulator {
	private static Simulation sim = new Simulation();

	public static void main(String args[]) {
		System.out.println("Debugging");
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
