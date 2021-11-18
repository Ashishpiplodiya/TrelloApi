package com.trello.baseutil;

import static com.github.automatedowl.tools.AllureEnvironmentWriter.allureEnvironmentWriter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.google.common.collect.ImmutableMap;
import com.trello.util.Util;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;

/***********************************************************************
 * Description : Implements Application Precondition and Postcondition.
 * 
 * @author : Sajal, Vikas
 * @BeforeSuite: Creates all the folder structure for Allure Reports
 * @BeforeClass : Sets Allure report.
 * @BeforeMethod : Creates logs.
 * @AfterSuite : Generates Allure Report
 * 
 */
public class BaseTest {
	public static Properties prop;
	public static Logger logger = LoggerFactory.getLogger(BaseTest.class);
	public static final String CONFIGPATH = System.getProperty("user.dir") + "./config/config.properties";
	public static Util actionUtil;
	public static final int ITO = 10;
	public static final int ETO = 30;
	public static final String EXCELPATH = System.getProperty("user.dir") + "./src/main/resources/data/Data.xlsx";
	public static final String ALLUREPATH = System.getProperty("user.dir") + "./target/allure-results/";
	public static PrintStream fos;
	public static RequestSpecBuilder requestSpecBuilder;
	public static RequestSpecification requestSpecification;
	
	
	static {
		try {
			prop = new Properties();
			FileInputStream fis = new FileInputStream(CONFIGPATH);
			prop.load(fis);
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Description : Creates folder structure for Allure reports.
	 * 
	 * @author:Sajal, Vikas
	 */
	@BeforeSuite(alwaysRun = true)
	public synchronized void createFiles() {
		try {
			
			Util.deleteDir(ALLUREPATH);
			
			allureEnvironmentWriter( ImmutableMap.<String, String>builder().put("Environment", "QA").build());
			
		} catch (Exception e) {
			logger.error("Exception while report inititation");
		}

	}

	/**
	 * Description: sets Allure report
	 * 
	 * @author:Sajal, Vikas 
	 * 
	 */

	@SuppressWarnings("deprecation")
	@BeforeClass
	public void setExtentReport()  {
		
		
		requestSpecBuilder=new RequestSpecBuilder().setBaseUri(prop.getProperty("baseUrl")).addFilter(new AllureRestAssured());

		
	}
	/**
	 * Description: creates Logs
	 * 
	 * @author Sajal, Vikas
	 */

	@BeforeMethod
	public void createNode() {
		try {
		
			logger = LoggerFactory.getLogger(getClass().getName());
			actionUtil = new Util(ETO);

		} catch (Throwable e) {
			Util.error("Unable to create a Log");
			Assert.fail("Unable to create a Log");
		}

	}
	
	/**
	 * Description: Generates Allure Report
	 * 
	 * @author:Sajal, Vikas
	 * 
	 */
	
	@AfterSuite(alwaysRun = true)
	public synchronized void reportGeneration() {
		try {
			 			 
				Runtime.getRuntime().exec(new String[]{"cmd.exe","/c","start,"+System.getProperty("user.dir")+"./command.bat"});
			
				logger.info("Allure Report is Generated Successfully");
				
			} 
		

	catch (IOException e) {	

		logger.error("Failed to Generate Allure Reports");
		}
	}
	
	/**
	 * Description: Creates Bug in jira for failed test cases
	 * 
	 * @author Sajal, Manish
	 * @param: result
	 * @throws JiraException 
	 */
	@AfterMethod
	public void createBug(ITestResult result) throws JiraException {
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				String password = "plexLAd3JmzO2NS8p8oh6048";
				String username = "sajal.j@testyantra.com";
				String uri = "https://manishk01.atlassian.net/";
				String projectName = "DEM";
				String issueType = "Bug";
				BasicCredentials basicCredentials = new BasicCredentials(username, password);
				JiraClient jiraClient = new JiraClient(uri, basicCredentials);
				System.out.println("Login successful");
				Issue issueName = jiraClient.createIssue(projectName, issueType)
						.field(Field.SUMMARY, "Script failed due to " + result.getThrowable().toString())
						.field(Field.DESCRIPTION, "Get the description").execute();
				String key = issueName.getKey();
				System.out.println("Issue is created in jira with issue key:" + key);
			}

		} catch (Exception e) {
	//		WebActionUtil.error("Unable to create Bug in jira");
			Assert.fail("Unable to create Bug in jira");
		}

	}

}
