package com.build.qa.build.selenium.pageobjects.homepage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.build.qa.build.selenium.pageobjects.BasePage;

public class HomePage extends BasePage {
	
	private By buildThemeBody;
	private static final String CONFIG_FILE1 = "./conf/automation.properties";
	static Properties configuration=null;

	public HomePage(WebDriver driver, Wait<WebDriver> wait) throws InvalidPropertiesFormatException, IOException {
		super(driver, wait);
		buildThemeBody = By.cssSelector("body.build-theme");
		configuration = new Properties();
		FileInputStream input;
		input = new FileInputStream(new File(CONFIG_FILE1));
		configuration.loadFromXML(input);
		input.close();
	}
	
	public boolean onBuildTheme() { 
		return wait.until(ExpectedConditions.presenceOfElementLocated(buildThemeBody)) != null;
	}
	
	public static void Enter_Value_In_Text_Box(String element, String text)
	{
		Wait_For_Element(element, 20);	
		String element_locator = configuration.getProperty(element);
		if(element.contains("_id"))
		{
			driver.findElement(By.id(element_locator)).sendKeys(text);
		}
		if(element.contains("_xpath"))
		{
			driver.findElement(By.xpath(element_locator)).sendKeys(text);			
		}
	}
	public static void Click_Element(String element)
	{
		Wait_For_Element(element, 20);		
		String element_locator = configuration.getProperty(element);		
		if(element.contains("_id"))
		{
			driver.findElement(By.id(element_locator)).click();
		}
		if(element.contains("_xpath"))
		{
			driver.findElement(By.xpath(element_locator)).click();			
		}
	}
	
	public static void Wait_For_Element(String element, int time_to_wait)
	{
		WebDriverWait wait = new WebDriverWait(driver, time_to_wait);
		String element_locator = configuration.getProperty(element);
		
		if(element.contains("_id"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(element_locator)));
		}
		if(element.contains("_xpath"))
		{
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(element_locator)));		
		}
		
	}
	
	public static String Get_Text(String element)
	{
		Wait_For_Element(element, 20);
		String element_locator = configuration.getProperty(element);		
		if(element.contains("_id"))
		{
			return driver.findElement(By.id(element_locator)).getText();
		}
		else if(element.contains("_xpath"))
		{
			return driver.findElement(By.xpath(element_locator)).getText();			
		}
		else
		return "Element Not Found";	
	}
}
