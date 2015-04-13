package simulator.body;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;

import simulator.Defines;
import simulator.RenderComponent;
import simulator.Window;
import simulator.utils.Resources;

public class BodyRender extends RenderComponent {
	private Body body;
	private Sphere image;
	private int imageID;
	private Texture tex;
	private boolean isTextured = false;
	private String texturePath;
	private FloatBuffer lightAmbient;
	private FloatBuffer lightDiffuse;
	private FloatBuffer lightSpecular;
	private FloatBuffer lightPosition;
	private float tilt;
	private double rot;

	public BodyRender(Defines.BodyType t) {
		this.type = t;
		this.color = Defines.colors.get(t);
		body = (Body) simObject;

		image = new Sphere();
		lightAmbient = BufferUtils.createFloatBuffer(4);
		lightAmbient.put(0.0f).put(0.0f).put(0.0f).put(1.0f).flip();
		lightDiffuse = BufferUtils.createFloatBuffer(4);
		lightDiffuse.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		lightSpecular = BufferUtils.createFloatBuffer(4);
		lightSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
	}

	@Override
	public void initGL() {
		imageID = GL11.glGenLists(1);
		if (isTextured) {
			tex = Resources.get(texturePath);
			GL11.glNewList(imageID, GL11.GL_COMPILE);
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE,
					GL11.GL_REPLACE);
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
	
	public void light() {
		if (body == null) {
			body = (Body) simObject;
		}
		if (body.getType() != Defines.BodyType.STAR) {
			GL11.glRotated(-Window.yaw, 0, 0, 1); // TODO do this better
			GL11.glRotated(-Window.pitch, 1, 0, 0);
			GL11.glEnable(GL11.GL_LIGHTING);
			lightPosition = BufferUtils.createFloatBuffer(4);
			lightPosition.put((float) -body.getPos().x).put((float) -body.getPos().y)
					.put((float) -body.getPos().z).put(0.0f).flip();
			GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);
			GL11.glRotated(Window.pitch, 1, 0, 0);
			GL11.glRotated(Window.yaw, 0, 0, 1);
		} else {
			GL11.glDisable(GL11.GL_LIGHTING);
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
		
		GL11.glDisable(GL11.GL_LIGHTING);
		if (drawConic && (body.getParent() != null)) {
			drawConic();
		}
		GL11.glBegin(GL11.GL_POINTS);
		GL11.glVertex3d(0, 0, 0);
		GL11.glEnd();
		
		//light();

		tilt = body.getTilt();
		rot = body.getRotation();
		GL11.glRotated(tilt, 0, 1, 0);
		GL11.glRotated(rot, 0, 0, 1);
		
		// Draw sphere
		GL11.glCallList(imageID);
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
