package simulator.body;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import simulator.Defines;
import simulator.RenderComponent;

public class BodyRender extends RenderComponent {
	private Body body;
	private Sphere image;
	private int imageID;
	private Texture earthTexture;
	private int texID;

	public BodyRender(Defines.BodyType t) {
		this.type = t;
		this.color = Defines.colors.get(t);
		body = (Body) simObject;

		image = new Sphere();
		//image.setDrawStyle(GL11.GL_FILL);
		image.setTextureFlag(true);
		image.setNormals(GL11.GL_SMOOTH);
		//texID = GL11.glGenTextures();
	}
	
	@Override
	public void initGL(){
		earthTexture = null;
		try {
			earthTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("resources/earth.png"));
		} catch (IOException e) {
			System.out.println("texture loading failed");
			e.printStackTrace();
		}
		imageID = GL11.glGenLists(1);
		GL11.glNewList(imageID, GL11.GL_COMPILE);
		//earthTexture.bind();
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
		image.draw((float) ((Body) simObject).getRadius(), 25, 25);
		//earthTexture.release();
		GL11.glEndList();
	}

	@Override
	public void update() {
		if (body == null) {
			body = (Body) simObject;
		}
		GL11.glPushMatrix();
		GL11.glTranslated(body.getPos().x, body.getPos().y, body.getPos().z);
		GL11.glColor3f(color[0], color[1], color[2]);

		GL11.glCallList(imageID);
		//image.draw((float) (body.getRadius()), 16, 16);

		if (drawConic && (body.getParent() != null)) {
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
