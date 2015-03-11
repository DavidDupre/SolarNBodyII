package simulator;

import java.util.HashMap;

import simulator.body.Body;
import simulator.utils.Astrophysics;
import simulator.utils.Vector3D;

public abstract class PhysicsComponent extends Component {
	protected void calculateOrbit() {
		Vector3D r = simObject.getRelativePos();
		Vector3D v = simObject.getRelativeVel();
		HashMap<String, Double> orb = Astrophysics.toOrbitalElements(r, v,
				simObject.getParent());
		simObject.setOrbit(orb);
		if (orb.get("e") > 1) {
			if (simObject.getParent() != null) {
				if (simObject.getParent().getParent() != null) {
					Body newParent = simObject.getParent().getParent();
					simObject.setParent(newParent);
					System.out.println(simObject.getName() + " escaped to "
							+ newParent.getName());
				} else {
					// escaped solar system
				}
			}
		}
	}
}
