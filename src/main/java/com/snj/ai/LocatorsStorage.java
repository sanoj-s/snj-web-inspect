package com.snj.ai;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
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

		FileInputStream inputStream = new FileInputStream(new File(repositoryFilePath));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);

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

		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.getRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(repositoryFilePath).getAbsoluteFile());
			workbook.write(out);
			out.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method to track <a> tag locators and mapped to the object repository
	 * 'LinkLocators' excel sheet
	 * 
	 * @author sanoj.swaminathan
	 * @since 27-07-2023
	 * @param repositoryFilePath
	 * @param aTagValues
	 * @param sheetName
	 * @param tagName
	 * @throws EncryptedDocumentException
	 * @throws IOException
	 */
	public static void trackATagLocators(String repositoryFilePath, Elements aTagValues, String sheetName,
			String tagName) throws EncryptedDocumentException, IOException {

		FileInputStream inputStream = new FileInputStream(new File(repositoryFilePath));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheet(sheetName);

		// This data needs to be written (Object[])
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("0", new Object[] { "Loc1", "Loc2", "Loc3", "Loc4", "Loc5", "Loc6", "Loc7" });

		// Write value to the first, second and third columns of the sheet based on the
		// aria-label and title tags availability
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
		// Write data to the Excel sheet
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