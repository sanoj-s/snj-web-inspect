package com.snj.ai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.snj.utils.Utilities;

public class LocatorsPattern {

	private static FileInputStream fis;
	private static FileOutputStream fileOut;
	private static Workbook wb;
	private static Sheet sh;
	private static Cell cell;
	private static Row row;
	private static CellStyle cellstyle;
	private static Color mycolor;
	private static String excelFilePath;
	private static Map<String, Integer> columns = new HashMap<>();
	public static String seleniumLocator;

	/**
	 * Private method to set the excel file for object repository
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param ExcelPath
	 * @param SheetName
	 * @throws Exception
	 */
	private static void setExcelFile(String ExcelPath, String SheetName) throws Exception {
		try {
			File f = new File(ExcelPath);

			if (!f.exists()) {
				f.createNewFile();
				System.out.println("File doesn't exist, so created!");
			}

			fis = new FileInputStream(ExcelPath);
			wb = WorkbookFactory.create(fis);
			sh = wb.getSheet(SheetName);
			if (sh == null) {
				sh = wb.createSheet(SheetName);
			}

			excelFilePath = ExcelPath;

			// adding all the column header names to the map 'columns'
			sh.getRow(0).forEach(cell -> {
				columns.put(cell.getStringCellValue(), cell.getColumnIndex());
			});

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Private method to get the excel cell data with given column and row index
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param rownum
	 * @param colnum
	 * @return
	 * @throws Exception
	 */
	private static String getCellData(int rownum, int colnum) throws Exception {
		try {
			cell = sh.getRow(rownum).getCell(colnum);
			String CellData = null;
			switch (cell.getCellType()) {
			case STRING:
				CellData = cell.getStringCellValue();
				break;
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					CellData = String.valueOf(cell.getDateCellValue());
				} else {
					CellData = String.valueOf((long) cell.getNumericCellValue());
				}
				break;
			case BOOLEAN:
				CellData = Boolean.toString(cell.getBooleanCellValue());
				break;
			case BLANK:
				CellData = "";
				break;
			}
			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Private method to get the excel cell data with given column name and row
	 * index
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param columnName
	 * @param rownum
	 * @return
	 * @throws Exception
	 */
	private static String getCellData(String columnName, int rownum) throws Exception {
		return getCellData(rownum, columns.get(columnName));
	}

	/**
	 * Method to write the 'input' tag related locators into the object repository
	 * class
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createInputTagXpathObjects(String repositoryFilePath, String repositoryName) throws Exception {
		setExcelFile(repositoryFilePath, "InputLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {
			seleniumLocator = "//*[" + "@" + getCellData("Loc1", i) + " and @" + getCellData("Loc2", i) + " and @"
					+ getCellData("Loc3", i) + "]";

			if (!seleniumLocator.contains("type='hidden'")) {
				if (!getCellData("Loc1", i).equals("")) {
					for (int j = 1; j <= 4; j++) {
						// Attribute matching and get the object name
						if (getCellData("Loc" + j, i).startsWith("placeholder=")
								|| getCellData("Loc" + j, i).startsWith("name=")
								|| getCellData("Loc" + j, i).startsWith("value=")) {
							String[] parts = getCellData("Loc" + j, i).split("=");
							String extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
							String firstLetter = extractedText.substring(0, 1).toLowerCase();
							String restOfString = extractedText.substring(1);
							String finalText = firstLetter + restOfString;
							objectName = finalText;
							break;
						}
					}

					if (objectName == null) {
						objectName = "txt_objectName" + i;
					}
					Utilities.createJavaFile(repositoryName, objectName, seleniumLocator);
				}
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'textarea' tag related locators into the object
	 * repository class
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createTextAreaTagXpathObjects(String repositoryFilePath, String repositoryName)
			throws Exception {
		setExcelFile(repositoryFilePath, "TextareaLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {
			seleniumLocator = "//*[" + "@" + getCellData("Loc1", i) + "and @" + getCellData("Loc2", i) + "and @"
					+ getCellData("Loc3", i) + "]";
			if (!seleniumLocator.contains("type='hidden'")) {
				if (!getCellData("Loc1", i).equals("")) {
					for (int j = 1; j <= 4; j++) {
						// Attribute matching and get the object name
						if (getCellData("Loc" + j, i).startsWith("placeholder=")
								|| getCellData("Loc" + j, i).startsWith("name=")
								|| getCellData("Loc" + j, i).startsWith("value=")) {
							String[] parts = getCellData("Loc" + j, i).split("=");
							String extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
							String firstLetter = extractedText.substring(0, 1).toLowerCase();
							String restOfString = extractedText.substring(1);
							String finalText = firstLetter + restOfString;
							objectName = finalText;
							break;
						}
					}

					if (objectName == null) {
						objectName = "txa_objectName" + i;
					}
					Utilities.createJavaFile(repositoryName, "txa_" + objectName, seleniumLocator);
				}
			}
		}
		return seleniumLocator;

	}

	/**
	 * Method to write the 'select' tag related locators into the object repository
	 * class
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createSelectTagXpathObjects(String repositoryFilePath, String repositoryName)
			throws Exception {
		setExcelFile(repositoryFilePath, "DropDownLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {
			seleniumLocator = "//select[" + "@" + getCellData("Loc1", i) + "]";
			if (!seleniumLocator.contains("type='hidden'")) {
				if (!getCellData("Loc1", i).equals("")) {
					for (int j = 1; j <= 4; j++) {
						// Attribute matching and get the object name
						if (getCellData("Loc" + j, i).startsWith("placeholder=")
								|| getCellData("Loc" + j, i).startsWith("name=")
								|| getCellData("Loc" + j, i).startsWith("value=")) {
							String[] parts = getCellData("Loc" + j, i).split("=");
							String extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
							String firstLetter = extractedText.substring(0, 1).toLowerCase();
							String restOfString = extractedText.substring(1);
							String finalText = firstLetter + restOfString;
							objectName = finalText;
							break;
						}
					}

					if (objectName == null) {
						objectName = "ddl_objectName" + i;
					}
					Utilities.createJavaFile(repositoryName, "ddl_" + objectName, seleniumLocator);
				}
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'button' tag related locators into the object repository
	 * class
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createButtonTagXpathObjects(String repositoryFilePath, String repositoryName)
			throws Exception {
		setExcelFile(repositoryFilePath, "ButtonLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {
			seleniumLocator = "//*[" + "@" + getCellData("Loc1", i) + "]";
			if (!seleniumLocator.contains("type='hidden'")) {
				if (!getCellData("Loc1", i).equals("")) {
					for (int j = 1; j <= 4; j++) {
						// Attribute matching and get the object name
						if (getCellData("Loc" + j, i).startsWith("placeholder=")
								|| getCellData("Loc" + j, i).startsWith("name=")
								|| getCellData("Loc" + j, i).startsWith("value=")) {
							String[] parts = getCellData("Loc" + j, i).split("=");
							String extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
							String firstLetter = extractedText.substring(0, 1).toLowerCase();
							String restOfString = extractedText.substring(1);
							String finalText = firstLetter + restOfString;
							objectName = finalText;
							break;
						}
					}

					if (objectName == null) {
						objectName = "btn_objectName" + i;
					}
					Utilities.createJavaFile(repositoryName, "btn_" + objectName, seleniumLocator);
				}
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'a' tag related locators into the object repository class
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createATagXpathObjects(String repositoryFilePath, String repositoryName) throws Exception {
		setExcelFile(repositoryFilePath, "LinkLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {

			// Attribute matching and get the object and object name
			if (!getCellData("Loc1", i).equals("") || !getCellData("Loc2", i).equals("")
					|| !getCellData("Loc3", i).equals("")) {
				String extractedText;
				if (!getCellData("Loc1", i).equals("") && !getCellData("Loc2", i).equals("")
						&& getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//a[text()='" + getCellData("Loc1", i) + "' or @" + getCellData("Loc2", i) + "]";
					extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				} else if (!getCellData("Loc1", i).equals("") && getCellData("Loc2", i).equals("")
						&& getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//a[text()='" + getCellData("Loc1", i) + "']";
					extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				} else if (getCellData("Loc1", i).equals("") && !getCellData("Loc2", i).equals("")
						&& !getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//a[@" + getCellData("Loc2", i) + " or @" + getCellData("Loc3", i) + "]";
					String[] parts = getCellData("Loc2", i).split("=");
					extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
				} else if (getCellData("Loc1", i).equals("") && !getCellData("Loc2", i).equals("")
						&& getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//a[@" + getCellData("Loc2", i) + "]";
					String[] parts = getCellData("Loc2", i).split("=");
					extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
				} else if (getCellData("Loc1", i).equals("") && getCellData("Loc2", i).equals("")
						&& !getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//a[@" + getCellData("Loc3", i) + "]";
					String[] parts = getCellData("Loc3", i).split("=");
					extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
				} else {
					seleniumLocator = "//a[text()='" + getCellData("Loc1", i) + "' or @" + getCellData("Loc2", i)
							+ " or @" + getCellData("Loc3", i) + "]";
					extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				}

				String firstLetter = extractedText.substring(0, 1).toLowerCase();
				String restOfString = extractedText.substring(1);
				String finalText = firstLetter + restOfString;
				objectName = finalText;

				Utilities.createJavaFile(repositoryName, "lnk_" + objectName, seleniumLocator);
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'label' tag related locators into the object repository
	 * class
	 * 
	 * @author sanoj.swaminathan
	 * @since 28-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createLabelTagXpathObjects(String repositoryFilePath, String repositoryName) throws Exception {
		setExcelFile(repositoryFilePath, "LabelLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {

			// Attribute matching and get the object and object name
			if (!getCellData("Loc1", i).equals("") || !getCellData("Loc2", i).equals("")) {
				String extractedText;
				if (getCellData("Loc1", i).equals("") && !getCellData("Loc2", i).equals("")) {
					seleniumLocator = "//label[@" + getCellData("Loc2", i) + "]";
					String[] parts = getCellData("Loc2", i).split("=");
					extractedText = parts[1].replaceAll("[^a-zA-Z0-9]", "");
				} else if (!getCellData("Loc1", i).equals("") && getCellData("Loc2", i).equals("")) {
					seleniumLocator = "//label[text()='" + getCellData("Loc1", i) + "']";
					extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				} else {
					seleniumLocator = "//label[text()='" + getCellData("Loc1", i) + "' or @" + getCellData("Loc2", i)
							+ "]";
					extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				}

				String firstLetter = extractedText.substring(0, 1).toLowerCase();
				String restOfString = extractedText.substring(1);
				String finalText = firstLetter + restOfString;
				objectName = finalText;

				Utilities.createJavaFile(repositoryName, "lbl_" + objectName, seleniumLocator);
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'table' tag related locators into the object repository
	 * class
	 * 
	 * @author sanoj.swamianthan
	 * @since 28-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createTableTagXpathObjects(String repositoryFilePath, String repositoryName) throws Exception {

		setExcelFile(repositoryFilePath, "TableLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {

			// Attribute matching and get the object and object name
			if (!getCellData("Loc1", i).equals("") || !getCellData("Loc2", i).equals("")
					|| !getCellData("Loc3", i).equals("")) {
				String tableHeading, tableCellData, extractedText;

				if (getCellData("Loc1", i).equals("") && !getCellData("Loc2", i).equals("")
						&& !getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//table//tr//th[text()='" + getCellData("Loc2", i)
							+ "']//..//..//following-sibling::td[text()='" + getCellData("Loc3", i) + "']";
					tableHeading = getCellData("Loc2", i).replaceAll("[^a-zA-Z0-9]", "");
					tableCellData = getCellData("Loc3", i).replaceAll("[^a-zA-Z0-9]", "");
					extractedText = tableHeading + "_" + tableCellData;
				} else if (!getCellData("Loc1", i).equals("") && getCellData("Loc2", i).equals("")
						&& !getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//table[@class='" + getCellData("Loc1", i)
							+ "']//tr//..//..//following-sibling::td[text()='" + getCellData("Loc3", i) + "']";
					tableCellData = getCellData("Loc3", i).replaceAll("[^a-zA-Z0-9]", "");
					extractedText = tableCellData;
				} else if (getCellData("Loc1", i).equals("") && getCellData("Loc2", i).equals("")
						&& !getCellData("Loc3", i).equals("")) {
					seleniumLocator = "//table//tr//..//..//following-sibling::td[text()='" + getCellData("Loc3", i)
							+ "']";
					tableCellData = getCellData("Loc3", i).replaceAll("[^a-zA-Z0-9]", "");
					extractedText = tableCellData;
				} else {
					seleniumLocator = "//table[@class='" + getCellData("Loc1", i) + "']//tr//th[text()='"
							+ getCellData("Loc2", i) + "']//..//..//following-sibling::td[text()='"
							+ getCellData("Loc3", i) + "']";
					tableHeading = getCellData("Loc2", i).replaceAll("[^a-zA-Z0-9]", "");
					tableCellData = getCellData("Loc3", i).replaceAll("[^a-zA-Z0-9]", "");
					extractedText = tableHeading + "_" + tableCellData;
				}

				String firstLetter = extractedText.substring(0, 1).toLowerCase();
				String restOfString = extractedText.substring(1);
				String finalText = firstLetter + restOfString;
				objectName = finalText;

				Utilities.createJavaFile(repositoryName, "tbl_" + objectName, seleniumLocator);
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'img' tag related locators into the object repository
	 * class
	 * 
	 * @author sanoj.swaminathan
	 * @since 31-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @return
	 * @throws Exception
	 */
	public static String createImgTagXpathObjects(String repositoryFilePath, String repositoryName) throws Exception {
		setExcelFile(repositoryFilePath, "ImageLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {

			// Attribute matching and get the object and object name
			if (!getCellData("Loc1", i).equals("")) {
				String extractedText;
				seleniumLocator = "//img[@alt='" + getCellData("Loc1", i) + "' or contains(@alt,'"
						+ getCellData("Loc1", i).substring(0, 6) + "')]";
				extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				String firstLetter = extractedText.substring(0, 1).toLowerCase();
				String restOfString = extractedText.substring(1);
				String finalText = firstLetter + restOfString;
				objectName = finalText;

				Utilities.createJavaFile(repositoryName, "img_" + objectName, seleniumLocator);
			}
		}
		return seleniumLocator;
	}

	/**
	 * Method to write the 'h1','h2','h3','h4' tags related locators into the object
	 * repository class
	 * 
	 * @author sanoj.swamintan
	 * @since 31-07-2023
	 * @param repositoryFilePath
	 * @param repositoryName
	 * @throws Exception
	 */
	public static String createHeadingTagXpathObjects(String repositoryFilePath, String repositoryName)
			throws Exception {

		setExcelFile(repositoryFilePath, "HeadingLocators");
		String objectName = null;
		for (int i = 1; i <= 1000; i++) {

			// Attribute matching and get the object and object name
			if (!getCellData("Loc1", i).equals("")) {
				String extractedText;
				seleniumLocator = "//*[text()='" + getCellData("Loc1", i) + "' or contains(text(),'"
						+ getCellData("Loc1", i).substring(0, 5) + "')]";
				extractedText = getCellData("Loc1", i).replaceAll("[^a-zA-Z0-9]", "");
				String firstLetter = extractedText.substring(0, 1).toLowerCase();
				String restOfString = extractedText.substring(1);
				String finalText = firstLetter + restOfString;
				objectName = finalText;

				Utilities.createJavaFile(repositoryName, "lbl_" + objectName, seleniumLocator);
			}
		}
		return seleniumLocator;
	}
}
