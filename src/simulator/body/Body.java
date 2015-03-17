package simulator.body;

import simulator.Component;
import simulator.Defines;
import simulator.Defines.BodyType;
import simulator.SimObject;
import simulator.utils.Vector3D;

public class Body extends SimObject {
	private double mass;
	private double systemMass;
	private double radius;
	private float tilt;
	private double period;
	private double rotation = 0;

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
	
	@Override
	public BodyRender getRender() {
		return (BodyRender) components.get(Defines.RENDER);
	}
	
	public void setTilt(float tilt) {
		this.tilt = tilt;
	}
	
	public float getTilt() {
		return tilt;
	}
	
	public void setPeriod(double period) {
		this.period = period;
	}
	
	public double getPeriod() {
		return period;
	}
	
	public void setRotation(double d) {
		this.rotation = d%360.0f;
	}
	
	public double getRotation() {
		return rotation;
	}
}
