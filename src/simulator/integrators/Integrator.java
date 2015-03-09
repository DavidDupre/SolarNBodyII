package simulator.integrators;

import simulator.Defines;
import simulator.utils.Astrophysics;

public abstract class Integrator extends Thread {
	protected int timeStep = Defines.TIMESTEP;
	private double speedRequest = Defines.SECONDS_PER_TICK;

	public abstract double getElapsedTime();

	public void setTimeStep(double d) {
		if (d != this.speedRequest) {
			timeStep = (int) (d / Math.sqrt(Astrophysics.G));
			this.speedRequest = d;
		}
	}
}
