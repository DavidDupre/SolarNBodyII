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
	private boolean selected;
	private NumberFormat numFormat;
	private final JFrame rootFrame;
	private final JPanel rootPanel;
	private File dataFile;
	private FileButton continueButton;

	public StartMenu(int width, int height) throws IOException {
		selected = false;
		rootPanel = new JPanel(new GridBagLayout());
		rootFrame = new JFrame();
		rootFrame.setContentPane(rootPanel);

		rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rootFrame.setSize(new Dimension(width, height));
		rootFrame.setVisible(true);
		rootFrame.setTitle("Menu");

		GridBagConstraints gbc = new GridBagConstraints();
		Dimension buttonSize = new Dimension(200, 50);
		
		continueButton = new FileButton();

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
		
		final JPanel editorPanel = new EditorPanel();
		JButton editButton = new JButton("Edit Simulation");
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				rootFrame.setContentPane(editorPanel);
				rootFrame.revalidate();
			}
		});

		continueButton.setPreferredSize(buttonSize);
		editButton.setPreferredSize(buttonSize);
		optionButton.setPreferredSize(buttonSize);
		gbc.gridx = 0;
		gbc.gridy = 0;
		rootPanel.add(continueButton, gbc);
		gbc.gridy = 1;
		rootPanel.add(editButton, gbc);
		gbc.gridy = 2;
		rootPanel.add(optionButton, gbc);

		rootFrame.revalidate();
	}
	
	@SuppressWarnings("serial")
	private class FileButton extends JButton {
		public FileButton() {
			update();
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					selected = true;
					rootFrame.dispose();
				}
			});
		}
		
		public void update() {
			File jarPath = new File(this.getClass().getProtectionDomain()
					.getCodeSource().getLocation().getPath());
			final File rootDirectory = jarPath.getParentFile();

			final String dataFilePath = PropertiesManager.getProperty("dataFile");
			
			String simpleName = new File(dataFilePath).getName();
			this.setText("Run " + simpleName);
			
			dataFile = new File(rootDirectory.getPath() + "//resources");
		}
	}
	
	@SuppressWarnings("serial")
	private class EditorPanel extends JPanel {
		public EditorPanel() {
			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();

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
					}
				}
			});
			this.add(selectButton, gbc);
			
			JButton backButton = new JButton("Back");
			backButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					rootFrame.setContentPane(rootPanel);
					rootFrame.revalidate();
					continueButton.update();
				}
			});
			gbc.gridy = 1;
			this.add(backButton, gbc);			
		}
	}

	public boolean isSelected() {
		return selected;
	}
}
