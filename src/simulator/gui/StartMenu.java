package simulator.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.utils.PropertiesManager;

public class StartMenu {
	private int width;
	private int height;
	private boolean selected;
	private NumberFormat numFormat;

	public StartMenu(int width, int height) throws IOException {
		this.width = width;
		this.height = height;
		selected = false;
		final JPanel rootPanel = new JPanel(new GridBagLayout());
		final JFrame rootFrame = new JFrame();
		rootFrame.setContentPane(rootPanel);

		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootFrame.setSize(new Dimension(width, height));
		rootFrame.setVisible(true);
		rootFrame.setTitle("Menu");

		File jarPath = new File(this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		final File rootDirectory = jarPath.getParentFile();

		final String dataFilePath = PropertiesManager.getProperty("dataFile");

		String simpleName = new File(dataFilePath).getName();
		JButton continueButton = new JButton("Run " + simpleName);
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				selected = true;
				rootFrame.dispose();
			}
		});

		final File dataFile = new File(rootDirectory.getPath() + "/resources");

		JButton selectButton = new JButton("Select File");
		selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(dataFile);
				fileChooser.setFileFilter(new FileNameExtensionFilter("*.csv",
						"csv"));
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					PropertiesManager.setProperty("dataFile", fileChooser
							.getSelectedFile().getAbsolutePath());
					selected = true;
					rootFrame.dispose();
				}
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		Dimension buttonSize = new Dimension(200, 50);

		// Options/settings stuffs
		final JPanel optionPanel = new JPanel(new GridBagLayout());
		JLabel timestepLabel = new JLabel("Max timestep");
		final JFormattedTextField timestepCap = new JFormattedTextField(
				numFormat);
		timestepCap.setValue(Double.valueOf(
				PropertiesManager.getProperty("timestep")).longValue());
		timestepCap.setColumns(10);
		final JCheckBox dynamicTimestepCheckbox = new JCheckBox(
				"Dynamic timesteps");
		dynamicTimestepCheckbox.setSelected(Boolean
				.parseBoolean(PropertiesManager.getProperty("dynamicTime")));
		JButton backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				PropertiesManager.setProperty("dynamicTime",
						Boolean.toString(dynamicTimestepCheckbox.isSelected()));
				long timestep = Double.valueOf(timestepCap.getText())
						.longValue();
				if (timestep > 0) {
					PropertiesManager.setProperty("timestep",
							Long.toString(timestep));
				}
				rootFrame.setContentPane(rootPanel);
				rootFrame.revalidate();
			}
		});
		JButton optionButton = new JButton("Options");
		optionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rootFrame.setContentPane(optionPanel);
				rootFrame.revalidate();
			}
		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		optionPanel.add(dynamicTimestepCheckbox, gbc);
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		optionPanel.add(timestepLabel, gbc);
		gbc.gridx = 1;
		optionPanel.add(timestepCap, gbc);
		gbc.gridwidth = 2;
		gbc.gridx=0;
		gbc.gridy = 2;
		backButton.setPreferredSize(buttonSize);
		optionPanel.add(backButton, gbc);

		continueButton.setPreferredSize(buttonSize);
		selectButton.setPreferredSize(buttonSize);
		optionButton.setPreferredSize(buttonSize);
		gbc.gridx = 0;
		gbc.gridy = 0;
		rootPanel.add(continueButton, gbc);
		gbc.gridy = 1;
		rootPanel.add(selectButton, gbc);
		gbc.gridy = 2;
		rootPanel.add(optionButton, gbc);

		rootFrame.revalidate();
	}

	public boolean isSelected() {
		return selected;
	}
}
