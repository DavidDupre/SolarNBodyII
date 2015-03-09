package simulator.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.utils.GetProperties;

public class StartMenu {
	private int width;
	private int height;
	private boolean selected;
	private File selectedFile;

	public StartMenu(int width, int height) throws IOException {
		this.width = width;
		this.height = height;
		selected = false;
		JPanel rootPanel = new JPanel(new GridBagLayout());
		final JFrame rootFrame = new JFrame();
		rootFrame.getContentPane().add(rootPanel);

		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootFrame.setSize(new Dimension(width, height));
		rootFrame.setVisible(true);
		rootFrame.setTitle("Menu");

		File jarPath = new File(this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().getPath());
		final File rootDirectory = jarPath.getParentFile();

		// load properties
		final Properties prop = new Properties();
		InputStream propIS = null;
		File propFile = new File(rootDirectory + "/resources/config.properties");
		try {
			propIS = new FileInputStream(propFile);
		} catch (FileNotFoundException e1) {
			System.out.println("config not found");
			e1.printStackTrace();
		}
		prop.load(propIS);
		final String dataFilePath = prop.getProperty("dataFile");
		propIS.close();

		String simpleName = new File(dataFilePath).getName();
		JButton continueButton = new JButton("Run " + simpleName);
		continueButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				selectedFile = new File(dataFilePath);
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
					selectedFile = fileChooser.getSelectedFile();
					prop.setProperty("dataFile", selectedFile.getAbsolutePath());
					try {
						FileOutputStream propOut = new FileOutputStream(
								rootDirectory + "/resources/config.properties");
						prop.store(propOut, null);
						propOut.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					selected = true;
					rootFrame.dispose();
				}
			}
		});

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		rootPanel.add(continueButton, gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		rootPanel.add(selectButton, gbc);
	}

	public boolean isSelected() {
		return selected;
	}

	public File getSelectedFile() {
		return selectedFile;
	}
}
