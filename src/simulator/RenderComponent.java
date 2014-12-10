package simulator;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import simulator.utils.Astrophysics;
import simulator.utils.Conic;
import simulator.utils.Vector3D;

public abstract class RenderComponent extends Component {
	protected boolean drawConic;
	protected float[] color;
	protected Defines.BodyType type;
	
	protected void drawConic(SimObject simObject) {
		Vector3D r = simObject.getRelativePos();
		Vector3D v = simObject.getRelativeVel();
		HashMap<String, Double> orb = Astrophysics.toOrbitalElements(r, v, simObject.getParent());
		double anomaly = orb.get("v"); //get the current true anomaly so the ellipse passes through the body and looks good
		Conic conic = new Conic(orb);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (int i=0; i < 100; i++){
			Vector3D pos = conic.getPosition(i*Math.PI/49 + anomaly);
			pos.subtract(r);
			GL11.glVertex3d(pos.x, pos.y, pos.z);
		}
		GL11.glEnd();
	}
	
	public void setDrawConic(boolean b) {
		drawConic = b;
	}
	
	public boolean getDrawConic() {
		return drawConic;
	}
	
	public Defines.BodyType getType() {
		return type;
	}
}
