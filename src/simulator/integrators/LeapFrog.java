package simulator.integrators;

import java.util.List;

import simulator.SimObject;
import simulator.Simulation;
import simulator.body.Body;
import simulator.craft.Craft;
import simulator.utils.Vector3D;

public class LeapFrog extends Integrator {
	private List<SimObject> bodies;
	private Vector3D[] a;
	private List<SimObject> ships;
	private Vector3D[] aCraft;
	private Thread thread;
	private long elapsedTicks = 0;
	
	public LeapFrog(List<SimObject> bList, List<SimObject> cList) {
		this.bodies = bList;
		a = new Vector3D[bodies.size()];
		for (int i = 0; i < bodies.size(); i++) { // Necessary to preload
													// accelerations for
													// leapfrog integration.
			a[i] = getAcceleration((Body) bodies.get(i));
		}
		this.ships = cList;
		aCraft = new Vector3D[ships.size()];
		for (int i = 0; i < ships.size(); i++) { 
			aCraft[i] = getAccelerationForCraft((Craft) ships.get(i));
		}
	}
	
	private double getSimpleMass(Body body1, Body body2) {
		double mass = 0;
		Body ancestor = body1.getParent();
		while (ancestor != null) {
			if (ancestor == body2) {
				return body2.getMass();
			}
			ancestor = ancestor.getParent();
		}
		if (body1.getParent() == body2.getParent()
		// Sibling case: bundle niblings with siblings
				|| body1 == body2.getParent()
		// Child case: bundle grandchildren with child
		) {
			return body2.getSystemMass();
		}
		if (body1.getParent() != null) {
			if (body1.getParent().getParent() == body2.getParent()) {
				// Aunt/Uncle case: bundle cousins with aunt/uncle
				return body2.getSystemMass();
			}
		}
		return mass;
	}

	private Vector3D getAcceleration(Body body1) {
		// Uses system mass instead of accounting for moons
		Vector3D aTotal = new Vector3D();
		for (int i = 0; i < bodies.size(); i++) {
			Body body2 = (Body) bodies.get(i);
			if (body1 == body2) {
				break;
			}

			double mass = getSimpleMass(body1, body2);
			if (mass == 0) {
				break;
			}

			Vector3D diff = body2.getPos().clone().subtract(body1.getPos().clone());
			double mag = diff.magnitude();
			Vector3D a = diff.multiply(mass / Math.pow(mag, 3));
			aTotal.add(a);
		}
		return aTotal;
	}
	
	private Vector3D getAccelerationForCraft(Craft craft) {
		// Uses mass of all bodies
		Vector3D aTotal = new Vector3D();
		for (int i = 0; i < bodies.size(); i++) {
			Body body = (Body) bodies.get(i);

			if (body.getMass() == 0) {
				break;
			}

			Vector3D diff = body.getPos().clone().subtract(craft.getPos());
			double mag = diff.magnitude();
			Vector3D a = diff.multiply(body.getMass() / Math.pow(mag, 3));
			aTotal.add(a);
		}
		return aTotal; //.multiply(Astrophysics.G);
	}
	
	private void simulate(double t) {
		Simulation.physicsLock.lock();
		// Do physics for bodies
		for (int k = 0; k < bodies.size(); k++) {
			Body b = (Body) bodies.get(k);
			Vector3D vHalf = b.getVel().clone().add(a[k].multiply(t / 2d));
			b.getPos().add(vHalf.clone().multiply(t));
			a[k] = getAcceleration(b);
			b.setVel(vHalf.add(a[k].clone().multiply(t / 2.0)));
		}
		
		// Do physics for craft
		for (int i = 0; i < ships.size(); i++) {
			Craft c = (Craft) ships.get(i);
			Vector3D vHalf = c.getVel().clone().add(aCraft[i].multiply(t / 2d));
			c.getPos().add(vHalf.clone().multiply(t));
			aCraft[i] = getAccelerationForCraft(c);
			c.setVel(vHalf.add(aCraft[i].clone().multiply(t / 2.0)));
		}
		Simulation.physicsLock.unlock();
	}
	
	public void run() {
		while(true) {
			simulate(timeStep);
			elapsedTicks += 1;
		}
	}
	
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setName("Leapfrog");
			thread.start();
		}
	}

	@Override
	public double getElapsedTime() {
		double elapsedTime = elapsedTicks * timeStep;
		elapsedTicks = 0;
		return elapsedTime;
	}
}
