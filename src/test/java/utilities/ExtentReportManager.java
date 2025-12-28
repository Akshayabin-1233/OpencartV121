package utilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
//import java.net.URL;
import java.net.URL;

//Extent report 5.x...//version

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import testBase.BaseClassGroupExtentReport;

public class ExtentReportManager implements ITestListener {
	public ExtentSparkReporter sparkReporter;
	public ExtentReports extent;
	public ExtentTest test;

	String repName;

	public void onStart(ITestContext testContext) {
		
		// which format we want to display/generate date and time
		// first year then month then date then hour then min then sec
		
		/*SimpleDateFormat df=new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date dt=new Date();
		// Converting date and time into string format
		String currentdatetimestamp=df.format(dt);*/
		
		// alternative way of above code
		// In timestamp "new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")" is taken instead of df
		// and "new Date()" is taken instead of dt
		
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());// time stamp
		
		// create report name using above time stamp variable
		// concatenating Test Report with time stamp and .html extension
		repName = "Test-Report-" + timeStamp + ".html";
		// specify location of the report we already created reports folder in root
		sparkReporter = new ExtentSparkReporter(".\\reports\\" + repName);// specify location of the report

		sparkReporter.config().setDocumentTitle("opencart Automation Report"); // Title of report
		sparkReporter.config().setReportName("opencart Functional Testing"); // name of the report
		sparkReporter.config().setTheme(Theme.DARK);// theme of the report
		
		extent = new ExtentReports();
		extent.attachReporter(sparkReporter);
		extent.setSystemInfo("Application", "opencart");
		extent.setSystemInfo("Module", "Admin");
		extent.setSystemInfo("Sub Module", "Customers");
		// get user name using system property(current username of the system
		extent.setSystemInfo("User Name", System.getProperty("user.name"));
		extent.setSystemInfo("Environment", "QA");
		// using predefined methods we get the data from environment variables
		//get os name from xml file
		String os = testContext.getCurrentXmlTest().getParameter("os");
		extent.setSystemInfo("Operating System", os);
		
		String browser = testContext.getCurrentXmlTest().getParameter("browser");
		extent.setSystemInfo("Browser", browser);
		// get all groups is included in the included groups list
		List<String> includedGroups = testContext.getCurrentXmlTest().getIncludedGroups();
		// check if list is not empty
		// if group is present then only we are setting the system info
		// if no group is present/ group name is not available then no need to display groupname in the report
		if(!includedGroups.isEmpty()) {
		extent.setSystemInfo("Groups", includedGroups.toString());
		}
	}

	public void onTestSuccess(ITestResult result) {
		//create a new entry test case in the report
		// from result we are extracting the test class name
		test = extent.createTest(result.getTestClass().getName());
		// which ever group and testmethod is got assigned, iam also getting groups to display in report
		// assignCategory is used to assign group names to the test cases
		test.assignCategory(result.getMethod().getGroups()); // to display groups in report
		// log method is used to add logs in the report
		// result.getName() will give the class name
		test.log(Status.PASS,result.getName()+" got successfully executed");
		
	}

	public void onTestFailure(ITestResult result) {
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		
		test.log(Status.FAIL,result.getName()+" got failed");
		//to get error message in the report
		test.log(Status.INFO, result.getThrowable().getMessage());
		// Attach screenshot to the report
		// On the screen whenever failure happens that screenshot will be captured and attach the screenshot to the report
		// from the result object we get name of the class and pass that to captureScreen method
		try {
			// return filepath of captureScreen method in baseclass will be stored in imgPath variable
			String imgPath = new  BaseClassGroupExtentReport().captureScreen(result.getName());
			// below statement will attach the screenshot to the report
			test.addScreenCaptureFromPath(imgPath);
			
		} catch (IOException e1) {
			// screenshot not captured properly or screenshot not available throws file not found Exception so we put in a try catch block
			// This will display the exception message in the console where you are running your code
			e1.printStackTrace();
		}
	}

	public void onTestSkipped(ITestResult result) {
		// whenever test is skipped this method will be called
		test = extent.createTest(result.getTestClass().getName());
		test.assignCategory(result.getMethod().getGroups());
		test.log(Status.SKIP, result.getName()+" got skipped");
		//why it got skipped that reason message will be displayed in the report
		test.log(Status.INFO, result.getThrowable().getMessage());
	}

	public void onFinish(ITestContext testContext) {
		// This method will consolidate all information from report and generate the report
		
		extent.flush();
		// open the report automatically instead of opening in reports manually
		
		// Below code is an optional code
		String pathOfExtentReport = System.getProperty("user.dir")+"\\reports\\"+repName;
		File extentReport = new File(pathOfExtentReport);
		
		try {
			// Here Desktop is a class and getDesktop is a method which will open the report automatically
			Desktop.getDesktop().browse(extentReport.toURI());
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		
		/*try {
			
			// To work this we need to add apache commons-email dependency in pom.xml
			// Create the url of the extent report file
			// Mail server will not accept file type so we need to convert file to url
			// we need to send the report as an attachment in the email
		 
			  URL url = new  URL("file:///"+System.getProperty("user.dir")+"\\reports\\"+repName);
		  
		  // Create the email message 
		  ImageHtmlEmail email = new ImageHtmlEmail();
		  email.setDataSourceResolver(new DataSourceUrlResolver(url));
		  // gmail id only try this smtp.googlemail.com
		  email.setHostName("smtp.googlemail.com"); 
		  email.setSmtpPort(465);
		  email.setAuthenticator(new DefaultAuthenticator("akshayas335@gmail.com","Akku1234#akku")); 
		  email.setSSLOnConnect(true);
		  email.setFrom("akshayas335@gmail.com"); //Sender
		  email.setSubject("Test Results");
		  email.setMsg("Please find Attached Report....");
		  email.addTo("akshayas335@gmail.com"); //Receiver 
		  email.attach(url, "extent report", "please check report..."); 
		  email.send(); // send the email 
		  }
		  catch(Exception e) 
		  { 
			  e.printStackTrace(); 
			  }
		 */
		 
	}

}
