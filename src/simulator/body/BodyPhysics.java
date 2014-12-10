package simulator.body;

import simulator.PhysicsComponent;
import simulator.utils.Vector3D;

public class BodyPhysics extends PhysicsComponent {
	private Body body;
	
	public BodyPhysics() {
		body = (Body) simObject;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}

}
