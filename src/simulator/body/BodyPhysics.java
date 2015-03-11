package simulator.body;

import simulator.PhysicsComponent;

public class BodyPhysics extends PhysicsComponent {
	private Body body;

	public BodyPhysics() {
		body = (Body) simObject;
	}

	@Override
	public void update() {
		if((simObject.isDisplayingInfo() || simObject.getDrawConic()) && simObject.getParent() != null){
			calculateOrbit();
		}
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub

	}
}
