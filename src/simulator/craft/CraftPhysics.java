package simulator.craft;

import simulator.PhysicsComponent;

public class CraftPhysics extends PhysicsComponent {
	private Craft ship;
	
	public CraftPhysics() {
		ship = (Craft) simObject;
	}

	@Override
	public void update() {
		if(simObject.isDisplayingInfo() || simObject.getDrawConic()){
			calculateOrbit();
		}
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}

}
