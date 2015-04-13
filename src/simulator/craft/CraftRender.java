package simulator.craft;

import org.lwjgl.opengl.GL11;

import simulator.Defines;
import simulator.RenderComponent;

public class CraftRender extends RenderComponent {
	private Craft craft;
	
	public CraftRender(Defines.BodyType t) {
		this.type = t;
		craft = (Craft) simObject;
	}
	
	@Override
	public void initGL() {
		// ayy lmao
	}
	
	@Override
	public void update() {
		if (craft == null) {
			craft = (Craft) simObject;
		}
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslated(craft.getPos().x, craft.getPos().y, craft.getPos().z);
		GL11.glColor3f(1f, 1f, 1f);
		
		if (drawConic && (craft.getParent() != null)) {
			drawConic();
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
