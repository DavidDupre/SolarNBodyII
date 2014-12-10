package simulator.craft;

import simulator.Component;
import simulator.SimObject;
import simulator.body.Body;
import simulator.utils.Vector3D;

public class Craft extends SimObject {

	public Craft(int id, String name, Body parent, Vector3D[] state,
			CraftPhysics physics, CraftRender render) {
		super(id, physics, render);
		this.name = name;
		this.parent = parent;
		this.pos = state[0];
		this.vel = state[1];
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
}
