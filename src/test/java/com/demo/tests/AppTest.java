package com.demo.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.demo.objects.StudentRegistraionForm;

public class AppTest {
	WebDriver driver;

	@BeforeClass
	public void setUp() {
		driver = new ChromeDriver();
		driver.get(
				"file:///C:\\Users\\sanojs\\eclipse-workspace-panippura\\WebAutomationAI\\src\\test\\resources\\Application\\Student_Registration_Page.html ");
	}

	@Test
	public void TC001_submitForm() {
		driver.findElement(By.xpath(StudentRegistraionForm.firstname)).sendKeys("Sanoj");
		driver.findElement(By.xpath(StudentRegistraionForm.lastname)).sendKeys("Swaminathan");
		new Select(driver.findElement(By.xpath(StudentRegistraionForm.course))).selectByVisibleText("BBA");

		System.out.println();
	}
}
