package com.snj.inspect;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.poi.EncryptedDocumentException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebInspect implements ActionListener {
	static WebDriver driver;
	private static JTextField textFieldURL;
	private static JTextField textFieldRepository;
	private static JLabel labelURL;
	private static JLabel labelRepository;
	private static String editFieldLabelURL = "Enter application URL  ";
	private static String editFieldLabelRepository = "Enter repository name";
	private static JCheckBox checkBoxTextField;
	private static JCheckBox checkBoxButton;
	private static JCheckBox checkBoxLabel;
	private static JCheckBox checkBoxTextArea;

	private static JCheckBox checkBoxDropdown;
	private static JCheckBox checkBoxLink;
	private static JCheckBox checkBoxTable;
	private static JCheckBox checkBoxImage;

	/**
	 * Method to start the scanning the object locators
	 * 
	 * @author sanoj.swaminathan
	 * @since 31-07-2023
	 */
	public static void scanObjectLocators() {

		// Create a button
		JButton buttonStart = new JButton("Start");
		labelURL = new JLabel(editFieldLabelURL);
		textFieldURL = new JTextField("https://");
		textFieldURL.setCaretPosition(textFieldURL.getText().length());
		textFieldURL.setColumns(20);

		JButton buttonScan = new JButton("Scan");
		labelRepository = new JLabel(editFieldLabelRepository);
		textFieldRepository = new JTextField();
		textFieldRepository.setColumns(20);

		checkBoxTextField = new JCheckBox("Text Field");
		checkBoxButton = new JCheckBox("Button");
		checkBoxLabel = new JCheckBox("Label");
		checkBoxTextArea = new JCheckBox("Text Area");
		checkBoxDropdown = new JCheckBox("Dropdown");
		checkBoxLink = new JCheckBox("Link");
		checkBoxTable = new JCheckBox("Table");
		checkBoxImage = new JCheckBox("Image");

		// Create a JFrame and set its layout manager
		JFrame frame = new JFrame("SNJ Web Inspect");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		// Create two panels for each line
		JPanel panelURL = new JPanel();
		JPanel panelRepo = new JPanel();
		JPanel panelCheckbox = new JPanel();

		// Configure layout managers for each panel
		panelURL.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelRepo.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelCheckbox.setLayout(new GridLayout(2, 4, 10, 10));

		// Add the button and text field to the panel
		panelURL.add(labelURL);
		panelURL.add(textFieldURL);
		panelURL.add(buttonStart);
		panelRepo.add(labelRepository);
		panelRepo.add(textFieldRepository);

		Border border = BorderFactory.createTitledBorder("Select web component");
		panelCheckbox.setBorder(border);
		panelCheckbox.add(checkBoxTextField);
		panelCheckbox.add(checkBoxButton);
		panelCheckbox.add(checkBoxLabel);
		panelCheckbox.add(checkBoxTextArea);
		panelCheckbox.add(checkBoxDropdown);
		panelCheckbox.add(checkBoxLink);
		panelCheckbox.add(checkBoxTable);
		panelCheckbox.add(checkBoxImage);

		panelRepo.add(buttonScan);

		frame.add(panelURL);
		frame.add(panelRepo);
		frame.add(panelCheckbox);
		frame.pack();
		frame.setVisible(true);

		buttonStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String appURL = textFieldURL.getText();
				if (appURL.isEmpty() || appURL.equals("https://")) {
					JButton sourceButton = (JButton) e.getSource();
					JOptionPane.showMessageDialog(sourceButton, "Please enter a valid URL");
				} else {
					driver = new ChromeDriver();
					driver.get(appURL);
					driver.manage().window().maximize();
				}

				if (driver != null) {
					JButton sourceButton = (JButton) e.getSource();
					JOptionPane.showMessageDialog(sourceButton, "Application loaded successfully. Start your scan");
				}
			}
		});

		buttonScan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String repositoryFilePath = null;
				String repositoryName;
				String repositoryFieldValue = textFieldRepository.getText().replaceAll("\\s+", "");

				if (driver == null) {
					JButton sourceButton = (JButton) e.getSource();
					JOptionPane.showMessageDialog(sourceButton, "Please load the application by clicking Start button");
				} else {
					if (repositoryFieldValue.isEmpty()) {
						JButton sourceButton = (JButton) e.getSource();
						JOptionPane.showMessageDialog(sourceButton, "Please enter valid repository name");
					} else {
						if (checkBoxTextField.isSelected() || checkBoxButton.isSelected()
								|| checkBoxTextArea.isSelected() || checkBoxDropdown.isSelected()
								|| checkBoxLink.isSelected() || checkBoxLabel.isSelected() || checkBoxTable.isSelected()
								|| checkBoxImage.isSelected()) {

							// Convert to CamelCase
							String[] words = repositoryFieldValue.split(" ");
							StringBuilder result = new StringBuilder();
							for (String word : words) {
								result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
							}
							repositoryName = result.toString().replaceAll("[^a-zA-Z0-9]", "");

							WebElement elements = driver.findElement(By.xpath("//*"));
							Document document = Jsoup.parse(elements.getAttribute("innerHTML"));
							Elements inputTag = document.getElementsByTag("input");
							Elements buttonTag = document.getElementsByTag("button");
							Elements textareaTag = document.getElementsByTag("textarea");
							Elements selectTag = document.getElementsByTag("select");
							Elements optionTag = document.getElementsByTag("option");
							Elements aTag = document.getElementsByTag("a");
							Elements labelTags = document.getElementsByTag("label");
							Elements tableTags = document.getElementsByTag("table");
							Elements imageTags = document.getElementsByTag("img");
							Elements headingTags = document.select("h1, h2, h3, h4");
							try {
								repositoryFilePath = WebInspectUtilities.copyRepositoryTemplate(repositoryName);

								// Create object repository for selected web components
								createInputTagObjectRepository(repositoryFilePath, inputTag, repositoryName);
								createButtonTagObjectRepository(repositoryFilePath, buttonTag, repositoryName);
								createLabelTagObjectRepository(repositoryFilePath, labelTags, headingTags,
										repositoryName);
								createTextAreaTagObjectRepository(repositoryFilePath, textareaTag, repositoryName);
								createDropdownTagObjectRepository(repositoryFilePath, selectTag, optionTag,
										repositoryName);
								createATagObjectRepository(repositoryFilePath, aTag, repositoryName);
								createTableTagObjectRepository(repositoryFilePath, tableTags, repositoryName);
								createImageTagObjectRepository(repositoryFilePath, imageTags, repositoryName);
							} catch (EncryptedDocumentException | IOException exec) {
							}
							JOptionPane.showMessageDialog((JButton) e.getSource(),
									"Locators are captured and stored successfully");
						} else {
							JButton sourceButton = (JButton) e.getSource();
							JOptionPane.showMessageDialog(sourceButton, "Please select at least one web component");
						}
					}
				}
			}

			/**
			 * Create object repository for selected table tags
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param imageTags
			 * @param repositoryName
			 */
			private void createImageTagObjectRepository(String repositoryFilePath, Elements imageTags,
					String repositoryName) {
				if (checkBoxImage.isSelected()) {
					LocatorsStorage.trackImgTagLocators(repositoryFilePath, imageTags, "ImageLocators");
					try {
						LocatorsPattern.createImgTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			/**
			 * Create object repository for selected table tags
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param tableTags
			 * @param repositoryName
			 * @throws EncryptedDocumentException
			 * @throws IOException
			 */
			private void createTableTagObjectRepository(String repositoryFilePath, Elements tableTags,
					String repositoryName) throws EncryptedDocumentException, IOException {
				if (checkBoxTable.isSelected()) {
					LocatorsStorage.trackTableTagLocators(repositoryFilePath, tableTags, "TableLocators");
					try {
						LocatorsPattern.createTableTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			/**
			 * Create object repository for selected a tags
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param aTag
			 * @param repositoryName
			 * @throws EncryptedDocumentException
			 * @throws IOException
			 */
			private void createATagObjectRepository(String repositoryFilePath, Elements aTag, String repositoryName)
					throws EncryptedDocumentException, IOException {
				if (checkBoxLink.isSelected()) {
					LocatorsStorage.trackATagLocators(repositoryFilePath, aTag, "LinkLocators");
					try {
						LocatorsPattern.createATagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			/**
			 * Create object repository for selected select and option tags
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param selectTag
			 * @param optionTag
			 * @param repositoryName
			 */
			private void createDropdownTagObjectRepository(String repositoryFilePath, Elements selectTag,
					Elements optionTag, String repositoryName) {
				if (checkBoxDropdown.isSelected()) {
					LocatorsStorage.trackSelectTagLocators(repositoryFilePath, selectTag, "DropDownLocators");
					LocatorsStorage.trackOptionTagLocators(repositoryFilePath, optionTag, "DropDownOptionLocators");
					try {
						LocatorsPattern.createSelectTagXpathObjects(repositoryFilePath, repositoryName);
						LocatorsPattern.createOptionTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			/**
			 * Create object repository for selected text area tag
			 * 
			 * @author sanoj.swaminatha
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param textareaTag
			 * @param repositoryName
			 */
			private void createTextAreaTagObjectRepository(String repositoryFilePath, Elements textareaTag,
					String repositoryName) {
				if (checkBoxTextArea.isSelected()) {
					LocatorsStorage.trackTextareaTagLocators(repositoryFilePath, textareaTag, "TextareaLocators");
					try {
						LocatorsPattern.createTextAreaTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

			}

			/**
			 * Create object repository for selected label and heading tags
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param labelTags
			 * @param headingTags
			 * @param repositoryName
			 * @throws EncryptedDocumentException
			 * @throws IOException
			 */
			private void createLabelTagObjectRepository(String repositoryFilePath, Elements labelTags,
					Elements headingTags, String repositoryName) throws EncryptedDocumentException, IOException {
				if (checkBoxLabel.isSelected()) {
					LocatorsStorage.trackLabelTagLocators(repositoryFilePath, labelTags, "LabelLocators");
					LocatorsStorage.trackHeadingTagLocators(repositoryFilePath, headingTags, "HeadingLocators");
					try {
						LocatorsPattern.createLabelTagXpathObjects(repositoryFilePath, repositoryName);
						LocatorsPattern.createHeadingTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			/**
			 * Create object repository for selected button tag
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param buttonTag
			 * @param repositoryName
			 */
			private void createButtonTagObjectRepository(String repositoryFilePath, Elements buttonTag,
					String repositoryName) {
				if (checkBoxButton.isSelected()) {
					LocatorsStorage.trackButtonTagLocators(repositoryFilePath, buttonTag, "ButtonLocators");
					try {
						LocatorsPattern.createButtonTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			/**
			 * Create object repository for selected input tag
			 * 
			 * @author sanoj.swaminathan
			 * @since 21-08-2023
			 * @param repositoryFilePath
			 * @param inputTag
			 * @param repositoryName
			 * @throws EncryptedDocumentException
			 * @throws IOException
			 */
			private void createInputTagObjectRepository(String repositoryFilePath, Elements inputTag,
					String repositoryName) throws EncryptedDocumentException, IOException {
				if (checkBoxTextField.isSelected()) {
					LocatorsStorage.trackObjectLocators(repositoryFilePath, "" + inputTag, "InputLocators", "input");
					try {
						LocatorsPattern.createInputTagXpathObjects(repositoryFilePath, repositoryName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	// Entry point
	public static void main(String[] args) {
		scanObjectLocators();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}
