package com.snj.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

		System.out.println();
	}
}
