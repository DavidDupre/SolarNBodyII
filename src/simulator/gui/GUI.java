package simulator.gui;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.lwjgl.opengl.Display;

import simulator.Defines;
import simulator.SimObject;
import simulator.body.Body;
import simulator.craft.Craft;

public class GUI {
	private ObjectTree objectPane;
	private int width, height;
	private int windowWidth, windowHeight;
	private List<SimObject> bodies;
	private List<SimObject> ships;
	private int requestedSpeed = Defines.SECONDS_PER_TICK;
	private JLabel simSpeedLabel;

	public GUI(int width, int height, List<SimObject> bodies,
			List<SimObject> ships) {
		this.width = width;
		this.height = height;
		windowWidth = (int) (((float) width) * .8f);
		windowHeight = (int) (((float) height) * .8f);
		this.bodies = bodies;
		this.ships = ships;
		Canvas canvas = new Canvas();

		canvas.setSize(windowWidth, windowHeight);
		canvas.setFocusable(true);
		canvas.setIgnoreRepaint(true);

		JPanel view = new JPanel();
		view.add(canvas);
		view.setVisible(true);
		canvas.setVisible(true);

		JPanel rootPanel = new JPanel(new GridBagLayout());
		JFrame rootFrame = new JFrame();
		rootFrame.getContentPane().add(rootPanel);

		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootFrame.setSize(new Dimension(width, height));
		rootFrame.setVisible(true);
		rootFrame.setTitle("Solar N-Body");

		objectPane = new ObjectTree();
		
		//Speed settings panel
		JPanel speedPanel = new JPanel(new GridLayout(1, 0));
		final String pauseString = "||";
		final String slowSpeedString = ">";
		final String mediumSpeedString = ">>";
		final String fastSpeedString = ">>>";
		JRadioButton pauseButton = new JRadioButton(pauseString);
		JRadioButton slowSpeedButton = new JRadioButton(slowSpeedString);
		JRadioButton mediumSpeedButton = new JRadioButton(mediumSpeedString);
		JRadioButton fastSpeedButton = new JRadioButton(fastSpeedString);
		ButtonGroup speedButtonGroup = new ButtonGroup();
		speedButtonGroup.add(pauseButton);
		speedButtonGroup.add(slowSpeedButton);
		speedButtonGroup.add(mediumSpeedButton);
		speedButtonGroup.add(fastSpeedButton);
		ActionListener speedListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch(e.getActionCommand()) {
				case pauseString:
					requestedSpeed = 0;
					break;
				case slowSpeedString:
					requestedSpeed = 1;
					break;
				case mediumSpeedString:
					requestedSpeed = 10;
					break;
				case fastSpeedString:
					requestedSpeed = 100;
					break;
				default:
					requestedSpeed = Defines.SECONDS_PER_TICK;
					break;
				}
			}
		};
		for(Enumeration<AbstractButton> buttons = speedButtonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			button.addActionListener(speedListener);
			speedPanel.add(button);
		}
		
		//Speed notifications
		JPanel simSpeedPanel = new JPanel();
		simSpeedLabel = new JLabel("Simulation speed: 0x");
		simSpeedPanel.add(simSpeedLabel);

		//Place components onto root frame
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		rootPanel.add(view, gbc);

		gbc.gridx = 0;
		rootPanel.add(objectPane, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		rootPanel.add(speedPanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		rootPanel.add(simSpeedPanel, gbc);

		try {
			Display.setParent(canvas);
		} catch (Exception e) {
			// fyuck you
		}

		rootPanel.revalidate();
	}

	@SuppressWarnings("serial")
	private class ObjectTree extends JPanel {
		private JTree tree;
		private DefaultMutableTreeNode root;
		private SimObject requestedObject = bodies.get(Defines.FOCUS_BODY);
		private List<DefaultMutableTreeNode> nodes;
		private ClassLoader cl;

		public ObjectTree() {
			// create the root note
			cl = this.getClass().getClassLoader();
			root = new DefaultMutableTreeNode("System");
			// create the tree by passing in the root node
			tree = new JTree(root);
			JScrollPane scroll = new JScrollPane(tree);
			scroll.setPreferredSize(new Dimension(200, getWindowHeight()));
			add(scroll);

			// Renderer for custom icons
			tree.setCellRenderer(new DefaultTreeCellRenderer() {
				private Icon starIcon = new ImageIcon(cl.getResource("simulator/images/star.png"));
				private Icon planetIcon = new ImageIcon(cl.getResource("simulator/images/planet.png"));
				private Icon asteroidIcon = new ImageIcon(cl.getResource(
						"simulator/images/asteroid.png"));
				private Icon moonIcon = new ImageIcon(cl.getResource("simulator/images/moon.png"));
				private Icon craftIcon = new ImageIcon(cl.getResource("simulator/images/craft.png"));

				@Override
				public Component getTreeCellRendererComponent(JTree tree,
						Object value, boolean selected, boolean expanded,
						boolean isLeaf, int row, boolean focused) {
					Component c = super.getTreeCellRendererComponent(tree,
							value, selected, expanded, isLeaf, row, focused);
					if (!((DefaultMutableTreeNode) value).getUserObject()
							.equals("System")) {
						switch (((SimObject) ((DefaultMutableTreeNode) value)
								.getUserObject()).getType()) {
						case STAR:
							setIcon(starIcon);
							break;
						case PLANET:
							setIcon(planetIcon);
							break;
						case DWARF_PLANET:
							setIcon(planetIcon);
							break;
						case ASTEROID:
							setIcon(asteroidIcon);
							break;
						case MOON:
							setIcon(moonIcon);
							break;
						case CRAFT:
							setIcon(craftIcon);
							break;
						default:
							setIcon(moonIcon);
							break;
						}
					}
					return c;
				}
			});

			// Selection listener
			tree.getSelectionModel().addTreeSelectionListener(
					new TreeSelectionListener() {
						@Override
						public void valueChanged(TreeSelectionEvent e) {
							DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
									.getLastSelectedPathComponent();
							if (selectedNode != null) {
								requestedObject = ((SimObject) selectedNode
										.getUserObject());
							}
						}
					});

			// Mouse listener
			tree.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (SwingUtilities.isRightMouseButton(e)) {
						TreePath path = tree.getPathForLocation(e.getX(),
								e.getY());
						Rectangle pathBounds = tree.getUI().getPathBounds(tree,
								path);
						if (pathBounds != null
								&& pathBounds.contains(e.getX(), e.getY())) {
							SimObject simObject = ((SimObject) ((DefaultMutableTreeNode) path
									.getLastPathComponent()).getUserObject());
							SimPopupMenu menu = new SimPopupMenu(simObject);
							menu.show(tree, pathBounds.x, pathBounds.y
									+ pathBounds.height);
						}
					}
				}
			});

			// Populate tree
			nodes = new ArrayList<DefaultMutableTreeNode>();
			nodes.add(new DefaultMutableTreeNode(bodies.get(0)));
			root.add(nodes.get(0));
			for (SimObject b : bodies) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(b);
				if (b.getParent() != null) {
					for (DefaultMutableTreeNode n : nodes) {
						if (b.getParent().equals(n.getUserObject())) {
							n.add(newNode);
						}
					}
					nodes.add(newNode);
				}
			}
			for (SimObject c : ships) {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(c);
				if (c.getParent() != null) {
					for (DefaultMutableTreeNode n : nodes) {
						if (c.getParent().equals(n.getUserObject())) {
							n.add(newNode);
						}
					}
				}
			}

			// Hide root node
			tree.expandRow(0);
			tree.setShowsRootHandles(true);
			tree.setRootVisible(false);
		}

		public SimObject getFocusRequest() {
			return requestedObject;
		}
	}

	//TODO this could be optimized so we don't have to make new menus and listeners every time
	@SuppressWarnings("serial")
	private class SimPopupMenu extends JPopupMenu {
		public SimPopupMenu(final SimObject simObject) {
			final boolean state = simObject.getDrawConic();
			JCheckBoxMenuItem conicItem = new JCheckBoxMenuItem("Draw conic", state);
			conicItem.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					simObject.setDrawConic(!state);
				}
			});
			add(conicItem);
		}
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	public SimObject getFocusRequest() {
		return objectPane.getFocusRequest();
	}
	
	public int getSpeedRequest() {
		return requestedSpeed;
	}

	public void setSimSpeedLabel(int deltaTime) {
		this.simSpeedLabel.setText("Simulation speed: " + deltaTime + "x");
	}
}
