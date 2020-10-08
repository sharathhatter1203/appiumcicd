package com.qa;

import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.FindsByAndroidUIAutomator;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServerHasNotBeenStartedLocallyException;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

public class BaseTest  {

	protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
	protected static ThreadLocal<Properties> props = new ThreadLocal<Properties>();

	protected static ThreadLocal<HashMap<String,String>> strings = new ThreadLocal<HashMap<String, String>>();

	TestUtils utils = new TestUtils();
	protected static ThreadLocal<String> platform = new ThreadLocal<String>();
	protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
	protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();
	private static AppiumDriverLocalService server;



	//page factory initialization 
	public BaseTest() {
		// TODO Auto-generated constructor stub
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
	}

	//Get and set for thread local objects
	//to get the driver
	public AppiumDriver getDriver() {
		return driver.get();
	}

	//to set the driver
	public void setDriver(AppiumDriver driver2) {
		driver.set(driver2);
	}

	//to get the properties
	public Properties getProps() {
		return props.get();
	}

	//to set the properties
	public void setProps(Properties  props2) {
		props.set(props2);
	}

	//to get the strings
	public HashMap<String, String> getString() {
		return strings.get();
	}

	//to set the strings
	public void setStrings(HashMap<String,String>  strings2) {
		strings.set(strings2);
	}

	//to get the platform
	public String getPlatform() {
		return platform.get();
	}

	//to set the platform
	public void setPlatform(String  platform2) {
		platform.set(platform2);
	}

	//to return date time	
	public String getDateTime() {
		return dateTime.get();
	}

	//to set the date time
	public void setDatetime(String  dateTime2) {
		dateTime.set(dateTime2);
	}

	//to return date time	
	public String getDeviceName() {
		return deviceName.get();
	}

	//to set the date time
	public void setDeviceName(String  deviceName2) {
		deviceName.set(deviceName2);
	}
	
	@BeforeSuite
	public void beforeSuite() throws AppiumServerHasNotBeenStartedLocallyException, Exception {
		ThreadContext.put("ROUTINGKEY", "ServerLogs");
		server  = getAppiumService();
		if(!checkIfAppiumServerIsRunnning(4723)) {
			server.start();
			server.clearOutPutStreams();
			utils.log().info("Appium server started");
		}else
			utils.log().info("Appium server is already running");
		
	}
	
	public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
	    boolean isAppiumServerRunning = false;
	    ServerSocket socket;
	    try {
	        socket = new ServerSocket(port);
	        socket.close();
	    } catch (IOException e) {
	    	System.out.println("1");
	        isAppiumServerRunning = true;
	    } finally {
	        socket = null;
	    }
	    return isAppiumServerRunning;
	}
	
	@AfterSuite
	public void afterSuite() {
		server.stop();
		utils.log().info("Appium server stopped");
	}
	
	
	public AppiumDriverLocalService getAppiumServerDefault() {
		return AppiumDriverLocalService.buildDefaultService();
	}
	
	public AppiumDriverLocalService getAppiumService() {
		System.out.println("Inside local service");
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH", "/usr/local/Cellar/maven/3.6.2/bin:/usr/local/Cellar/gradle/5.6.2/bin:/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/sbin:/Library/Apple/usr/bin:/Applications/apache-maven-3.6.3/bin:/Users/sharathrudramuniyappa/Library/Android/sdk/emulator:/Users/sharathrudramuniyappa/Library/Android/sdk/tools:/Users/sharathrudramuniyappa/Library/Android/sdk/tools:/Users/sharathrudramuniyappa/Library/Android/sdk/platform-tools:/Users/sharathrudramuniyappa/Library/Android/sdk/platform-tools/adb:/Users/sharathrudramuniyappa/Library/Android/sdk/build-tools:/Users/sharathrudramuniyappa/Documents/sdk/fluttersdk/flutter/bin" + System.getenv("PATH"));
		//environment.put("JAVA_HOME","/Library/Java/JavaVirtualMachines/jdk1.8.0_221.jdk/Contents/Home");
		environment.put("ANDROID_HOME", "/Users/sharathrudramuniyappa/Library/Android/sdk");
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File("/usr/local/bin/node"))
				.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js"))
				.usingPort(4723)
				.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
				.withEnvironment(environment)
				.withLogFile(new File("ServerLogs/server.log")));
	}


	@Parameters({"emulator","platformName","deviceName","udid","systemPort","chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
	@BeforeTest
	public void beforeTest(@Optional("androidOnly")String emulator, String platformName, String deviceName,String udid, 
			@Optional("androidOnly")String systemPort,@Optional("androidOnly")String chromeDriverPort, @Optional("iOSOnly")String wdaLocalPort,
			@Optional("iOSOnly")String webkitDebugProxyPort ) throws IOException {
		

		
		//platform = platformName;
		setPlatform(platformName);
		//dateTime = utils.getDateTime();
		setDatetime(utils.dateTime());
		InputStream inputStream = null;
		InputStream stringInputStream = null;
		Properties props =new Properties();
		setDeviceName(deviceName);
		AppiumDriver driver;
		
		String strFile = "logs" + File.separator + getPlatform() + "_" + getDeviceName(); 
		File logFile = new File(strFile);
		if(!logFile.exists()) {
			logFile.mkdirs();
		}
		ThreadContext.put("ROUTINGKEY", strFile); 
		utils.log().info("log path: " +strFile);
		try {

			props = new Properties();
			String xmlFilename = "strings/strings.xml";
			String propFileName = "config.properties";
			URL url;
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			props.load(inputStream);
			setProps(props);

			//to get the strings sml data
			stringInputStream = getClass().getClassLoader().getResourceAsStream(xmlFilename);

			setStrings(utils.parseStringXML(stringInputStream));

			DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
			desiredCapabilities.setCapability("platformName", platformName);
			desiredCapabilities.setCapability("deviceName", deviceName);
			desiredCapabilities.setCapability("udid", udid);
			url = new URL(props.getProperty("appiumURL"));

			switch(platformName) {
			case "Android":
				utils.log().info(props.getProperty("androidAutomationName"));
				desiredCapabilities.setCapability("automationName", props.getProperty("androidAutomationName"));	
				desiredCapabilities.setCapability("appPackage", props.getProperty("androidAppPackage"));
				desiredCapabilities.setCapability("appActivity", props.getProperty("androidAppActivity"));
				if(emulator.equalsIgnoreCase("true")){
					desiredCapabilities.setCapability("avd",deviceName );
				}
				desiredCapabilities.setCapability("systemPort", systemPort);
				desiredCapabilities.setCapability("chromeDriverPort", chromeDriverPort);
				String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();
				utils.log().info(androidAppUrl);
				desiredCapabilities.setCapability("app",androidAppUrl);
				
				driver = new AndroidDriver(url, desiredCapabilities);
				String sessionId = driver.getSessionId().toString();	
				break;
			case "iOS":
				desiredCapabilities.setCapability("automationName", props.getProperty("iOSAutomationName"));	
				String iOSAppUrl = getClass().getResource(props.getProperty("iOSAppLocation")).getFile();
				utils.log().info(iOSAppUrl);
				//desiredCapabilities.setCapability("app",iOSAppUrl);
				desiredCapabilities.setCapability("bundleId", props.getProperty("iOSBundleId"));
				desiredCapabilities.setCapability("wdaLocalPort", wdaLocalPort);
				desiredCapabilities.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);
				driver = new IOSDriver(url, desiredCapabilities);
				break;
			default:
				throw new Exception("Invlaid Platform " + platformName);
			}
			setDriver(driver);
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(inputStream!=null) {
				inputStream.close();
			}
			if(stringInputStream!=null) {
				stringInputStream.close();
			} 
		}
	}


	//Methof to clear the element firld
	public void clear(MobileElement e) {
		waitForVisibility(e);
		e.clear();
	}

	//Method fot eait for visibility
	public void waitForVisibility(MobileElement ele) {
		WebDriverWait wait = new WebDriverWait(getDriver(), TestUtils.WAIT);
		wait.until(ExpectedConditions.visibilityOf(ele));
	}

	//Method to click mobile element
	public void click(MobileElement ele) {
		waitForVisibility(ele);
		ele.click();
	}
	
	//method for click with message for report
	public void click(MobileElement e, String msg) {
		  waitForVisibility(e);
		  utils.log().info(msg);
		  ExtentReport.getTest().log(Status.INFO, msg);
		  e.click();
	  }

	//Method to sendkeys to mobile element
	public void sendKeys(MobileElement ele, String text) {
		waitForVisibility(ele);
		ele.sendKeys(text);
	}
	
	public void sendKeys(MobileElement e, String txt, String msg) {
		  waitForVisibility(e);
		  utils.log().info(msg);
		  ExtentReport.getTest().log(Status.INFO, msg);
		  e.sendKeys(txt);
	  }



	//Method to get attribute of mobile element
	public String getAttribute(MobileElement ele, String attribute) {
		waitForVisibility(ele);
		return ele.getAttribute(attribute);
	}

	//Method to get text based on platform
	public String getText(MobileElement e, String msg) {
		  String txt = null;
		  switch(getPlatform()) {
		  case "Android":
			  txt = getAttribute(e, "text");
			  break;
		  case "iOS":
			  txt = getAttribute(e, "label");
			  break;
		  }
		  utils.log().info(msg + txt);
		  ExtentReport.getTest().log(Status.INFO, msg);
		  return txt;
	  }




	//Screenshot method 
	public static void getScreenshot(String testName) throws IOException {
		System.out.println("Inside screenshot method");
		File srcFile= ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(srcFile, new File(System.getProperty("user.dir")+"/screenshots/"+testName+".png"));
	}

	//to close app
	public void closeApp() {
		getDriver().closeApp();
	}

	//launch app
	public void launchApp() {
		getDriver().launchApp();
	}


	//to scroll android
	public MobileElement scrollToElement() {	  
		return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector()" + ".description(\"test-Inventory item page\")).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));");
	}
	//When the page ha only one scrollable element no need to mention the parent scrollable element
	/*public MobileElement scrollToElement() {	  
		return (MobileElement) ((FindsByAndroidUIAutomator) driver).findElementByAndroidUIAutomator(
				"new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
						+ "new UiSelector().description(\"test-Price\"));");
	}*/

	//to scroll ios
	public void iOSScrollToElement() {
		//	RemoteWebElement element = (RemoteWebElement)driver.findElement(By.className("XCUIElementTypeScrollView "));
		RemoteWebElement element = (RemoteWebElement)getDriver().findElement(By.name("test-ADD TO CART"));
		String elementID = element.getId();
		HashMap<String, String> scrollObject = new HashMap<String, String>();
		scrollObject.put("element", elementID);
		//scrollObject.put("direction", "down");
		//scrollObject.put("predicateString", "label == 'ADD TO CART'");
		//		  scrollObject.put("name", "test-ADD TO CART");
		scrollObject.put("toVisible", "anyvalue");
		getDriver().executeScript("mobile:scroll", scrollObject);
	}


	@AfterTest
	public void afterTest() {
		getDriver().quit();
	}

}
