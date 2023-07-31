package com.snj.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Utilities {
	/**
	 * Method to create a copy of repository template and store to the Repositories
	 * folder inside src/test/resources
	 * 
	 * @author Sanoj Swaminathan
	 * @since 12-07-2023
	 * @param repositoryName
	 * @return
	 */
	public static String copyRepositoryTemplate(String repositoryName) {
		Path destinationPath = null;
		try {
			Path sourcePath = Path.of("src/main/resources/Template/Template.xlsx");
			byte[] fileContent = Files.readAllBytes(sourcePath);

			File resourcesDirectory = new File("src/test/resources" + File.separator + "Repositories");
			if (!resourcesDirectory.exists()) {
				resourcesDirectory.mkdirs();
			}
			destinationPath = Path.of("src/test/resources/Repositories/" + repositoryName + ".xlsx");
			Files.createDirectories(destinationPath.getParent());
			Files.createFile(destinationPath);
			Files.write(destinationPath, fileContent);

			System.out.println("Repository file created at " + destinationPath);

		} catch (IOException e) {
		}
		return destinationPath.toString();
	}

	/**
	 * Method to create the java class for object repository
	 * 
	 * @author Sanoj Swaminathan
	 * @since 12-07-2023
	 * @param repoSitoryName
	 * @param objectName
	 * @param objectValue
	 */
	public static void createJavaFile(String repoSitoryName, String objectName, String objectValue) {
		String packageName = "com.demo.objects";
		String className = repoSitoryName;

		// Creating the com.demo.objects package inside src/test/java
		String rootDirectory = System.getProperty("user.dir") + "/src/test/java";
		String packagePath = rootDirectory + "/com/demo/objects";
		File packageDirectory = new File(packagePath);
		if (!packageDirectory.exists()) {
			packageDirectory.mkdirs();
		}

		String directory = "src/test/java/" + packageName.replace(".", "/") + "/";
		String fileName = directory + className + ".java";

		// Accept user input for variables
		Map<String, String> variables = new HashMap<>();
		variables.put(objectName, objectValue);

		// Check if the file already exists
		Path filePath = Path.of(fileName);
		StringBuilder code = new StringBuilder();
		if (Files.exists(filePath)) {

			// Read the existing code from the Java file
			try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
				String line;
				while ((line = reader.readLine()) != null) {
					code.append(line).append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Check if the last curly brace exists in the code
			int lastCurlyBraceIndex = code.lastIndexOf("}");

			// Update or append the variables in the code
			StringBuilder variablesCode = new StringBuilder();
			for (Map.Entry<String, String> entry : variables.entrySet()) {
				String variableName = entry.getKey();
				String variableValue = entry.getValue();

				// Check if the variable already exists in the code
				boolean variableExists = code.toString().contains("public static final String " + variableName);

				if (variableExists) {
					// Truncate the existing variable and update it with the new value
					code = new StringBuilder(
							code.toString().replaceAll("(public static final String " + variableName + " = \").*(\";)",
									"$1" + variableValue + "\";"));
				}

				if (!variableExists) {
					// Append the new variable to the variables code
					variablesCode.append("\n    public static final String ").append(variableName).append(" = \"")
							.append(variableValue).append("\";");

					// Newly Added here
					// Insert the variables code inside the last curly brace
					if (lastCurlyBraceIndex != -1) {
						code.insert(lastCurlyBraceIndex, variablesCode);
					}
				}
			}
		} else {
			// Generate code for the class and variables
			code.append("package ").append(packageName).append(";\n\n");
			code.append("public class ").append(className).append(" {\n");

			for (Map.Entry<String, String> entry : variables.entrySet()) {
				String variableName = entry.getKey();
				String variableValue = entry.getValue();
				code.append("    public static final String ").append(variableName).append(" = \"")
						.append(variableValue).append("\";\n");
			}
			code.append("}\n");
		}
		// Write the code to the Java file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
			writer.write(code.toString());
			System.out.println("Object repository " + repoSitoryName + " updated");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
//		copyRepositoryTemplate("SearchPage");
		createJavaFile("NewClass", "btn_new", "//login");
	}
}
