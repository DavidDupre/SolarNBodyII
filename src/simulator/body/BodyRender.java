package simulator.body;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

import simulator.Defines;
import simulator.RenderComponent;
import simulator.Timer;
import simulator.utils.Resources;

public class BodyRender extends RenderComponent {
	private Body body;
	private Sphere image;
	private int imageID;
	private Texture tex;
	private boolean isTextured = false;
	private String texturePath;

	public BodyRender(Defines.BodyType t) {
		this.type = t;
		this.color = Defines.colors.get(t);
		body = (Body) simObject;

		image = new Sphere();
	}
	
	@Override
	public void initGL(){
		imageID = GL11.glGenLists(1);
		if(isTextured){
			tex = Resources.get(texturePath);
			GL11.glNewList(imageID, GL11.GL_COMPILE);
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
			image.draw((float) ((Body) simObject).getRadius(), 25, 25);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();
			GL11.glEndList();
		} else {
			GL11.glNewList(imageID, GL11.GL_COMPILE);
			GL11.glPushMatrix();
			image.draw((float) ((Body) simObject).getRadius(), 25, 25);
			GL11.glPopMatrix();
			GL11.glEndList();
		}
	}

	@Override
	public void update() {
		if (body == null) {
			body = (Body) simObject;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(body.getPos().x, body.getPos().y, body.getPos().z);
		GL11.glColor3f(color[0], color[1], color[2]);

		GL11.glRotated(body.getTilt(), 0, 1, 0);
		GL11.glRotated(body.getRotation(), 0, 0, 1);
		
		GL11.glCallList(imageID);

		if (drawConic && (body.getParent() != null)) {
			drawConic();
		}

		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex3d(0, 0, 0);
		GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	public void setTexturePath(String path) {
		isTextured = true;
		image.setTextureFlag(true);
		texturePath = path;
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub

	}
}
