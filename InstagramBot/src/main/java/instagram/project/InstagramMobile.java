package instagram.project;


import java.util.Map;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InstagramMobile {
	
	WebDriver mobileDriver;
	WebDriverWait wait;
	
	InstagramMobile () {
		
			Map <String, String> mobileEmulation = new HashMap<String, String>();
			mobileEmulation.put("deviceName", "Nexus 5");
			
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
			
			System.setProperty("webdriver.chrome.driver","geckodriver/chromedriver.exe");
			mobileDriver = new ChromeDriver(chromeOptions);
			wait = new WebDriverWait(mobileDriver, 30, 500);
			mobileDriver.get("https://instagram.com/");
	}
	
	 // Log in
		public  void Login(String UserName,String Password) throws InterruptedException
		{

			
			mobileDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			WebElement loginButton=mobileDriver.findElement(By.cssSelector("button[class='_qv64e _gexxb _4tgw8 _njrw0']"));
			
			loginButton.click();
			
			// input[name='st.email']
			WebElement UserNameFild = mobileDriver.findElement(By.name("username"));
			UserNameFild.clear();
			UserNameFild.sendKeys(UserName);
			
			// input[name='st.email']
			WebElement PasswordFild = mobileDriver.findElement(By.name("password"));
			PasswordFild.clear();
			PasswordFild.sendKeys(Password);
			
			// input[class='button-pro __wide
			loginButton = mobileDriver.findElements(By.cssSelector("button[class='_qv64e _gexxb _4tgw8 _njrw0']")).get(1);
			
			// Enter in my account
			loginButton.click();
			Thread.sleep(5000);

			}
		
		 public void Post(){
			 
			 // click button add photo
			 mobileDriver.findElement(By.cssSelector("div[class='_crp8c coreSpriteFeedCreation']")).click();
			 
			 // Send photo to input tag
			 mobileDriver.findElements(By.cssSelector("input[class='_l8al6']")).get(2).sendKeys("C:\\Users\\Design\\Desktop\\2\\image.jpg");
			 
			 // Full image
			 mobileDriver.findElement(By.cssSelector("button[class='_j7nl9']")).click();
			 
			 // Click button  next ->
			 mobileDriver.findElement(By.cssSelector("button[class='_9glb8']")).click();
			 
			 // Complet text area
			 WebElement textArea = mobileDriver.findElement(By.cssSelector("textarea[class='_qlp0q']"));
			 textArea.click();
			 textArea.clear();
              
             // Click share post
             mobileDriver.findElement(By.cssSelector("button[class='_9glb8']")).click();
             
		 }
		
		  
		
		
		   
}
