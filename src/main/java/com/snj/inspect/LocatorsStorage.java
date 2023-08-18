package com.snj.inspect;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LocatorsStorage {

	/**
	 * Method to track locators and mapped to the object repository excel sheets
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param htmlinput
	 * @param sheetName
	 * @param tagName
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public static void trackObjectLocators(String repositoryFilePath, String htmlinput, String sheetName,
			String tagName) throws EncryptedDocumentException, IOException {

		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

		String htmlsplit[] = htmlinput.split("<" + tagName + " ");
		for (int i = 0; i < htmlsplit.length; i++) {
			String htmlsplit2[] = htmlsplit[i].split("\" ");

			for (int j = 1; j < htmlsplit2.length; j++) {

				if (j == 1) {
					data.put("" + (i + 1),
							new Object[] { ("" + htmlsplit2[0].replace(">", "") + "\"").replace("\"", "'"),
									("" + htmlsplit2[1].replace(">", "") + "\"").replace("\"", "'"), "", "", "", "",
									"" });
				}
				if (j == 2) {
					data.put("" + (i + 1),
							new Object[] { ("" + htmlsplit2[0].replace(">", "") + "\"").replace("\"", "'"),
									("" + htmlsplit2[1].replace(">", "") + "\"").replace("\"", "'"),
									("" + htmlsplit2[2].replace(">", "") + "\"").replace("\"", "'"), "", "", "", "" });
				}
				if (j == 3) {
					data.put("" + (i + 1),
							new Object[] { ("" + htmlsplit2[0].replace(">", "") + "\"").replace("\"", "'"),
									("" + htmlsplit2[1].replace(">", "") + "\"").replace("\"", "'"),
									("" + htmlsplit2[2].replace(">", "") + "\"").replace("\"", "'"),
									("" + htmlsplit2[3].replace(">", "") + "\"").replace("\"", "'"), "", "", "" });
				}
			}
		}

		// Iterate over data and write to Excel repository
		writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
	}

	/**
	 * Method to track 'select' tag locators and mapped to the object repository
	 * 'DropDownLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-08-2023
	 * @param repositoryFilePath
	 * @param selectTagValues
	 * @param sheetName
	 */
	public static void trackSelectTagLocators(String repositoryFilePath, Elements selectTagValues, String sheetName) {
		try {
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

			// Write value to the first, second of the sheet based on the name and id
			// attributes availability
			int i = 0;
			for (Element selectTag : selectTagValues) {
				String name = selectTag.attr("name");
				String id = selectTag.attr("id");
				String className = selectTag.attr("class");
				if (!name.equals("") || !id.equals("") || !className.equals("")) {
					if (!name.equals("")) {
						data.put("" + (i + 1), new Object[] { "name='" + name + "'", "", "", "", "", "", "" });
					} else if (!id.equals("")) {
						data.put("" + (i + 1), new Object[] { "", "id='" + id + "'", "", "", "", "", "" });
					} else if (!className.equals("")) {
						data.put("" + (i + 1), new Object[] { "", "", "class='" + className + "'", "", "", "", "" });
					} else if (!name.equals("") && !className.equals("") && id.equals("")) {
						data.put("" + (i + 1), new Object[] { "name='" + name + "'", "", "class='" + className + "'",
								"", "", "", "" });
					} else if (name.equals("") && !className.equals("") && id.equals("")) {
						data.put("" + (i + 1),
								new Object[] { "", "id='" + id + "'", "class='" + className + "'", "", "", "", "" });
					} else if (!name.equals("") && className.equals("") && id.equals("")) {
						data.put("" + (i + 1),
								new Object[] { "name='" + name + "'", "id='" + id + "'", "", "", "", "", "" });
					} else {
						data.put("" + (i + 1), new Object[] { "name='" + name + "'", "id='" + id + "'",
								"class='" + className + "'", "", "", "", "" });
					}
					i++;
				}
			}
			// Iterate over data and write to Excel repository
			writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to track 'option' tag locators and mapped to the object repository
	 * 'DropDownOptionLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-08-2023
	 * @param repositoryFilePath
	 * @param optionTags
	 * @param sheetName
	 */
	public static void trackOptionTagLocators(String repositoryFilePath, Elements optionTags, String sheetName) {
		try {
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

			// Write value to the first, second of the sheet based on the text and value
			// attributes availability
			int i = 0;
			for (Element optionTag : optionTags) {
				String value = optionTag.attr("value");
				String text = optionTag.text();
				if (!value.equals("") || !text.equals("")) {
					if (!value.equals("")) {
						data.put("" + (i + 1), new Object[] { "value='" + value + "'", "", "", "", "", "", "" });
					} else if (!text.equals("")) {
						data.put("" + (i + 1), new Object[] { "", text, "", "", "", "", "" });
					} else {
						data.put("" + (i + 1), new Object[] { "value='" + value + "'", text, "", "", "", "", "" });
					}
					i++;
				}
			}
			// Iterate over data and write to Excel repository
			writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to track 'button' tag locators and mapped to the object repository
	 * 'ButtonLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-08-2023
	 * @param repositoryFilePath
	 * @param buttonTagValues
	 * @param sheetName
	 */
	public static void trackButtonTagLocators(String repositoryFilePath, Elements buttonTagValues, String sheetName) {
		try {
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

			// Write value to the first, second, third and fourth columns of the sheet based
			// on the button text, button name, button value, button class attributes
			// availability
			int i = 0;
			for (Element buttonTag : buttonTagValues) {
				String buttonText = buttonTag.text();
				String buttonName = buttonTag.attr("name");
				String buttonValue = buttonTag.attr("value");
				String buttonClass = buttonTag.attr("class");
				if (!buttonText.equals("") || !buttonName.equals("") || !buttonValue.equals("")
						|| !buttonClass.equals("")) {
					if (!buttonName.equals("")) {
						data.put("" + (i + 1),
								new Object[] { buttonTag.text(), "name='" + buttonName + "'", "", "", "", "", "" });
					} else if (!buttonValue.equals("") && buttonName.equals("")) {
						data.put("" + (i + 1),
								new Object[] { buttonTag.text(), "", "value='" + buttonValue + "'", "", "", "", "" });
					} else if (!buttonClass.equals("") && buttonName.equals("")) {
						data.put("" + (i + 1),
								new Object[] { buttonTag.text(), "", "", "class='" + buttonClass + "'", "", "", "" });
					} else if (!buttonClass.equals("") && !buttonName.equals("") && buttonValue.equals("")) {
						data.put("" + (i + 1), new Object[] { buttonTag.text(), "name='" + buttonName + "'", "",
								"class='" + buttonClass + "'", "", "", "" });
					} else if (!buttonClass.equals("") && buttonName.equals("") && !buttonValue.equals("")) {
						data.put("" + (i + 1), new Object[] { buttonTag.text(), "", "value='" + buttonValue + "'",
								"class='" + buttonClass + "'", "", "", "" });
					} else if (!buttonValue.equals("") && !buttonName.equals("")) {
						data.put("" + (i + 1), new Object[] { buttonTag.text(), "name='" + buttonName + "'",
								"value='" + buttonValue + "'", "", "", "", "" });
					} else if (!buttonValue.equals("") && !buttonName.equals("") && !buttonClass.equals("")) {
						data.put("" + (i + 1), new Object[] { buttonTag.text(), "name='" + buttonName + "'",
								"value='" + buttonValue + "'", "class='" + buttonClass + "'", "", "", "" });
					} else {
						data.put("" + (i + 1), new Object[] { buttonTag.text(), "", "", "", "", "", "" });
					}
					i++;
				}
			}
			// Iterate over data and write to Excel repository
			writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to track 'textarea' tag locators and mapped to the object repository
	 * 'TextareaLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 18-08-2023
	 * @param repositoryFilePath
	 * @param textareaTags
	 * @param sheetName
	 */
	public static void trackTextareaTagLocators(String repositoryFilePath, Elements textareaTags, String sheetName) {

		try {
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

			// Write value to the first, second, third and fourth columns of the sheet based
			// on the textarea text, textarea name, textarea value, textarea placeholder
			// attributes
			// availability
			int i = 0;
			for (Element textareaTag : textareaTags) {
				String textareaText = textareaTag.text();
				String textareaName = textareaTag.attr("name");
				String textareaPlaceholder = textareaTag.attr("placeholder");
				String textareaValue = textareaTag.attr("value");

				if (!textareaText.equals("") || !textareaName.equals("") || !textareaPlaceholder.equals("")
						|| !textareaValue.equals("")) {
					if (!textareaName.equals("")) {
						data.put("" + (i + 1),
								new Object[] { textareaTag.text(), "name='" + textareaName + "'", "", "", "", "", "" });
					} else if (!textareaPlaceholder.equals("") && !textareaName.equals("")
							&& textareaValue.equals("")) {
						data.put("" + (i + 1), new Object[] { textareaTag.text(), "name='" + textareaName + "'", "",
								"placeholder='" + textareaPlaceholder + "'", "", "", "" });
					} else if (!textareaPlaceholder.equals("") && textareaName.equals("")
							&& !textareaValue.equals("")) {
						data.put("" + (i + 1), new Object[] { textareaTag.text(), "", "value='" + textareaValue + "'",
								"placeholder='" + textareaPlaceholder + "'", "", "", "" });
					} else if (!textareaPlaceholder.equals("") && textareaName.equals("")) {
						data.put("" + (i + 1), new Object[] { textareaTag.text(), "", "",
								"placeholder='" + textareaPlaceholder + "'", "", "", "" });
					} else if (!textareaValue.equals("") && !textareaName.equals("")) {
						data.put("" + (i + 1), new Object[] { textareaTag.text(), "name='" + textareaName + "'",
								"value='" + textareaValue + "'", "", "", "", "" });
					} else if (!textareaValue.equals("") && textareaName.equals("")) {
						data.put("" + (i + 1), new Object[] { textareaTag.text(), "", "value='" + textareaValue + "'",
								"", "", "", "" });
					} else if (!textareaValue.equals("") && !textareaName.equals("")
							&& !textareaPlaceholder.equals("")) {
						data.put("" + (i + 1),
								new Object[] { textareaTag.text(), "name='" + textareaName + "'",
										"value='" + textareaValue + "'", "placeholder='" + textareaPlaceholder + "'",
										"", "", "" });
					} else {
						data.put("" + (i + 1), new Object[] { textareaTag.text(), "", "", "", "", "", "" });
					}
					i++;
				}
			}
			// Iterate over data and write to Excel repository
			writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to track 'a' tag locators and mapped to the object repository
	 * 'LinkLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param aTagValues
	 * @param sheetName
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public static void trackATagLocators(String repositoryFilePath, Elements aTagValues, String sheetName)
			throws EncryptedDocumentException, IOException {

		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

		// Write value to the first, second and third columns of the sheet based on the
		// aria-label and title attributes availability
		int i = 0;
		for (Element linkTag : aTagValues) {
			if (!linkTag.text().equals("") || !linkTag.attr("aria-label").equals("")
					|| !linkTag.attr("title").equals("")) {
				String ariaLabel = linkTag.attr("aria-label");
				String titleValue = linkTag.attr("title");
				if (!ariaLabel.equals("")) {
					data.put("" + (i + 1),
							new Object[] { linkTag.text(), "aria-label='" + ariaLabel + "'", "", "", "", "", "" });
				} else if (!titleValue.equals("") && ariaLabel.equals("")) {
					data.put("" + (i + 1),
							new Object[] { linkTag.text(), "title='" + titleValue + "'", "", "", "", "", "" });
				} else if (!titleValue.equals("") && !ariaLabel.equals("")) {
					data.put("" + (i + 1), new Object[] { linkTag.text(), "aria-label='" + ariaLabel + "'",
							"title='" + titleValue + "'", "", "", "", "" });
				} else {
					data.put("" + (i + 1), new Object[] { linkTag.text(), "", "", "", "", "", "" });
				}
				i++;
			}
		}
		// Iterate over data and write to Excel repository
		writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
	}

	/**
	 * Method to track 'label' tag locators and mapped to the object repository
	 * 'LabelLocators' excel sheet
	 * 
	 * @param repositoryFilePath
	 * @param labelTagValues
	 * @param sheetName
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public static void trackLabelTagLocators(String repositoryFilePath, Elements labelTagValues, String sheetName)
			throws EncryptedDocumentException, IOException {

		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

		// Write value to the first and second columns of the sheet based on the
		// for attribute availability
		int i = 0;
		for (Element labelTag : labelTagValues) {
			if (!labelTag.text().equals("") || !labelTag.attr("for").equals("")) {
				String forLableValue = labelTag.attr("for");
				if (!forLableValue.equals("")) {
					data.put("" + (i + 1),
							new Object[] { labelTag.text(), "for='" + forLableValue + "'", "", "", "", "", "" });
				} else {
					data.put("" + (i + 1), new Object[] { labelTag.text(), "", "", "", "", "", "" });
				}
				i++;
			}
		}
		// Iterate over data and write to Excel repository
		writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
	}

	/**
	 * Method to track 'table' tag locators and mapped to the object repository
	 * 'TableLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-07-2023
	 * @param repositoryFilePath
	 * @param tableTags
	 * @param sheetName
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public static void trackTableTagLocators(String repositoryFilePath, Elements tableTags, String sheetName)
			throws EncryptedDocumentException, IOException {

		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("0", new Object[] { "Class", "Heading", "Data", "Loc4", "Loc5", "Loc6", "Loc7" });

		// Write the table class name to the first column, cell data to the second
		// column and table heading to the
		// third column
		int i = 0, j = 0;
		for (Element table : tableTags) {
			String tableClass = table.attr("class");
			Elements heading = table.getElementsByTag("th");
			Elements rows = table.getElementsByTag("tr");
			for (Element row : rows) {
				Elements cells = row.getElementsByTag("td");
				for (Element cell : cells) {
					if (!cell.text().equals("") || !tableClass.equals("")) {
						if (tableClass.equals("") && !cell.text().equals("") && heading.size() > 0) {
							data.put("" + (i + 1),
									new Object[] { "", heading.get(j).text(), cell.text(), "", "", "", "" });
							i++;
							j++;
							if (cells.size() == j) {
								j = 0;
							}
						} else if (tableClass.equals("") && !cell.text().equals("") && heading.size() == 0) {
							data.put("" + (i + 1), new Object[] { "", "", cell.text(), "", "", "", "" });
							i++;
						} else if (!tableClass.equals("") && !cell.text().equals("") && heading.size() == 0) {
							data.put("" + (i + 1), new Object[] { tableClass, "", cell.text(), "", "", "", "" });
							i++;
						} else {
							data.put("" + (i + 1),
									new Object[] { tableClass, heading.get(j).text(), cell.text(), "", "", "", "" });
							i++;
							j++;
							if (cells.size() == j) {
								j = 0;
							}
						}
					}
				}
			}
		}
		// Iterate over data and write to Excel repository
		writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);
	}

	/**
	 * Method to track 'image' tag locators and mapped to the object repository
	 * 'ImageLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 31-07-2023
	 * @param repositoryFilePath
	 * @param imgTags
	 * @param sheetName
	 */
	public static void trackImgTagLocators(String repositoryFilePath, Elements imgTags, String sheetName) {
		try {
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

			// Write value to the first column of the sheet based on the for attribute
			// availability
			int i = 0;
			for (Element imgTag : imgTags) {
				if (!imgTag.attr("alt").equals("")) {
					String altAttributeValue = imgTag.attr("alt");
					data.put("" + (i + 1), new Object[] { altAttributeValue, "", "", "", "", "", "" });
					i++;
				}
			}
			// Iterate over data and write to Excel repository
			writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to track 'h1','h2','h3','h4' tag locators and mapped to the object
	 * repository 'HeadingLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 31-07-2023
	 * @param repositoryFilePath
	 * @param headingTags
	 * @param sheetName
	 */
	public static void trackHeadingTagLocators(String repositoryFilePath, Elements headingTags, String sheetName) {
		try {
			// This data needs to be written (Object[])
			Map<String, Object[]> data = new TreeMap<String, Object[]>();
			data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

			// Write value to the first column of the sheet
			int i = 0;
			for (Element headingTag : headingTags) {
				if (!headingTag.text().equals("")) {
					data.put("" + (i + 1), new Object[] { headingTag.text(), "", "", "", "", "", "" });
					i++;
				}
			}
			// Iterate over data and write to Excel repository
			writeObjectDataToExcelRepository(repositoryFilePath, sheetName, data);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Private method to write the object data into the Excel repositories
	 * 
	 * @author sanoj.swamination
	 * @since 28-07-2023
	 * @param repositoryFilePath
	 * @param sheetName
	 * @param data
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	private static void writeObjectDataToExcelRepository(String repositoryFilePath, String sheetName,
			Map<String, Object[]> data) throws EncryptedDocumentException, IOException {
		FileInputStream inputStream = new FileInputStream(new File(repositoryFilePath));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);

		for (Map.Entry<String, Object[]> entry : data.entrySet()) {
			String key = entry.getKey();
			Object[] value = entry.getValue();
			Row row = sheet.createRow(Integer.parseInt(key));
			for (int j = 0; j < value.length; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(value[j].toString());
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(repositoryFilePath).getAbsoluteFile());
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
