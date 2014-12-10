package simulator;

import simulator.Component;
import simulator.body.Body;
import simulator.utils.Vector3D;

import java.util.HashMap;
import java.util.Map;

public abstract class SimObject {
	public Map<String, Component> components;
	protected String name;
	protected Body parent;
	protected Vector3D pos;
	protected Vector3D vel;
	protected int id;
	
	public SimObject(int id, PhysicsComponent physics, RenderComponent render) {
		this.id = id;
		components = new HashMap<String, Component>();
		components.put(Defines.PHYSICS, physics);
		components.put(Defines.RENDER, render);
	}
	
	public abstract void update();
	
	public void setDrawConic(boolean b) {
		((RenderComponent) components.get(Defines.RENDER)).setDrawConic(b);
	}
	
	public boolean getDrawConic() {
		return ((RenderComponent) components.get(Defines.RENDER)).getDrawConic();
	}
	
	public Defines.BodyType getType() {
		return ((RenderComponent) components.get(Defines.RENDER)).getType();
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return getName();
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public Body getParent() {
		return parent;
	}

	public void setParent(Body p) {
		this.parent = p;
	}
	
	public Vector3D getPos() {
		return pos;
	}
	
	public Vector3D getRelativePos() {
		if (parent != null) {
			return pos.clone().subtract(parent.getPos());
		} else {
			return pos;
		}
	}
	
	public void setPos(Vector3D pos) {
		this.pos = pos;
	}
	
	public Vector3D getVel() {
		return vel;
	}
	
	public Vector3D getRelativeVel() {
		if (parent != null) {
			return vel.clone().subtract(parent.getVel());
		} else {
			return vel;
		}
	}
	
	public void setVel(Vector3D vel) {
		this.vel = vel;
	}
}
