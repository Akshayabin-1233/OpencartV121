package pageObjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage {

	public MyAccountPage(WebDriver driver) {
		super(driver);
	}

	@FindBy(xpath = "//h2[text()='My Account']") // MyAccount Page heading
	WebElement msgHeading;
	
	// Logout link under MyAccount
	
	@FindBy(xpath = "//div[@class='list-group']//a[text()='Logout']") // Added in step6
	WebElement lnkLogout;
	
	// below statement will return true if MyAccount Page heading is displayed not a validation part
   // if page not exist then it will throw exception and catch block will return false

	public boolean isMyAccountPageExists()   // MyAccount Page heading display status
	{
		try {
			return (msgHeading.isDisplayed());
		} catch (Exception e) {
			return (false);
		}
	}

	public void clickLogout() {
		lnkLogout.click();

	}
	
}
