package instagram.project;


import java.util.Map;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

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

public class Instagram {
	WebDriver OKdriver;
	WebDriverWait wait;
	InstaDB instaDataBase;
	JavascriptExecutor js;
	String nameYourPage;
	int	StopProcessFollow = 1;
	int StopProcessUnFollow = 1;
	int positionFollowButton=0;
	int numberFollowAdd = 0;
	int countFollowAdd =0;
	int numberFollowersSave = 0;
	int numberUnFollow = 0;
	List<String> listWithAllPage;
	List <String> addressPhotoList;
	SimpleDateFormat simpleDateFormat;
	String currentDate;
	Date dateNow;

	// Constructor 
	public Instagram() {
		System.setProperty("webdriver.chrome.driver","geckodriver/chromedriver.exe");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("HH:mm");
		addressPhotoList = new ArrayList <String>();
		OKdriver = new ChromeDriver();
		wait = new WebDriverWait(OKdriver, 30, 500);
		OKdriver.get("https://instagram.com/");
		js= (JavascriptExecutor) OKdriver;

	}


	public boolean verificationIfLogate() {
		if(!OKdriver.findElements(By.cssSelector("a[class = '_8scx2 _gvoze coreSpriteDesktopNavProfile']")).isEmpty()) {
			return true;
		}
		return false;

	}

	// Log in
	public  void Login(String UserName,String Password) throws InterruptedException, ClassNotFoundException, SQLException
	{


		OKdriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		WebElement buttonLine=OKdriver.findElement(By.cssSelector("p[class='_g9ean']"));
		WebElement loginButton=buttonLine.findElement(By.tagName("a"));

		loginButton.click();

		// input[name='st.email']
		WebElement UserNameFild = OKdriver.findElement(By.name("username"));
		UserNameFild.clear();
		UserNameFild.sendKeys(UserName);

		// input[name='st.email']
		WebElement PasswordFild = OKdriver.findElement(By.name("password"));
		//PasswordFild.clear();
		PasswordFild.sendKeys(Password);

		// input[class='button-pro __wide
		WebElement LoginButton = OKdriver.findElement(By.cssSelector("button[class='_qv64e       _gexxb _4tgw8     _njrw0   ']"));

		// Enter in my account
		LoginButton.click();
		Thread.sleep(5000);

		// Verificate if logation with succes

	}

	public String functionGetImageLink(String namePage) throws ParseException {

		OKdriver.get("https://instagram.com/" + namePage + "/");
		if(OKdriver.findElements(By.cssSelector("div[class='_mck9w _gvoze  _tn0ps'")).isEmpty())
			return null;
		
		WebElement divElement = OKdriver.findElement(By.cssSelector("div[class='_mck9w _gvoze  _tn0ps'"));
		WebElement aElement = divElement.findElement(By.cssSelector("a:not([class])"));
		aElement.click();

		if(!OKdriver.findElements(By.cssSelector("a[class='_nzn1h _gu6vm']")).isEmpty()) {

			String dateStr = OKdriver.findElement(By.cssSelector("time[class = '_p29ma _6g6t5']")).getAttribute("datetime");
			System.out.println(dateStr);
			if(verificateIfDateIsCurrent(dateStr)){

				int numberLike =stringNumberToInteger( OKdriver.findElement(By.cssSelector("a[class='_nzn1h _gu6vm']"))
						.findElement(By.tagName("span")).getText() );
				System.out.println(numberLike);

				if(numberLike > 500) {
					System.out.println("");
					return aElement.getAttribute("href");
				}
			}

		}


		return null;
	}

	public boolean verificateIfDateIsCurrent(String date) throws ParseException {

		String dateCurent = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Calendar.getInstance().getTime());
		String dateImagePost = date.substring(0,21);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date timeCurrent = format.parse(dateCurent);
		Date timeImapePost = format.parse(dateImagePost);
		long diferentTime = (timeCurrent.getTime() - timeImapePost.getTime()) / (3600 * 1000) -2;
		if( diferentTime < 20)
			return true;
		else 
			return false;
	}

	public int functionFollingMorePage( ) throws ClassNotFoundException, SQLException, InterruptedException, ParseException {

		int numberFollowingTotal = 1100;
		int numberFollowingForOnePhoto = 5;
		int xCount = numberFollowingTotal / numberFollowingForOnePhoto;
		int indexList = -1;
        addNewFollowesDB(0);
        sendStatisticDB();
		while(true && addressPhotoList.size() > 0 ) {
			indexList++;
			System.out.println("Capcelica8");
			if(indexList == addressPhotoList.size())
				indexList = 0;
			String urlImg = functionGetImageLink(addressPhotoList.get(indexList));
			System.out.println("Capcelica3");
		  if(urlImg != null) {
				Following(urlImg,numberFollowingForOnePhoto);
				Thread.sleep(60 *1000); 
			} else {
				 System.out.println("Capcelica1");
				Thread.sleep(30 *1000);
			}
		  System.out.println("Capcelica2");
			if(StopProcessFollow != 1) {
				StopProcessFollow =1;
				break;  
			}
				
			System.out.println("Capcelica4");
			int controlNumber = instaDataBase.getNumberFollowing24h();
			System.out.println("Capcelica5");
			while(controlNumber == -1) {
				System.out.println("Don't have connection with Data Base");
				Thread.sleep(120 * 1000);
				controlNumber = instaDataBase.getNumberFollowing24h();
			}
			System.out.println("Capcelica6");
			if(controlNumber > 1050) {
				System.out.println("Azi adaugati" + controlNumber);
				takeOutFollowForFollowing(1200);
			}
			System.out.println("Azi adaugati:" + controlNumber);
		}
		return 1;
	}

	public int  Following(String URLimage, int numberFollowing) throws SQLException, InterruptedException, ClassNotFoundException, ParseException
	{


		// add new Followes in data base ;


		int timeSleepIfBlockedMin = 30;
		numberFollowAdd = 0;
		int index = -1;
		WebElement pageElement;
		String classFollowButton = "_qv64e       _gexxb _4tgw8     _njrw0   ";

		//  Open Image 
		OKdriver.get(URLimage);
		if(OKdriver.findElements(By.cssSelector("a[class='_nzn1h']")).isEmpty())
			return 0;

		// Open table with alll people that like at this image
		WebElement allLikeButton = OKdriver.findElement(By.cssSelector("a[class='_nzn1h']"));

		// Calculate Number Like 
		String numberLikeStr = allLikeButton.findElement(By.tagName("span")).getText();
		int numberLike = stringNumberToInteger(numberLikeStr);

		allLikeButton.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='_nzn1h']")));

		System.out.println("Number like for this  image is:" + numberLike);

		// Initialization javascript executor for scroll
		JavascriptExecutor js = (JavascriptExecutor) OKdriver;

		// Variable for scoling determin position next scroling
		int i=0;

		// Find TOP and BOOTON  poin windows with follow
		WebElement windowsFollow = OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']"));	 
		Point windowPoin = windowsFollow.getLocation();
		int topPositionWindow = windowPoin.getY()+8;
		int heightWindow = windowsFollow.getSize().getHeight();
		int buttonPositionWindows = topPositionWindow+(heightWindow-200);
		
		WebElement buttonElement ;
		while(OKdriver.findElements(By.cssSelector("li[class='_6e4x5']"))
				.size() < (numberLike-5) && numberFollowAdd < numberFollowing) {

			if(StopProcessFollow != 1)
				break;
			System.out.println("Index"+ index);
			index++;
			if(index == 6)
				break;
			while(index == OKdriver.findElements(By.cssSelector("li[class='_6e4x5']")).size()) {
                 
				System.out.println("Number Scroll"+ i);
				js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']")), i);
				i=i+250;
				Thread.sleep(15000);
			}
       

			// Verificate if this person  is following if following in  our database
			pageElement = OKdriver.findElements(By.cssSelector("li[class='_6e4x5']")).get(index);
			String namePage = pageElement.findElement(By.cssSelector("a[class='_2g7d5 notranslate _o5iw8 ']")).getText();
			int controlNumber = instaDataBase.addFollowingDB(namePage);
			if(controlNumber == -1) {
				System.out.println("Don't have connection with DATA BASE");
				Thread.sleep(120 * 1000);
				break;
			}
			if( controlNumber== 0) {

				buttonElement = pageElement.findElement(By.tagName("button"));

				// Verificate if button is follow not following
				if(buttonElement.getAttribute("class").equals(classFollowButton)) {

					Point buttonPoint = buttonElement.getLocation();
					int topPosition = buttonPoint.getY();

					System.out.println("Element's Position from top side Is "+topPosition +" pixels.");

					// Scolling  if elemnt is not in Windows
					while(!(topPosition>=topPositionWindow && topPosition<=buttonPositionWindows))
					{   
						// Manula intreruped process
						if(StopProcessFollow != 1)
							break;

						System.out.println("Capcelea 3");
						if(topPosition<=topPositionWindow)
						{
							i=i-1000;
						}
						else
						{
							i=i+75;
						}

						js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']")), i);
						// get new position element
						
						windowsFollow = OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']"));	 
						windowPoin = windowsFollow.getLocation();
						topPositionWindow = windowPoin.getY()+8;
						heightWindow = windowsFollow.getSize().getHeight();
						buttonPositionWindows = topPositionWindow+(heightWindow-200);
						
						positionFollowButton=topPosition;
						buttonPoint = buttonElement.getLocation();
						topPosition = buttonPoint.getY();
						
						OKdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
						System.out.println("Element's Position from top side Is "+topPosition +" pixels."+"/"+topPositionWindow + "/" + i);
						Thread.sleep(2000);

					}

					//click on follow button
					buttonElement.click();
					Thread.sleep(14000);

					if(buttonElement.getAttribute("class").equals("_qv64e       _gexxb _4tgw8     _njrw0   "))
					{   
						// if blocked i go sleep  on timeSlepIfBlocked = 5 min.
						System.out.println("Vasilii-74");
						
						//Thread.sleep(timeSleepIfBlockedMin * 60 * 1000);
						//timeSleepIfBlockedMin = timeSleepIfBlockedMin + 10;
						takeOutFollowForFollowing(timeSleepIfBlockedMin);
						timeSleepIfBlockedMin = timeSleepIfBlockedMin + 10;
						index = 0;
						//  Open Image 
						OKdriver.get(URLimage);

						// Open table with alll people that like at this image
						allLikeButton = OKdriver.findElement(By.cssSelector("a[class='_nzn1h']"));
						allLikeButton.click(); 
						

					}else 	
					{

						timeSleepIfBlockedMin = 30;
						numberFollowAdd++;
						countFollowAdd ++;
					}

				}

			}

		}
		return 1;

	}

	public void Folowing(String URLimage) throws InterruptedException
	{
		// Open pop-up with text Fild for post 

		OKdriver.get(URLimage);
		int timeSleepIfBlockedMin = 5;
		numberFollowAdd = 0;
		// Search all Like Button
		// a[class='_nzn1h']
		// Open table with alll people that like at this image
		WebElement allLikeButton = OKdriver.findElement(By.cssSelector("a[class='_nzn1h']"));

		// Calculate Number Like 
		String numberLikeStr = allLikeButton.findElement(By.tagName("span")).getText();
		int numberLike = stringNumberToInteger(numberLikeStr);

		allLikeButton.click();
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("a[class='_nzn1h']")));
		System.out.println("Number like for this  image is:" + numberLike);

		// Initialization javascript executor for scroll
		JavascriptExecutor js = (JavascriptExecutor) OKdriver;

		// Variable for scoling determin position next scroling
		int i=0;

		// Find TOP and BOOTON  poin windows with follow
		WebElement windowsFollow = OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']"));	 
		Point windowPoin = windowsFollow.getLocation();
		int topPositionWindow = windowPoin.getY()+8;
		int heightWindow = windowsFollow.getSize().getHeight();
		int buttonPositionWindows = topPositionWindow+(heightWindow-200);

		// Variable where save following button
		WebElement followButton;

		// Loop where i folowing people
		while(OKdriver.findElements(By.cssSelector("button[class='_qv64e    _t78yp    _4tgw8     _njrw0   ']"))
				.size() < (numberLike-5)){

			if(StopProcessFollow != 1)
			{
				StopProcessFollow = 1;
				break;
			}
			// Starting verificate if exist follow button
			OKdriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			if(!OKdriver.findElements(By.cssSelector("button[class='_qv64e       _gexxb _4tgw8     _njrw0   ']")).isEmpty())
			{   
				System.out.println("Capcelea 1");

				// Get follow button
				followButton = OKdriver.findElement(By.cssSelector("button[class='_qv64e       _gexxb _4tgw8     _njrw0   ']"));	 

				// get follow button position
				Point buttonPoint = followButton.getLocation();
				int topPosition = buttonPoint.getY();

				System.out.println("Element's Position from top side Is "+topPosition +" pixels.");

				// Scolling  if elemnt is not in Windows
				while(!(topPosition>=topPositionWindow && topPosition<=buttonPositionWindows))
				{   
					// Manula intreruped process
					if(StopProcessFollow != 1)
						break;

					System.out.println("Capcelea 3");
					if(topPosition<=topPositionWindow)
					{
						i=i-1000;
					}
					else
					{
						i=i+75;
					}

					js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']")), i);
					// get new position element
					positionFollowButton=topPosition;
					buttonPoint = followButton.getLocation();
					topPosition = buttonPoint.getY();
					OKdriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
					System.out.println("Element's Position from top side Is "+topPosition +" pixels."+"/"+topPositionWindow + "/" + i);
					Thread.sleep(2000);

				}

				//click on follow button
				followButton.click();
				OKdriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

				// After every click on follow button i go on 15 second sleep
				Thread.sleep(15000);

				// Verificate if not blocked
				if(followButton.getAttribute("class").equals("_qv64e       _gexxb _4tgw8     _njrw0   "))
				{   
					// if blocked i go sleep  on timeSlepIfBlocked = 5 min.
					System.out.println("Vasilii-74");
					for(int second = 0 ; second <= timeSleepIfBlockedMin * 60; second ++)
					{
						Thread.sleep(1000);

						// Manula intreruped process
						if(StopProcessFollow != 1)
							break;
					}


					timeSleepIfBlockedMin = timeSleepIfBlockedMin + 10;
				}else 	
				{

					timeSleepIfBlockedMin = 5;
					numberFollowAdd++;
				}

			}
			else{

				// if dosen't exist button follow i scroll 
				js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_ms7sh _2txtt ']")), i);
				System.out.println("Vasilii-52");
				i=i+100;
				Thread.sleep(2000);
			}

		}
	}

	public void goToProfile() throws InterruptedException
	{
		OKdriver.get("https://instagram.com/");
		// From start go to my profile
		WebElement iconProfile= OKdriver.findElement(By.cssSelector("a[class='_8scx2 _gvoze coreSpriteDesktopNavProfile']"));
		iconProfile.click();
	}

	public int takeOutFollow (int timeSleepEvery20UnFollowMin, int numberUnFollowerStarting) throws InterruptedException
	{

		numberUnFollow = 0;
		goToProfile();


		//Create list with post, followers, following
		List<WebElement> dateList= OKdriver.findElements(By.cssSelector("li[class='_bnq48 ']"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[class='_bnq48 ']")));
		//String namePage=OKdriver.findElement(By.cssSelector("ul[class='_pws6z']")).getAttribute("class");
		//System.out.println(namePage);

		// OKdriver.get("https://www.instagram.com/" + namePage + "/following/");

		//OKdriver.get("https://www.instagram.com/intrusul_live/following/");
		// Extract from list following element 
		WebElement following=dateList.get(2);


		// Extract tag a  from folowing
		WebElement followingTag_a=following.findElement(By.tagName("a"));

		// Extrag tag span with number following
		String numberFolowingStr = followingTag_a.findElement(By.tagName("span")).getText();

		int numberFollowing = stringNumberToInteger(numberFolowingStr);

		System.out.println(numberFollowing);

		// Open table with following table
		followingTag_a.click();
		Thread.sleep(1000);

		// Finde Top, and botton point
		WebElement table = OKdriver.findElement(By.cssSelector("div[class='_gs38e']"));
		Point tablePoint = table.getLocation();
		int tablePointTop = tablePoint.getY()+8;
		int tableHeight = table.getSize().getHeight();
		int tablePointBotton= tablePointTop+(tableHeight-30)-200;

		// Initialization javascript executor for scroll
		//JavascriptExecutor js = (JavascriptExecutor) OKdriver;

		// Count how money unfollow
		int countUnFollow=0;

		// Variable for scoling determin position next scroling
		int i=100;

		// Variable where save following button
		WebElement followingButton = OKdriver.findElement(By.cssSelector("button[class='_qv64e    _t78yp    _4tgw8     _njrw0   ']"));
		if(followingButton != null)
			followingButton.click();

		while(OKdriver.findElements(By.cssSelector("button[class='_qv64e       _gexxb _4tgw8     _njrw0   ']"))
				.size() < (numberFollowing-5) - numberUnFollowerStarting){

			// Stop manul process UnFollow
			if(StopProcessUnFollow != 1)
			{
				StopProcessUnFollow = 1;
				break;
			}


			if(!OKdriver.findElements(By.cssSelector("button[class='_qv64e    _t78yp    _4tgw8     _njrw0   ']")).isEmpty()
					& OKdriver.findElements(By.cssSelector("button[class='_qv64e    _t78yp    _4tgw8     _njrw0   ']")).size() > numberUnFollowerStarting )
			{    

				// Get follow button
				// OKdriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
				followingButton = OKdriver.findElements(By.cssSelector("button[class='_qv64e    _t78yp    _4tgw8     _njrw0   ']")).get(numberUnFollowerStarting);	 

				// get follow button position
				Point buttonPoint = followingButton.getLocation();
				int topPosition = buttonPoint.getY();

				System.out.println("Element's Position from top side Is "+topPosition +" pixels.");

				// Scolling  if elemnt is not in Windows
				while(!(topPosition>=tablePointTop && topPosition<=tablePointBotton))
				{
					// Stop manul process UnFollow
					if(StopProcessUnFollow !=1)
						break;

					System.out.println("Capcelea 3");
					if(topPosition<=tablePointTop)
					{
						i=i-1000;
					}
					else
					{
						i=i+35;
					}
					js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_gs38e']")), i);
					// get new position element 
					buttonPoint = followingButton.getLocation();
					topPosition = buttonPoint.getY();
					Thread.sleep(500);
				}

				//click on follow button
				followingButton.click();
				numberUnFollow++;
				if(countUnFollow == 10)
				{

					for(int second = 0; second <= timeSleepEvery20UnFollowMin * 60; second++)
					{
						Thread.sleep(1000);
						if(StopProcessUnFollow !=1)
							break;

					}
					countUnFollow = 0;
				} else {
					countUnFollow++;	
				}

				// After every click on follow button i go on 15 second sleep
				Thread.sleep(2000);


			} else {

				js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_gs38e']")), i);
				i=i+100;
				Thread.sleep(2000);
			}
		}
		return 1;


	}



	// Unfollow function for Following
	// if Following blocked  other sleep.
	public void takeOutFollowForFollowing (int timeSleep) throws InterruptedException, SQLException, ParseException
	{

		int controlNumber1;
		int numberControl;
		int timeSleepEvery20UnFollowMin = 10 ; // 10 min for sleep
		int xCount =timeSleep / timeSleepEvery20UnFollowMin; // count
		int count =0;
		goToProfile();


		//Create list with post, followers, following
		List<WebElement> dateList= OKdriver.findElements(By.cssSelector("li[class='_bnq48 ']"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[class='_bnq48 ']")));
		//String namePage=OKdriver.findElement(By.cssSelector("ul[class='_pws6z']")).getAttribute("class");
		//System.out.println(namePage);

		// OKdriver.get("https://www.instagram.com/" + namePage + "/following/");

		//OKdriver.get("https://www.instagram.com/intrusul_live/following/");
		// Extract from list following element 
		WebElement following=dateList.get(2);


		// Extract tag a  from folowing
		WebElement followingTag_a=following.findElement(By.tagName("a"));

		// Extrag tag span with number following
		String numberFollowingStr = followingTag_a.findElement(By.tagName("span")).getText();
		int numberFollowing = stringNumberToInteger(numberFollowingStr);
		System.out.println(numberFollowing);

		// Open table with following table
		followingTag_a.click();
		Thread.sleep(1000);

		// Finde Top, and botton point
		WebElement table = OKdriver.findElement(By.cssSelector("div[class='_gs38e']"));
		Point tablePoint = table.getLocation();
		int tablePointTop = tablePoint.getY()+8;
		int tableHeight = table.getSize().getHeight();
		int tablePointBotton= tablePointTop+(tableHeight-30)-200;

		// Count how money unfollow
		int countUnFollow=0;

		// Variable for scoling determin position next scroling
		int i=100;

		// Variable where save following button
		int indexList = -1;

		while((OKdriver.findElements(By.cssSelector("li[class='_6e4x5'"))
				.size() < (numberFollowing-5)) && count < xCount){


			System.out.println("count:"+ count +"| xCount:" + xCount );
			// Data 
			controlNumber1 = instaDataBase.getNumberFollowing24h();
			
			if(controlNumber1 == -1) {
				System.out.println("Don't have connection with Data Base");
				Thread.sleep(120 * 1000);
				break;
			}
			System.out.println("1:");
			if(controlNumber1 < 1050 && count > 30 )
				break;
			// Stop manul process UnFollow
			if(StopProcessFollow != 1)
				break;
			indexList ++;
			while(OKdriver.findElements(By.cssSelector("li[class='_6e4x5'")).size() == indexList) {
				System.out.println("2:");
				js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_gs38e']")), i);
				i=i+200;
				Thread.sleep(2000);
			}



			// get line page
			WebElement page =OKdriver.findElements(By.cssSelector("li[class='_6e4x5'")).get(indexList);
			System.out.println("3:");
			// get user name page			
			String namePage = page.findElement(By.cssSelector("a[class='_2g7d5 notranslate _o5iw8 ']")).getAttribute("title");
			System.out.println("4:");
			numberControl = instaDataBase.getIfReadyUnfollow(namePage);
			if(numberControl == -1) {
				System.out.println("Don't have connection with Data Base ...");
				Thread.sleep(120 * 1000);
				break;
			}
			System.out.println("5:");
			// verificate if this page is ready UnFollow
			if(numberControl == 0) {




				// Get button
				WebElement unFollowButton = page.findElement(By.tagName("button"));

				// get follow button position
				Point buttonPoint = unFollowButton.getLocation();
				int topPosition = buttonPoint.getY();

				System.out.println("Element's Position from top side Is "+topPosition +" pixels.");

				// Scolling  if elemnt is not in Windows
				while(!(topPosition>=tablePointTop && topPosition<=tablePointBotton))
				{
					// Stop manul process UnFollow
					if(StopProcessUnFollow !=1)
						break;

					System.out.println("Capcelea 3");
					if(topPosition<=tablePointTop)
					{
						i=i-1000;
					}
					else
					{
						i=i+35;
					}
					js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_gs38e']")), i);
					// get new position element
					table = OKdriver.findElement(By.cssSelector("div[class='_gs38e']"));
					tablePoint = table.getLocation();
					tablePointTop = tablePoint.getY()+8;
					tableHeight = table.getSize().getHeight();
					tablePointBotton= tablePointTop+(tableHeight-30)-200;
					buttonPoint = unFollowButton.getLocation();
					topPosition = buttonPoint.getY();
					Thread.sleep(500);
				}

				//click on follow button
				unFollowButton.click();
				numberUnFollow++;
				if(countUnFollow == 10)
				{

					for(int second = 0; second <= timeSleepEvery20UnFollowMin * 60; second++)
					{
						Thread.sleep(1000);
						if(StopProcessUnFollow !=1)
							break;

					}
					count ++;
					countUnFollow = 0;
				} else {
					countUnFollow++;	
				}

				// After every click on follow button i go on 15 second sleep
				Thread.sleep(2000);
			}



		}


	}


	//Function add new followers in database
	// if var1 = 0 add all people from followers
	// if var1 = 1 add new followers;

	public int addNewFollowesDB(int var1) throws InterruptedException, ClassNotFoundException, SQLException {

		OKdriver.get("https://instagram.com/");
		int numberWhenSleep = 1000;
		// From start go to my profile
		goToProfile();

		//Create list with post, followers, following
		List<WebElement> dateList= OKdriver.findElements(By.cssSelector("li[class='_bnq48 ']"));
		wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("li[class='_bnq48 ']")));

		// get followes WebElement 
		WebElement followers=dateList.get(1);


		// Extract tag a  from folowers Web Element
		WebElement followersTag_a=followers.findElement(By.tagName("a"));

		// Extrag tag span with number followers
		String numberFollowersStr = followersTag_a.findElement(By.tagName("span")).getAttribute("title");
		int numberFollowers = stringNumberToInteger(numberFollowersStr);

		System.out.println(numberFollowers);

		// Open table with followers table
		followersTag_a.click();
		Thread.sleep(1000);

		// Finde Top, and botton point

		int indexList = -1;
		WebElement liElement;
		WebElement tagAWithNamePerson;
		String namePerson;
		int controlNumber = 0; // When  stop add person in data base
		int i =0; // position scroll bar
		List<WebElement> List = OKdriver.findElements(By.cssSelector("li[class='_6e4x5']"));
		while(List.size() < (numberFollowers - 5) && controlNumber == 0){

			// Stop manul process UnFollow
			if(StopProcessUnFollow != 1)
			{
				StopProcessUnFollow = 1;
				break;
			}



			// Get liElement
			indexList ++;
			while(indexList == List.size()) {
				for(int i1 = 0; i1 < 10; i1++)
				{
					js.executeScript("arguments[0].scrollTop = arguments[1];",OKdriver.findElement(By.cssSelector("div[class='_gs38e']")), i);
					i=i+400;
					Thread.sleep(5000);
				}
				List = OKdriver.findElements(By.cssSelector("li[class='_6e4x5']"));

			}

			liElement = List.get(indexList); 

            System.out.println(liElement.getAttribute("class"));
			//Get Tag A with Name person
			tagAWithNamePerson = liElement.findElement(By.cssSelector("a[class='_2g7d5 notranslate _o5iw8 ']"));

			//Get Name person
			namePerson = tagAWithNamePerson.getText();
			if(var1 == 0) {
				controlNumber = instaDataBase.addFollowersDB(namePerson);
			    if(controlNumber == -1)
			    {
			    	System.out.println("Don't exists connection with DataBase...");
			    	Thread.sleep(120*1000);
			    	break;
			    }
			}
			else {
				controlNumber = instaDataBase.addFollowersDB(namePerson);
			    if(controlNumber == -1)
			    {
			    	System.out.println("Don't exists connection with DataBase...");
			    	Thread.sleep(120*1000);
			    	break;
			    }
			    controlNumber = 0;
			}
			if(indexList == numberWhenSleep )
			{
				Thread.sleep(20 * 60 * 1000);
				numberWhenSleep = numberWhenSleep +1000;
			}
			numberFollowersSave = indexList;


		}
		return 1;
	}



	public int stringNumberToInteger(String numberStr) {

		if(numberStr.indexOf(',')!=-1) {
			numberStr = numberStr.substring(0,numberStr.indexOf(','))
					+ numberStr.substring(numberStr.indexOf(',')+1,numberStr.length());
		}

		if(numberStr.indexOf(' ')!=-1) {
			numberStr = numberStr.substring(0,numberStr.indexOf(' '))
					+ numberStr.substring(numberStr.indexOf(' ')+1,numberStr.length());
		}

		if(numberStr.indexOf('.')!=-1) {
			numberStr = numberStr.substring(0,numberStr.indexOf('.'))
					+ numberStr.substring(numberStr.indexOf('.')+1,numberStr.length());
		}

		return Integer.parseInt(numberStr);
	}

	public void getMyNamePage() throws InterruptedException {

		goToProfile();

		nameYourPage = OKdriver.findElement(By.cssSelector("h1[class='_rf3jb notranslate'")).getText();
	}

	public void intilizatioDB() throws ClassNotFoundException, SQLException, InterruptedException {
		
		getMyNamePage();
		instaDataBase = new InstaDB(nameYourPage);
	}
	public void sendStatisticDB() throws InterruptedException, SQLException {

		List<WebElement> dateList= OKdriver.findElements(By.cssSelector("li[class='_bnq48 ']"));

		// get followes WebElement 

		WebElement followers=dateList.get(1);
		WebElement following =dateList.get(2); 
		System.out.println("capcelisss" + followers.findElement(By.tagName("span")).getAttribute("title"));

		int  numberFollowers = stringNumberToInteger(followers.findElement(By.tagName("span")).getAttribute("title"));
		int  numberFollowing = stringNumberToInteger(following.findElement(By.tagName("span")).getText());
		instaDataBase.addStatisticDB(numberFollowers, numberFollowing);

	}
	
	public int verificateTimeAddFollowing() throws ParseException {
		currentDate = simpleDateFormat.format(Calendar.getInstance().getTime());
		dateNow = simpleDateFormat.parse(currentDate);
		
		return 0;
	}
		
}


