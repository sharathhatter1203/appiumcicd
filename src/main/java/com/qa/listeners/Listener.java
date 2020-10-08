package com.qa.listeners;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.qa.BaseTest;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;

public class Listener implements ITestListener{
	
	TestUtils testUtils = new TestUtils();

	
	public void onFinish(ITestContext arg0) {
		//This method writes all info to report
		ExtentReport.getReporter().flush();

	}

	public void onStart(ITestContext arg0) {
		// TODO Auto-generated method stub

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub

	}


	/*public void onTestFailure(ITestResult arg0) {
		String testName = arg0.getName();
		System.out.println(testName);
		if(arg0.getThrowable() !=null) {
			try {
				BaseTest.getScreenshot(testName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			arg0.getThrowable().printStackTrace(pw);
			System.out.println(sw.toString());
		}
	}*/

	public void onTestFailure(ITestResult result) {
		if(result.getThrowable() !=null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			result.getThrowable().printStackTrace(pw);
			testUtils.log(sw.toString());
		}

		BaseTest base = new BaseTest();
		File file= base.getDriver().getScreenshotAs(OutputType.FILE);
		
		//to convert the file object to bas64 string
		byte[] encoded = null;
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//to get the testng parameters
		Map<String, String> parameters = new HashMap<>();
		parameters = 	result.getTestContext().getCurrentXmlTest().getAllParameters();
		parameters.entrySet().forEach(pa->System.out.println(pa));

		//Create the folder path 
		String imagePath = "Screenshots" + File.separator + parameters.get("platformName") + "_" + parameters.get("platformVersion")
		+ "_" + parameters.get("deviceName") + File.separator + base.getDateTime() + File.separator 
		+ result.getTestClass().getRealClass().getSimpleName() + File.separator 
		+ result.getName() + ".png"; 

		//to get the relative path of the image
		String completeImagePath = System.getProperty("user.dir") + File.separator + imagePath; 

		try {
			FileUtils.copyFile(file, new File(imagePath));
			Reporter.log("This is the sample screenshot");
			Reporter.log("<a href='"+ completeImagePath + "'> <img src='"+ completeImagePath + "' height='300' width='300'/> </a>");				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//ExtentReport.getTest().log(Status.FAIL, "Test Failed");
		try {
			ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromPath(completeImagePath).build());
			ExtentReport.getTest().fail("Test Failed", 
					MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onTestSkipped(ITestResult result) {
		ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
	}

	public void onTestStart(ITestResult result) {
		BaseTest base = new BaseTest();
		ExtentReport.startTest(result.getName(), result.getMethod().getDescription())
		.assignCategory(base.getPlatform() + "_" + base.getDeviceName())
		.assignAuthor("Sharath Hatter");
		// TODO Auto-generated method stub
	}

	public void onTestSuccess(ITestResult result) {
		ExtentReport.getTest().log(Status.PASS, "Test Passed");
	}

}
