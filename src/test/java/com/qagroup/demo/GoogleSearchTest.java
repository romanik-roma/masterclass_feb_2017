package com.qagroup.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.Stories;

@Features("Google Search")
@Stories("Search")
public class GoogleSearchTest {
	private WebDriver driver;
	private String funnyCats = "FUNNY CATS";

	@BeforeClass
	public void setup() {
		openGoogle();
	}

	@Test(priority = 1)
	public void testGoogleSearch() {
		searchFor(funnyCats);
		List<String> results = getResults();

		shouldContain(results.get(0), funnyCats);
	}

	@Test(priority = 2)
	public void dummyTest() {
		doNothing();
		makeScreenshot();
	}

	@AfterClass(alwaysRun = true)
	public void tearDown() {
		if (driver != null)
			driver.quit();
	}

	@Step
	private void openGoogle() {
		System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("http://www.google.com.ua");
	}

	@Step
	private void searchFor(String keyword) {
		driver.findElement(By.id("lst-ib")).sendKeys(keyword);
		waitFewSeconds();
		driver.findElement(By.id("_fZl")).click();
		waitFewSeconds();
	}

	@Step
	@Attachment
	private List<String> getResults() {
		return driver.findElements(By.cssSelector(".r")).stream().map(WebElement::getText).collect(Collectors.toList());
	}

	@Step("String <{0}> should contain <{1}>")
	private void shouldContain(String baseString, String keyword) {
		Assert.assertTrue(baseString.toLowerCase().contains(keyword.toLowerCase()),
				"First found result does not contain searched word: " + keyword);
	}

	@Step
	private void doNothing() {

	}

	@Attachment("Screenshot")
	public byte[] makeScreenshot() {
		return TakesScreenshot.class.cast(driver).getScreenshotAs(OutputType.BYTES);
	}

	public void waitFewSeconds() {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
		}
	}
}
