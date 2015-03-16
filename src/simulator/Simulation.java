package simulator;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.lwjgl.opengl.Display;

import simulator.integrators.Integrator;
import simulator.integrators.LeapFrog;

public class Simulation {
	private Window window;
	private List<SimObject> bodies;
	private List<SimObject> ships;
	private Defines defines = new Defines();
	private CSVLoader loader = new CSVLoader();
	private Integrator integrator;
	public static ReentrantLock physicsLock;
	private Timer timer;
		
	public void init(String bodyFilePath) {
		//bodies = loader.getBodies("./data/solarSystem.csv");
		bodies = loader.getBodies(bodyFilePath);
		ships = loader.getShips("./resources/craft.csv");
		window = new Window(Defines.width, Defines.height, bodies, ships);
		window.init();
		for(SimObject b: bodies){ // TODO move this somewhere that has OpenGL
			b.getRender().initGL();
		}
		timer = new Timer(window);
		integrator = new LeapFrog(bodies, ships);
		physicsLock = new ReentrantLock();
		integrator.start();
		timer.start();
	}
	
	public void update() {
		timer.addTime(integrator.getElapsedTime());
		integrator.setTimeStep(window.getSpeedRequest());
		window.clear();
		window.ready3D();
		
		// Stuff that can't run while physics is running
		physicsLock.lock();
		window.focus();
		for(SimObject b : bodies) {
			b.update();
		}
		for(SimObject c : ships) {
			c.update();
		}
		physicsLock.unlock();
		
		window.ready2D();
		//render2D
		window.update();
	}
	
	public void kill() {
		System.out.println("sim.kill() triggered");
		try {
			integrator.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Display.destroy();
	}
}
