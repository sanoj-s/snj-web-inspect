package com.snj.inspect;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

		// Create a JFrame and set its layout manager
		JFrame frame = new JFrame("Web Inspect");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);

		// Create two panels for each line
		JPanel panelURL = new JPanel();
		JPanel panelRepo = new JPanel();

		// Configure layout managers for each panel
		panelURL.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelRepo.setLayout(new FlowLayout(FlowLayout.LEFT));

		// Add the button and text field to the panel
		panelURL.add(labelURL);
		panelURL.add(textFieldURL);
		panelURL.add(buttonStart);

		panelRepo.add(labelRepository);
		panelRepo.add(textFieldRepository);
		panelRepo.add(buttonScan);

		frame.add(panelURL);
		frame.add(panelRepo);
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
							LocatorsStorage.trackLabelTagLocators(repositoryFilePath, labelTags, "LabelLocators");
							LocatorsStorage.trackHeadingTagLocators(repositoryFilePath, headingTags, "HeadingLocators");
							LocatorsStorage.trackButtonTagLocators(repositoryFilePath, buttonTag, "ButtonLocators");
							LocatorsStorage.trackTextareaTagLocators(repositoryFilePath, textareaTag,
									"TextareaLocators");
							LocatorsStorage.trackATagLocators(repositoryFilePath, aTag, "LinkLocators");
							LocatorsStorage.trackSelectTagLocators(repositoryFilePath, selectTag, "DropDownLocators");
							LocatorsStorage.trackOptionTagLocators(repositoryFilePath, optionTag,
									"DropDownOptionLocators");
							LocatorsStorage.trackImgTagLocators(repositoryFilePath, imageTags, "ImageLocators");
							LocatorsStorage.trackTableTagLocators(repositoryFilePath, tableTags, "TableLocators");

							LocatorsStorage.trackObjectLocators(repositoryFilePath, "" + inputTag, "InputLocators",
									"input");
						} catch (EncryptedDocumentException | IOException exec) {
							exec.printStackTrace();
						}
						try {
							// Creating Java Class and mapping the objects
							LocatorsPattern.createLabelTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createHeadingTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createButtonTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createTextAreaTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createATagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createSelectTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createOptionTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createImgTagXpathObjects(repositoryFilePath, repositoryName);
							LocatorsPattern.createTableTagXpathObjects(repositoryFilePath, repositoryName);

							LocatorsPattern.createInputTagXpathObjects(repositoryFilePath, repositoryName);
						} catch (Exception exec) {
						}
						JOptionPane.showMessageDialog((JButton) e.getSource(),
								"Locators are captured and stored successfully");
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
