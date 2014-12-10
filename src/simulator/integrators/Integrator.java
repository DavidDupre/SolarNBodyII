package simulator.integrators;

import java.util.List;

import simulator.Defines;
import simulator.SimObject;
import simulator.body.Body;
import simulator.craft.Craft;
import simulator.utils.Astrophysics;

public abstract class Integrator extends Thread {
	protected int timeStep = Defines.TIMESTEP;
	private int speedRequest = Defines.SECONDS_PER_TICK;

	public abstract double getElapsedTime();

	public void setTimeStep(int speedRequest) {
		if (speedRequest != this.speedRequest) {
			timeStep = (int) (speedRequest / Math.sqrt(Astrophysics.G));
			this.speedRequest = speedRequest;
		}
	}
}
