package simulator;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glTranslated;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import simulator.gui.GUI;
import simulator.utils.Vector3D;

public class Window {
	private int windowWidth, windowHeight;
	public static double centerDistance = 50;
	public static double yaw = 0;
	public static double pitch = -120;
	private double horizontalTan;
	private double aspect;
	private GUI gui;

	public Window(int width, int height, List<SimObject> bodies, List<SimObject> ships) {
		gui = new GUI(width, height, bodies, ships);
		this.windowWidth = gui.getWindowWidth();
		this.windowHeight = gui.getWindowHeight();
		aspect = (float) windowHeight / (float) windowWidth;
		horizontalTan = Math.tan(Math.toRadians(25)); //no idea
	}

	public void init() {
		try {
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		ready3D();
		updateCamera();
		
		GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL_BLEND);
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		
		initLighting();
	}
	
	public void initLighting() {
		FloatBuffer matSpecular = BufferUtils.createFloatBuffer(4);
		matSpecular.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(1.0f).put(1.0f).put(1.0f).put(0.0f).flip();
		
		FloatBuffer whiteLight = BufferUtils.createFloatBuffer(4);
		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		
		FloatBuffer lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, matSpecular);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);	
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition);	
		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, whiteLight);
		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, whiteLight);	
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, lModelAmbient);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);	
		//GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		//GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public boolean isActive() {
		return Display.isActive();
	}

	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	public void ready2D() {
		glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); //makes fonts work
		
		glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();

	    GLU.gluOrtho2D(0.0f, windowWidth, windowHeight, 0.0f);

	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	    glTranslated(0.375, 0.375, 0.0);
	    
        glDisable(GL_DEPTH_TEST);
	}
	
	public void ready3D() {		
		GL11.glViewport(0, 0, windowWidth, windowHeight); // Reset The Current Viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix
		
		GL11.glFrustum(-horizontalTan, horizontalTan, aspect * horizontalTan,
				aspect * -horizontalTan, 1.0, 1000000000.0);

		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		GL11.glLoadIdentity(); // Reset The Modelview Matrix
		
		GL11.glDisable(GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
	}
	
	public void updateCamera() {
		GL11.glTranslated(0, 0, -centerDistance);
		GL11.glRotated(pitch, 1, 0, 0);
		GL11.glRotated(yaw, 0, 0, 1);
		GL11.glScaled(1E-17, 1E-17, 1E-17);
	}

	public void update() {
		if (!Display.isCloseRequested()) {
			pollInput();
			Display.update();
			Display.sync(60);
		} else {
			System.out.println("killing program via window.update()");
			Display.destroy();
			System.exit(0);
		}
	}

	private void pollInput() {
		if (Mouse.isButtonDown(1)) {
			int mouseDX = Mouse.getDX();
			int mouseDY = Mouse.getDY();
			if ((mouseDY > 0 && pitch < 0) || (mouseDY < 0 && pitch > -180)) {
				pitch += mouseDY / 1.5;
			}
			yaw += mouseDX / 2;
		}

		double zoom = Mouse.getDWheel() * (Math.abs(centerDistance) / 1000);
		centerDistance -= zoom;
		if (centerDistance < 1) {
			centerDistance = 1;
		}
	}

	public void focus() {
		Vector3D pos = gui.getFocusRequest().getPos();
		GL11.glTranslated(-pos.x, -pos.y, -pos.z);
	}

	public double getSpeedRequest() {
		return gui.getSpeedRequest();
	}

	public void setSimSpeedLabel(int deltaTime) {
		gui.setSimSpeedLabel(deltaTime);
	}
}
