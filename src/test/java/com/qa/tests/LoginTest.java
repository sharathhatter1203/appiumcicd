package com.qa.tests;

import org.testng.annotations.Test;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class LoginTest extends BaseTest{
	
	TestUtils testUtils = new TestUtils();
	
	LoginPage loginPage;
	ProductsPage productsPage;
	
	JSONObject loginUsers;
	

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
		testUtils.log().info("\n" +  "****"  + "Starting Test ->  " + m.getName() + "   *****" +  "\n");
	}

	@AfterMethod
	public void afterMethod() {
	}

	@Test
	public void invalidUserName() {
		testUtils.log().info("Inside test method");
		loginPage.enterUserName(loginUsers.getJSONObject("invalidUserName").getString("userName"));
		loginPage.enterPassword(loginUsers.getJSONObject("invalidUserName").getString("password"));
		loginPage.pressLoginBtn();
		String actualText = loginPage.getErrorText();
		String expectedText = getString().get("err_invalid_username_or_password");
		Assert.assertEquals(actualText, expectedText);
		testUtils.log().info("before coming out of test method");
		}
	
	@Test
	public void invalidPassword() {
		testUtils.log().info("Inside test method");
		loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("userName"));
		loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
		loginPage.pressLoginBtn();
		String actualText = loginPage.getErrorText();
		String expectedText = getString().get("err_invalid_username_or_password");
		Assert.assertEquals(actualText, expectedText);
		testUtils.log().info("before coming out of test method");
		}

	@Test
	public void successfulLogin() {
		testUtils.log().info("Inside test method");
		loginPage.enterUserName(loginUsers.getJSONObject("successfulLogin").getString("userName"));
		loginPage.enterPassword(loginUsers.getJSONObject("successfulLogin").getString("password"));
		productsPage = loginPage.pressLoginBtn();
		String actualText = productsPage.getTitle();
		String expectedText = getString().get("product_title");
		Assert.assertEquals(actualText, expectedText);
		testUtils.log().info("before coming out of test method");
		}
}
