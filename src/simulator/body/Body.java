package simulator.body;

import simulator.Component;
import simulator.Defines.BodyType;
import simulator.SimObject;
import simulator.utils.Vector3D;

public class Body extends SimObject {
	private double mass;
	private double systemMass;
	private double radius;

	public Body(int id, String name, Body parent, BodyType type, double mass,
			double systemMass, double radius, Vector3D[] state,
			BodyPhysics physics, BodyRender render) {
		super(id, physics, render);
		this.mass = mass;
		this.systemMass = systemMass;
		this.radius = radius;
		this.parent = parent;
		this.pos = state[0];
		this.vel = state[1];
		this.name = name;
		for (Component c : components.values()) {
			c.setModifier(this);
		}
	}

	@Override
	public void update() {
		for (Component c : components.values()) {
			c.update();
		}
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double m) {
		this.mass = m;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double r) {
		this.radius = r;
	}

	public double getSystemMass() {
		return systemMass;
	}
}
