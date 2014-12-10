package simulator.body;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import simulator.Defines;
import simulator.RenderComponent;

public class BodyRender extends RenderComponent {
	private Body body;
	private Sphere image;

	public BodyRender(Defines.BodyType t) {
		this.type = t;
		this.color = Defines.colors.get(t);
		body = (Body) simObject;
		image = new Sphere();
	}

	@Override
	public void update() {
		if (body == null) {
			body = (Body) simObject;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(body.getPos().x, body.getPos().y, body.getPos().z);
		GL11.glColor3f(color[0], color[1], color[2]);
		image.draw((float) (body.getRadius()), 16, 16);
		
		if (drawConic && (body.getParent() != null)) {
			drawConic(body);
		}

		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex3d(0, 0, 0);
		GL11.glEnd();
		GL11.glPopMatrix();
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub

	}
}
