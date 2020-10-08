package com.qa.pages;

import com.qa.BaseTest;
import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductsPage extends MenuPage{
	TestUtils testUtils = new TestUtils();
	
	@AndroidFindBy(xpath = "//android.widget.ScrollView[@content-desc=\"test-PRODUCTS\"]/preceding-sibling::android.view.ViewGroup/android.widget.TextView")
	@iOSXCUITFindBy(xpath="//XCUIElementTypeOther[@name=\"test-Toggle\"]/parent::*[1]/preceding-sibling::*[1]")
	private MobileElement productTitleText;
	
	@AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
	@iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"test-Item title\"])[1]")
	private MobileElement slpTitle;
	
	@AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
	@iOSXCUITFindBy (xpath = "(//XCUIElementTypeStaticText[@name=\"test-Price\"])[1]")
	private MobileElement slpPrice;
	
	public String getTitle() {
		return getText(productTitleText,"PRoduct Title text  -");
	}
	
	public String getSLPTitle() {
		String title = getText(slpTitle,"Product text - ");
		return title;
	}
	
	public String getSLPPrice() {
		String price = getText(slpPrice,"Profuct price - " );
		return price;
	}
	
	public ProductDetailsPage pressSLPTitle() {
		click(slpTitle, "Pressed SLP title ");
		return new ProductDetailsPage();
	}

}
