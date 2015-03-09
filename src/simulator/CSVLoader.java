package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import simulator.Defines.BodyType;
import simulator.body.Body;
import simulator.body.BodyPhysics;
import simulator.body.BodyRender;
import simulator.craft.Craft;
import simulator.craft.CraftPhysics;
import simulator.craft.CraftRender;
import simulator.utils.Astrophysics;
import simulator.utils.Vector3D;

public class CSVLoader {

	// creates a list of bodies from a csv file.
	// TODO less copy-paste

	public ArrayList<SimObject> bodies;
	public ArrayList<SimObject> ships;

	public CSVLoader() {
		bodies = new ArrayList<SimObject>();
		ships = new ArrayList<SimObject>();
	}

	public ArrayList<SimObject> getBodies(String filePath) {
		BufferedReader brBodies = null;

		String line = "";
		String cvsSplitBy = ",";

		int n = 0;
		try {
			InputStream is = new FileInputStream(filePath);
	        brBodies = new BufferedReader(new InputStreamReader(is));
			while (((line = brBodies.readLine()) != null)) {
				String[] column = line.split(cvsSplitBy);
				if (n > 0) { // skip header row
					loadBody(column);
				}
				n++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (brBodies != null) {
				try {
					brBodies.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return bodies;
	}

	public List<SimObject> getShips(String filePath) {
		BufferedReader brShips = null;

		String line = "";
		String cvsSplitBy = ",";

		int n = 0;
		try {
			InputStream is = new FileInputStream(filePath);
			brShips = new BufferedReader(new InputStreamReader(is));
			while (((line = brShips.readLine()) != null)) {
				String[] column = line.split(cvsSplitBy);
				if (n > 0) { // skip header row
					loadCraft(column);
				}
				n++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (brShips != null) {
				try {
					brShips.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return ships;
	}

	private void loadBody(String[] column) {
		int id = Integer.parseInt(column[0]);
		String name = column[1];
		Body parent = null;
		double mass = Double.parseDouble(column[3]) / Astrophysics.G;
		double radius = Double.parseDouble(column[4]) / Astrophysics.G;
		double systemMass = Double.parseDouble(column[7]) / Astrophysics.G;
		double a = Double.parseDouble(column[8]) / Astrophysics.G;
		double e = Double.parseDouble(column[9]);
		double i = Double.parseDouble(column[10]);
		double node = Double.parseDouble(column[11]);
		double peri = Double.parseDouble(column[12]);
		double anom = Double.parseDouble(column[13]);
		String t = column[15];
		BodyType type;
		switch (t) {
		case "Star":
			type = BodyType.STAR;
			break;
		case "Planet":
			type = BodyType.PLANET;
			break;
		case "Dwarf Planet":
			type = BodyType.DWARF_PLANET;
			break;
		case "Moon":
			type = BodyType.MOON;
			break;
		case "Asteroid":
			type = BodyType.ASTEROID;
			break;
		default:
			type = BodyType.MOON;
			break;
		}

		Vector3D[] state = new Vector3D[2];
		state[0] = new Vector3D();
		state[1] = new Vector3D();
		if (Integer.parseInt(column[2]) != -1) {
			parent = (Body) bodies.get(Integer.parseInt(column[2]));
			state = Astrophysics.toRV(a, e, i, node, peri, anom, parent, true);
			state[0].add(parent.getPos());
			state[1].add(parent.getVel());
		}

		Body newBody = new Body(bodies.size(), name, parent, type, mass,
				systemMass, radius, state, new BodyPhysics(), new BodyRender(
						type));
		bodies.add(newBody);
	}

	private void loadCraft(String[] column) {
		int id = Integer.parseInt(column[0]);
		String name = column[1];
		Body parent = null;
		double a = Double.parseDouble(column[3]) / Astrophysics.G;
		double e = Double.parseDouble(column[4]);
		double i = Double.parseDouble(column[5]);
		double node = Double.parseDouble(column[6]);
		double peri = Double.parseDouble(column[7]);
		double anom = Double.parseDouble(column[8]);

		Vector3D[] state = new Vector3D[2];
		state[0] = new Vector3D();
		state[1] = new Vector3D();
		if (Integer.parseInt(column[2]) != -1) {
			parent = (Body) bodies.get(Integer.parseInt(column[2]));
			state = Astrophysics.toRV(a, e, i, node, peri, anom, parent, true);
			state[0].add(parent.getPos());
			state[1].add(parent.getVel());
		}

		Craft newCraft = new Craft(ships.size(), name, parent, state,
				new CraftPhysics(), new CraftRender(Defines.BodyType.CRAFT));
		ships.add(newCraft);
	}
}
