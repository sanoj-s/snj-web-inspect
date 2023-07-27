package com.test.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class VaraibleName {
	public static void main(String[] args) {
		try {
			// Load the web page using Jsoup
			Document document = Jsoup.connect("https://google.com").get();

			// Create a Properties object to store the DOM object values
			Properties properties = new Properties();

			// Traverse the DOM and store the values with meaningful names
			traverseDOM(document, properties, "");

			// Save the properties to a file
			savePropertiesToFile(properties, "D://dom.properties");

			System.out.println("DOM object values stored in the properties file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void traverseDOM(Element element, Properties properties, String prefix) {
		// Generate a unique name for the current element
		String name = prefix + element.tagName() + "_" + element.elementSiblingIndex();

		// Check if the element is an input field (assuming it has the 'name' attribute)
		if (element.tagName().equals("input") && element.hasAttr("name")) {
			String fieldName = element.attr("name");
			String fieldValue = element.val();

			// Store the field value with the field name as the property name
			properties.setProperty(fieldName, fieldValue);
		}

		// Recursively traverse child elements
		for (Element child : element.children()) {
			traverseDOM(child, properties, name + "_");
		}
	}

	private static void savePropertiesToFile(Properties properties, String filename) throws IOException {
		try (OutputStream output = new FileOutputStream(filename)) {
			properties.store(output, "DOM Object Properties");
		}
	}
}
