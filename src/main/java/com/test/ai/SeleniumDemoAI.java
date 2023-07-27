package com.test.ai;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

public class SeleniumDemoAI {

	@Test
	public void verifyTitle() throws Exception {
		WebDriver driver;
		driver = new ChromeDriver();
		driver.get(
				"file:///C:/Users/sanojs/Desktop/Student_Registration_Page.html");
		driver.manage().window().maximize();

		/*
		 * Split all the input, button, textarea, select tags from the present loaded
		 * webpage and store in Excel sheet
		 */
		WebElement element1 = driver.findElement(By.xpath("//*"));
		Document doc1 = Jsoup.parse(element1.getAttribute("innerHTML"));
		Elements inputTag = doc1.getElementsByTag("input");
		Elements buttonTag = doc1.getElementsByTag("button");
		Elements textareaTag = doc1.getElementsByTag("textarea");
		Elements optionTag = doc1.getElementsByTag("select");
		LocatorsStorageOld.inputLocator("" + inputTag, "InputLocators", "input");
		LocatorsStorageOld.inputLocator("" + buttonTag, "ButtonLocators", "button");
		LocatorsStorageOld.inputLocator("" + textareaTag, "TextareaLocators", "textarea");
		LocatorsStorageOld.inputLocator("" + optionTag, "DropDownLocators", "select");

		/*
		 * LocatorsPattern.inputTagXpath("variableName") - pull present updated locators
		 * from excel and pass it to the selenium script during execution.
		 */
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_FirstName"))).sendKeys("Senthil");
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_MiddleName"))).sendKeys("Kumar");
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_LastName"))).sendKeys("Paul Suyambu");
		new Select(driver.findElement(By.xpath(LocatorsPatternOld.optionTagXpath("DropDown_Course"))))
				.selectByVisibleText("BBA");
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_Other"))).click();
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_Phone_new"))).click();
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_Phone_new"))).sendKeys("0987654321");
		driver.findElement(By.xpath(LocatorsPatternOld.textAreaXpath("TextArea_Address"))).sendKeys("Chennai");
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_Email"))).sendKeys("senthil@kumar.com");
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_Password"))).sendKeys("password8765");
		driver.findElement(By.xpath(LocatorsPatternOld.inputTagXpath("Input_ReTypePassword"))).sendKeys("password8765");
//	    driver.findElement(By.xpath(LocatorsPattern.buttonXpath("Button_Submit"))).click();

		// driver.quit();
	}

}
