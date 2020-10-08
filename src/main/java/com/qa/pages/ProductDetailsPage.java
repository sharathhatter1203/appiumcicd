package com.qa.pages;

import com.qa.MenuPage;
import com.qa.utils.TestUtils;

import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidBy;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class ProductDetailsPage extends MenuPage{
	
	TestUtils testUtils = new TestUtils();
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
	@iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[1]")
	private MobileElement slpTitle;
	
	@AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
	@iOSXCUITFindBy (xpath = "//XCUIElementTypeOther[@name=\"test-Description\"]/child::XCUIElementTypeStaticText[2]")
	private MobileElement slpText;
	
	@AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
	@iOSXCUITFindBy (id = "test-BACK TO PRODUCTS")
	private MobileElement backToProductsButton;
	
	@AndroidFindBy(accessibility = "test-Price")
	private MobileElement slpPrice;
	
	@iOSXCUITFindBy (id = "test-ADD TO CART") private MobileElement addToCartBtn;
	
	
	public String getPDPTitle() {
		String title = getText(slpTitle,"Profuct title - ");
		return title;
	}
	
	public String getPDPText() {
		String text = getText(slpText, "Profuct text - ");
		return text;
	}
	
	//For android
	public String scrollToSLPPriceandGetPrice() {
		return getText(scrollToElement(),"Scroll to - ");
	}
	
	//for ios
	public void scrollPage() {
		iOSScrollToElement();
	}
	
	public Boolean isAddToCartBtnDisplayed() {
		return addToCartBtn.isDisplayed();
	}
	
	public String getPDPPrice() {
		String price = getText(slpPrice,"Profuct price - ");
		return price;
	}
	
	public ProductsPage pressBackToProducts() {
		click(backToProductsButton,"Click back button ");
		return new ProductsPage();
	}

}
