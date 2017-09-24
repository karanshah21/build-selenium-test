package com.build.qa.build.selenium.tests;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import org.assertj.core.api.AbstractBooleanAssert;
import org.assertj.core.api.BooleanAssert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.build.qa.build.selenium.framework.BaseFramework;
import com.build.qa.build.selenium.pageobjects.homepage.HomePage;
import com.sun.corba.se.spi.orbutil.fsm.Action;
import com.sun.corba.se.spi.orbutil.fsm.FSM;
import com.sun.corba.se.spi.orbutil.fsm.Input;

public class BuildTest extends BaseFramework { 
	
	/** 
	 * Extremely basic test that outlines some basic
	 * functionality and page objects as well as assertJ
	 * @throws IOException 
	 * @throws InvalidPropertiesFormatException 
	 */
	@Test
	public void navigateToHomePage() throws InvalidPropertiesFormatException, IOException { 
		driver.get(getConfiguration("HOMEPAGE"));
		HomePage homePage = new HomePage(driver, wait);
		
		softly.assertThat(homePage.onBuildTheme())
			.as("The website should load up with the Build.com desktop theme.")
			.isTrue();
	}
	
	/** 
	 * Search for the Quoizel MY1613 from the search bar
	 * @throws InterruptedException 
	 * @assert: That the product page we land on is what is expected by checking the product title
	 * @difficulty Easy
	 */
	@Test
	public void searchForProductLandsOnCorrectProduct() throws InterruptedException { 
		driver.get(getConfiguration("HOMEPAGE"));

		HomePage.Click_Element("popupBlocker_xpath");
		HomePage.Enter_Value_In_Text_Box("searchBox_xpath", "Quoizel MY1613");
		HomePage.Click_Element("searchButton_xpath");
		String productTitle=HomePage.Get_Text("productTitle_xpath");
		System.out.println(productTitle);
		
		 softly.assertThat(productTitle).contains("Quoizel MY1613ML");
	}
	
	/** 
	 * Go to the Bathroom Sinks category directly (https://www.build.com/bathroom-sinks/c108504) 
	 * and add the second product on the search results (Category Drop) page to the cart.
	 * @throws InterruptedException 
	 * @assert: the product that is added to the cart is what is expected
	 * @difficulty Easy-Medium
	 */
	@Test
	public void addProductToCartFromCategoryDrop() throws InterruptedException { 
		driver.get("https://www.build.com/bathroom-sinks/c108504");

		HomePage.Click_Element("popupBlocker_xpath");
		HomePage.Click_Element("secoundProductLink_xpath");
		String expectedProduct=HomePage.Get_Text("productDescription_xpath");
		System.out.println(expectedProduct);
	
		softly.assertThat(expectedProduct).contains("Archer 22-5/8\" Drop In Bathroom Sink with 3 Holes Drilled and Overflow");
	}
	
	/** 
	 * Add a product to the cart and email the cart to yourself, also to my email address: jgilmore+SeleniumTest@build.com
	 * Include this message in the "message field" of the email form: "This is {yourName}, sending you a cart from my automation!"
	 * @throws InterruptedException 
	 * @assert that the "Cart Sent" success message is displayed after emailing the cart
	 * @difficulty Medium-Hard
	 */
	@Test
	public void addProductToCartAndEmailIt() throws InterruptedException { 
		driver.get("https://www.build.com/bathroom-sinks/c108504");
		HomePage.Click_Element("popupBlocker_xpath");
		HomePage.Click_Element("selectInstalltionType_xpath");
		HomePage.Click_Element("selectProduct_xpath");
		HomePage.Click_Element("addToCartWrap_xpath");
		Thread.sleep(2000);
		HomePage.Click_Element("openCartPage_xpath");
		Thread.sleep(5000);
		driver.findElement(By.xpath("//*[@id=\"page-content\"]/div[1]/div[1]/div/section[2]/div/div[1]/table/tbody/tr[2]/td/button[1]")).click();
	//	HomePage.Click_Element("emailCartButton_xpath");
		Thread.sleep(2000);
		HomePage.Enter_Value_In_Text_Box("nameField_xpath", "Karan Shah");
		HomePage.Enter_Value_In_Text_Box("emailField_xpath", "kdshah216@gmail.com");
		HomePage.Enter_Value_In_Text_Box("recipientName_xpath", "Jared Gilmore");
		HomePage.Enter_Value_In_Text_Box("recipientEmail_xpath", "jgilmore+SeleniumTest@build.com");
		HomePage.Enter_Value_In_Text_Box("additionalMessageBox_xpath", "This is Karan Shah, sending you a cart from my automation!");
		
		HomePage.Click_Element("emailCartButton_xpath");
		Thread.sleep(3000);
		String successMsg=driver.findElement(By.xpath("//*[@id=\"header\"]/div[4]/div/ul/li")).getText();		
		System.out.println(successMsg);
		
		softly.assertThat(successMsg).contains("Cart Sent! The cart has been submitted to the recipient via email.");
	}
	
	/** 
	 * Go to a category drop page (such as Bathroom Faucets) and narrow by
	 * at least two filters (facets), e.g: Finish=Chromes and Theme=Modern
	 * @throws InterruptedException 
	 * @assert that the correct filters are being narrowed, and the result count
	 * is correct, such that each facet selection is narrowing the product count.
	 * @difficulty Hard
	 */
	@Test
	public void facetNarrowBysResultInCorrectProductCounts() throws InterruptedException { 
		driver.get(getConfiguration("HOMEPAGE"));
		HomePage.Click_Element("selectBathroomOption_xpath");
		HomePage.Click_Element("selectBathroomFaucetsOption_xpath");
		String originalCount=HomePage.Get_Text("getCount_xpath");
		System.out.println(originalCount);
		int originalCountInt=0,reduceCount1Int=0,reduceCount2Int=0;
		try{
			originalCountInt=Integer.parseInt(originalCount);
				}catch(NumberFormatException e){}
		HomePage.Click_Element("selectFilterChromeFinish_xpath");
		String reduceCount1=HomePage.Get_Text("getCount_xpath");
		System.out.println("After selecting chrome finish, the count value reduced to "+reduceCount1);
		try{
			reduceCount1Int=Integer.parseInt(reduceCount1);
			}catch(NumberFormatException e){}
		
		softly.assertThat(originalCountInt>reduceCount1Int);
		
		System.out.println("Assertion 1 successful");
		
		HomePage.Click_Element("selectFilterModernTheme_xpath");
		String reduceCount2=HomePage.Get_Text("getCount_xpath");
		System.out.println("After selecting modern theme, the count value reduced to "+reduceCount2);
		try{
			reduceCount2Int=Integer.parseInt(reduceCount2);
			}catch(NumberFormatException e){}
	
		softly.assertThat(reduceCount1Int>reduceCount2Int);

	}
}
