package com.qa.tests;

import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsPage;
import com.qa.utils.TestUtils;

public class ProductTest extends BaseTest {
	TestUtils testUtils = new TestUtils();
	
	LoginPage loginPage;
	ProductsPage productsPage;
	JSONObject loginUsers;
	SettingsPage settingsPage;
	ProductDetailsPage productDetailsPage;


	@BeforeClass
	public void beforeClass() throws Exception {
		InputStream inputStream = null;
		try {
			String dataFileName = "data/loginUsers.json";
			inputStream = getClass().getClassLoader().getResourceAsStream(dataFileName);
			JSONTokener jsonTokener = new JSONTokener(inputStream);
			loginUsers = new JSONObject(jsonTokener);
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}finally {
			if(inputStream!=null) {
				inputStream.close();
			}
		}
		closeApp();
		launchApp();
	}

	@AfterClass
	public void afterClass() {
	}

	@BeforeMethod
	public void beforeMethod(Method m) {
		loginPage = new LoginPage();
		testUtils.log("\n" +  "****"  + "Starting Test ->  " + m.getName() + "   *****" +  "\n");

		productsPage = loginPage.login(loginUsers.getJSONObject("successfulLogin").getString("userName"), 
				loginUsers.getJSONObject("successfulLogin").getString("password"));
	}

	@AfterMethod
	public void afterMethod() {
		settingsPage = productsPage.pressSettingButton();
		loginPage = settingsPage.pressLogout();
	}
	/*@Test
	public void validateProductOnProductsPage() {
		testUtils.log("Inside test method");

		SoftAssert softAssert = new SoftAssert();

		String slpTitle = productsPage.getSLPTitle();
		softAssert.assertEquals(slpTitle, strings.get("product_page_slb_title"));
		String slpPrice = productsPage.getSLPPrice();
		softAssert.assertEquals(slpPrice, strings.get("product_page_slb_price"));

		softAssert.assertAll(); 

		testUtils.log("before coming out of test method");
	}*/

	@Test
	public void validateProductOnProductsDetailPage() {
		testUtils.log("Inside test method");

		SoftAssert softAssert = new SoftAssert();

		productDetailsPage = productsPage.pressSLPTitle();

		String slpTitle = productDetailsPage.getPDPTitle();
		softAssert.assertEquals(slpTitle, getString().get("product_details_page_slb_title"));
		
		//To scroll android
		//productDetailsPage.scrollToSLPPriceandGetPrice();

		String slpText = productDetailsPage.getPDPText();
		softAssert.assertEquals(slpText, getString().get("product_details_page_slb_desc"));

		//productDetailsPage.scrollToSLPPriceandGetPrice();

		productDetailsPage.scrollPage();
		softAssert.assertTrue(productDetailsPage.isAddToCartBtnDisplayed());


		//		String slpPrice = productDetailsPage.getPDPPrice();
		//		softAssert.assertEquals(slpPrice, strings.get("product_details_page_slb_price"));

		productsPage = productDetailsPage.pressBackToProducts();

		softAssert.assertAll(); 

		testUtils.log("before coming out of test method");
	}
}
