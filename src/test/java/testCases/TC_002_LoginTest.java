package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import testBase.BaseClass;

public class TC_002_LoginTest extends BaseClass
{
   // only validation part is required.
	@Test
	public void verify_login()
	{
		logger.info("**** Starting TC_002_LoginTest  ****");
		logger.debug("capturing application debug logs....");
		try
		{
		//Home page
		HomePage hp=new HomePage(driver);
		// click on MyAccount Link in Home page
		// two actions from home page are clickmyaccount and clicklogin
		hp.clickMyAccount();
		logger.info("clicked on myaccount link on the home page..");
		hp.clickLogin(); //Login link under MyAccount
		logger.info("clicked on login link under myaccount..");
		
		//Create Login page object and call methods for login page
		// Test case for valid credentials
		LoginPage lp=new LoginPage(driver);
		logger.info("Entering valid email and password..");
		// email and password values reading from properties file
		// p is object of properties class created in base class
		// Note:we cannot hardcode any values in test cases
		// email and password put in double quotes only even though it is a variable
		lp.setEmail(p.getProperty("email"));
		lp.setPassword(p.getProperty("password"));
		lp.clickLogin(); //Login button
		logger.info("clicked on login button..");
		
		//My Account Page
		MyAccountPage macc=new MyAccountPage(driver);
		
		// Validation-- check whether MyAccount page exists or not and return boolean value, capture it in targetPage variable
				
		boolean targetPage=macc.isMyAccountPageExists();
		// assert equals method will check whether targetPage is true or false
		// if true then login is successful else login failed message will be displayed in the console
		
		Assert.assertEquals(targetPage, true,"Login failed");
		// or we can use below statement also
		//Assert.assertTrue(targetPage);
		}
		catch(Exception e)
		{
			// if test case failed then the exception message will be displayed in the console
			Assert.fail();
		}
		
		logger.info("**** Finished TC_002_LoginTest  ****");
	}
}
