package instagram.project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.eclipse.jetty.websocket.common.extensions.fragment.FragmentExtension;

public class Interface {

	JFrame frame;
	JFrame addPageForFollowFrame;
	Instagram instagramDriver;
	int num=1;
	String addressImage;
	int timeBetweenUnFollow; // timpul   intre turele de unfollow
	int numberUnFollowerStarting; //  numarul de unfollow de la care sa inceapa
	String hashTag;
	JLabel countUnFollowLabel = new JLabel();
	JLabel countFollowLabel = new JLabel();


	// Constructor -------------
	public Interface() {

		frame = new JFrame("Instagram_Bot");
		frame.setSize(350, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

	}

	//process starting 
	public void startProgram() throws InterruptedException, ClassNotFoundException, SQLException, ParseException {

		//create login Panel 
		//add panel to frame
		JPanel loginPanel = new JPanel();
		frame.add(loginPanel);
		loginForm(loginPanel);
		frame.setVisible(true);
		while (true)
		{
			switch(num) {

			case 0:
				num = instagramDriver.functionFollingMorePage();
				break;

			case 2:
				num = instagramDriver.takeOutFollow(timeBetweenUnFollow, numberUnFollowerStarting);
				break;
			case 3:
				num = instagramDriver.addNewFollowesDB(1);
				break;
			default:
				//System.out.println("Wait process");

			}
			System.out.print("");
		}


	}


	public void addPageForFollowForm() throws SQLException, ClassNotFoundException, InterruptedException {

		addPageForFollowFrame = new JFrame(instagramDriver.nameYourPage);
		addPageForFollowFrame.setSize(650,650);
		addPageForFollowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addPageForFollowFrame.setLocationRelativeTo(null);

		final JPanel addPageForFollowPanel = new JPanel();
		addPageForFollowFrame.add(addPageForFollowPanel);

		

		addPageForFollowPanel.setLayout(null);
		instagramDriver.instaDataBase.getListPage();
		final List<String> listPage = instagramDriver.instaDataBase.listWithAllPage;

		// Introduceti adressa pozei
		JLabel namePageLabel = new JLabel("Enter name page (Ex:our_funny_fr1ends)");
		namePageLabel.setBounds(10, 20, 300, 25);
		addPageForFollowPanel.add(namePageLabel);
		
		// Enter time interval for add
		JLabel enterIntervileTimeLabel = new JLabel("Enter Interval Time");
		enterIntervileTimeLabel.setBounds(330, 20, 300, 25);
		addPageForFollowPanel.add(enterIntervileTimeLabel);
		
		//Start time
		JLabel startTimeLabel = new JLabel("Start time:");
		startTimeLabel.setBounds(330, 50, 60, 25);
		addPageForFollowPanel.add(startTimeLabel);
        
		// Spiner for start Time
		 Calendar calendar = new GregorianCalendar();
		 calendar.set(Calendar.HOUR_OF_DAY, 0); // 1pm
		 SpinnerDateModel dateModel = new SpinnerDateModel(calendar.getTime(), null,
		        null,Calendar.HOUR_OF_DAY);
		 
		 JSpinner spinnerStart = new JSpinner(dateModel);
		 spinnerStart.setEditor(new JSpinner.DateEditor(spinnerStart, "HH:mm"));
		 spinnerStart.setBounds(400, 50, 60, 25);
		 addPageForFollowPanel.add(spinnerStart);
		 
		 // End time
		 JLabel endTimeLabel = new JLabel("End time:");
		 endTimeLabel.setBounds(490, 50, 60, 25);
		 addPageForFollowPanel.add(endTimeLabel);
		 
		 calendar.set(Calendar.HOUR_OF_DAY, 12); // 1pm
		 SpinnerDateModel dateModel1 = new SpinnerDateModel(calendar.getTime(), null,
		        null,Calendar.HOUR_OF_DAY);
		 
		 JSpinner spinnerEnd = new JSpinner(dateModel1);
		 spinnerEnd.setEditor(new JSpinner.DateEditor(spinnerEnd, "HH:mm"));
		 spinnerEnd.setBounds(560, 50, 60, 25);
		 addPageForFollowPanel.add(spinnerEnd);
		 
		 
		// text field where enter image address https
		final JTextField namePageField = new JTextField(100);
		namePageField.setBounds(10,50,300,25);
		addPageForFollowPanel.add(namePageField);

		final JLabel countPageLabel = new JLabel ("You have Page:" + listPage.size());
		countPageLabel.setBounds(120, 80, 100,25);
		addPageForFollowPanel.add(countPageLabel);

		final Box box = Box.createVerticalBox();
		final List<JCheckBox> checkBoxList = new ArrayList<JCheckBox>();
		int indexListPage =0;
		System.out.println(listPage.size());
		while(listPage.size() != indexListPage) {

			String namePage = listPage.get(indexListPage);
			JCheckBox checkBoxPage = new JCheckBox(namePage);
			checkBoxList.add(checkBoxPage);
			box.add(checkBoxList.get(indexListPage));
			indexListPage++;

		}

		JScrollPane jscrlpBox = new JScrollPane(box);
		jscrlpBox.setBounds(10, 120, 300, 400);
		addPageForFollowPanel.add(jscrlpBox);

		JButton addButton = new JButton("add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{   
				String namePage = namePageField.getText();
				try {
					if(0 == instagramDriver.instaDataBase.putNewNamePage(namePage)) {
						namePageField.setText("");
						JCheckBox checkBoxPage = new JCheckBox(namePage);
						checkBoxList.add(checkBoxPage);
						box.add(checkBoxPage);
						countPageLabel.setText("You have Page:" + checkBoxList.size());
						addPageForFollowFrame.revalidate();
						addPageForFollowFrame.repaint();

					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		addButton.setBounds(10, 80, 100, 25);
		addPageForFollowPanel.add(addButton);
		
		// Delete Button
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener()
				{

					public void actionPerformed(ActionEvent e) {
						
						int indexList =0;
						while(checkBoxList.size()!= indexList) {
							if(checkBoxList.get(indexList).isSelected()) {
								countPageLabel.setText("You have Page:" + checkBoxList.size());
								System.out.println(checkBoxList.get(indexList).getText());
                                try {
									instagramDriver.instaDataBase.deleteNamePage(checkBoxList.get(indexList).getText());
								} catch (SQLException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
                                
                                box.remove(checkBoxList.get(indexList));
                                checkBoxList.remove(indexList);
							}
							indexList++;
						}
						
						addPageForFollowFrame.revalidate();
						addPageForFollowFrame.repaint();	
					}
			
				});
		deleteButton.setBounds(10, 550, 200, 25);
		addPageForFollowPanel.add(deleteButton);
		

		// Start Button
		JButton startButton = new JButton("Start");
		startButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{   
				instagramDriver.addressPhotoList = new ArrayList<String>();
				int indexList =0;
				System.out.println("Dimensiuni"+ checkBoxList.size());
				while(checkBoxList.size()!= indexList) {
					if(checkBoxList.get(indexList).isSelected()) {
						System.out.println(checkBoxList.get(indexList).getText());
						instagramDriver.addressPhotoList.add(checkBoxList.get(indexList).getText());
					}
					indexList++;
				}
				addPageForFollowFrame.remove(addPageForFollowPanel);
				JPanel processFollowPanel = new JPanel();
				frame.add(processFollowPanel);
				processFollowForm(processFollowPanel);
				frame.revalidate();
				frame.repaint();
				System.out.println("Marusea");
				num=0;
				addPageForFollowFrame.setVisible(false);
				frame.setVisible(true);

			}
		});

		startButton.setBounds(10, 520, 200, 25);
		addPageForFollowPanel.add(startButton);



		addPageForFollowFrame.setVisible(true);

	}
	public void processSearchPageForm(final JPanel processSearchPagePanel) {

		processSearchPagePanel.setLayout(null);

		// Label with text "Search Starting"
		JLabel processSearchPageLabel = new JLabel("Coutarea a inceput");
		processSearchPageLabel.setBounds(10, 20, 200, 25);
		processSearchPagePanel.add(processSearchPageLabel);

		// Button stop process
		JButton stopSearchPageButton = new JButton("Stop");
		stopSearchPageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ 
				num = 1;
				instagramDriver.StopProcessUnFollow = 0;
				frame.remove(processSearchPagePanel);
				JPanel functionPanel = new JPanel();
				frame.add(functionPanel);
				functionForm(functionPanel);
				frame.revalidate();
				frame.repaint();
			}
		});
		stopSearchPageButton.setBounds(10, 50, 200, 25);
		processSearchPagePanel.add(stopSearchPageButton);
	}
	public void processUnFolloWForm (final JPanel processUnFollowPanel) {
		processUnFollowPanel.setLayout(null);

		// Label with text  Start unfollow
		JLabel processUnFollowLabel = new JLabel("Proces UnFollow Starting");
		processUnFollowLabel.setBounds(10, 20, 200, 25);
		processUnFollowPanel.add(processUnFollowLabel);

		//Button stop process
		JButton stopUnFollowButton = new JButton("Stop");
		stopUnFollowButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ 
				num = 1;
				instagramDriver.StopProcessUnFollow = 0;
				frame.remove(processUnFollowPanel);
				JPanel functionPanel = new JPanel();
				frame.add(functionPanel);
				functionForm(functionPanel);
				frame.revalidate();
				frame.repaint();
			}
		});
		stopUnFollowButton.setBounds(10, 50, 200, 25);
		processUnFollowPanel.add(stopUnFollowButton);

		JButton refreshUnFollowButton = new JButton("Refresh");
		refreshUnFollowButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ 
				countUnFollowLabel.setText("UnFollow:"+instagramDriver.numberUnFollow);
				frame.revalidate();
				frame.repaint();
			}
		});
		refreshUnFollowButton.setBounds(10, 80, 200, 25);
		processUnFollowPanel.add(refreshUnFollowButton);


		countUnFollowLabel.setBounds(10, 110, 100, 25);
		processUnFollowPanel.add(countUnFollowLabel);

	}
	public void searchPageForm(final JPanel searchPagePanel)
	{
		searchPagePanel.setLocale(null);

		// Labe with text Enter hashteg 
		JLabel searchPageLabel = new JLabel("Enter #tag :");
		searchPageLabel.setBounds(10, 20, 200, 25);
		searchPagePanel.add(searchPageLabel);

		// set text field put #tag
		final JTextField hashTagField = new JTextField(50);
		hashTagField.setBounds(10, 50, 300, 25);
		searchPagePanel.add(hashTagField);

		// Labe with text Enter Number page wee need 
		JLabel searchPageNumberLabel = new JLabel("Enter number Page need");
		searchPageNumberLabel.setBounds(10, 20, 200, 25);
		searchPagePanel.add(searchPageNumberLabel);

		// set text field put #tag
		final JTextField searchPageNumberField = new JTextField(50);
		searchPageNumberField.setBounds(10, 50, 300, 25);
		searchPagePanel.add(searchPageNumberField);



		// Button Start Search
		JButton startSearchPageButton = new JButton("Start");
		startSearchPageButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ 
				hashTag=hashTagField.getText();
				frame.remove(searchPagePanel);
				num = 3;
				JPanel processSearchPagePanel =  new JPanel();
				frame.add(processSearchPagePanel);
				processSearchPageForm(processSearchPagePanel);
				frame.revalidate();
				frame.repaint();
			}
		});
		startSearchPageButton.setBounds(10, 80, 200, 25);
		searchPagePanel.add(startSearchPageButton);

	}

	public void unFollowForm(final JPanel unFollowPanel) {

		unFollowPanel.setLayout(null);

		// Label with text Starting unfollow now ?
		JLabel unFollowLabel = new JLabel("Enter time sleep between every 10 UnFollow");
		unFollowLabel.setBounds(10, 5, 200, 25);
		unFollowPanel.add(unFollowLabel);

		// Set text field where put time between unfollow in second
		final JTextField  timeBetweenUnFollowField = new JTextField(10);
		timeBetweenUnFollowField.setBounds(10,35,300,25);
		unFollowPanel.add(timeBetweenUnFollowField);

		// Label with text Enter number unFollow Starting
		JLabel numberUnFollowLabel = new JLabel("Enter number unFollow for Starting");
		numberUnFollowLabel.setBounds(10, 65, 300, 25);
		unFollowPanel.add(numberUnFollowLabel);

		// Set Text field where put number unfollow starting
		final JTextField  numberUnFollowLabelField = new JTextField(10);
		numberUnFollowLabelField.setBounds(10, 95,300,25);
		unFollowPanel.add(numberUnFollowLabelField);

		// Set  button starting unFollow
		JButton startUnFollowButton = new JButton("Start");
		startUnFollowButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ 
				timeBetweenUnFollow=Integer.parseInt(timeBetweenUnFollowField.getText());
				numberUnFollowerStarting = Integer.parseInt(numberUnFollowLabelField.getText());
				frame.remove(unFollowPanel);
				num = 2;
				JPanel processUnFollowPanel =  new JPanel();
				frame.add(processUnFollowPanel);
				processUnFolloWForm(processUnFollowPanel);
				frame.revalidate();
				frame.repaint();
			}
		});
		startUnFollowButton.setBounds(10, 125, 200, 25);
		unFollowPanel.add(startUnFollowButton);
	}

	public void processFollowForm(final JPanel processFollowPanel) {

		processFollowPanel.setLayout(null);
		System.out.println("Marusea");
		// follow procces is starting
		JLabel proccessFollowLabel = new JLabel("The follow add process starts");
		proccessFollowLabel.setBounds(10, 20, 200, 25);
		processFollowPanel.add(proccessFollowLabel);

		// Stop Button process

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{   
				num=1;
				instagramDriver.StopProcessFollow = 0;
				frame.remove(processFollowPanel);
				JPanel functionPanel = new JPanel();
				frame.add(functionPanel);
				functionForm(functionPanel);
				frame.revalidate();
				frame.repaint();

			}
		});

		stopButton.setBounds(10, 50, 200, 25);
		processFollowPanel.add(stopButton);

		JButton refreshFollowButton = new JButton("Refresh");
		refreshFollowButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{ 
				countFollowLabel.setText("Follow add:"+instagramDriver.countFollowAdd);
				frame.revalidate();
				frame.repaint();
			}
		});
		refreshFollowButton.setBounds(10, 80, 200, 25);
		processFollowPanel.add(refreshFollowButton);


		countFollowLabel.setBounds(10, 110, 100, 25);
		processFollowPanel.add(countFollowLabel);



	}

	public void addFollowForm(final JPanel addFollowPanel)
	{   

		addFollowPanel.setLayout(null);
		instagramDriver.addressPhotoList = new ArrayList<String>();

		// Introduceti adressa pozei
		JLabel addresField = new JLabel("Enter name page (Ex:our_funny_fr1ends)");
		addresField.setBounds(10, 20, 340, 25);
		addFollowPanel.add(addresField);

		// text field where enter image address https
		final JTextField addresImageField = new JTextField(500);
		addresImageField.setBounds(10,50,300,25);
		addFollowPanel.add(addresImageField);

		final JLabel countNumberImageLabel = new JLabel ("Adrress Imagini: 0");
		countNumberImageLabel.setBounds(120, 80, 100,25);
		addFollowPanel.add(countNumberImageLabel);

		JButton addButton = new JButton("add");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{ 
				instagramDriver.addressPhotoList.add(addresImageField.getText());
				addresImageField.setText("");
				countNumberImageLabel.setText("Adrress Imagini :" + instagramDriver.addressPhotoList.size());
				frame.revalidate();
				frame.repaint();
			}
		});

		addButton.setBounds(10, 80, 100, 25);
		addFollowPanel.add(addButton);



		// Start Button
		JButton startButton = new JButton("Start");
		startButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{   
				//addressImage = addresImageField.getText();
				frame.remove(addFollowPanel);
				JPanel processFollowPanel = new JPanel();
				frame.add(processFollowPanel);
				processFollowForm(processFollowPanel);
				frame.revalidate();
				frame.repaint();
				System.out.println("Marusea");
				num=0;

			}
		});

		startButton.setBounds(10, 120, 200, 25);
		addFollowPanel.add(startButton);

	}


	public void functionForm(final JPanel functionPanel)
	{

		// Verificate if valabile abonament

		functionPanel.setLayout(null);

		// *Alegeti obtiunea dorita
		JLabel chooseField = new JLabel("Choose the option:");
		chooseField.setBounds(10, 20, 200, 25);
		functionPanel.add(chooseField);

		// *addFolowing
		JButton addFolowingButton = new JButton("Add Folowing");
		addFolowingButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{   
				frame.remove(functionPanel);
				try {
					addPageForFollowForm();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				frame.setVisible(false);

			}
		});

		addFolowingButton.setBounds(10, 50, 200, 25);
		functionPanel.add(addFolowingButton);

		// unFollow

		JButton unfollowButton = new JButton("Unfollower");
		unfollowButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				frame.remove(functionPanel);
				JPanel unFollowPanel = new JPanel();
				frame.add(unFollowPanel);
				unFollowForm(unFollowPanel);
				frame.revalidate();
				frame.repaint();

			}
		});

		unfollowButton.setBounds(10, 80, 200, 25);
		functionPanel.add(unfollowButton);

		// Search  page by hashtag

		JButton saveFollowersButton = new JButton("Save all Followers");
		saveFollowersButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{   

				frame.remove(functionPanel);
				JPanel saveFollowersPanel = new JPanel();
				processSaveFollower(saveFollowersPanel);
				num =3;
				frame.add(saveFollowersPanel);
				frame.revalidate();
				frame.repaint();

			}
		});

		saveFollowersButton.setBounds(10, 120, 200, 25);
		saveFollowersButton.setVisible(true);
		functionPanel.add(saveFollowersButton);

	}

	public void processSaveFollower(final JPanel saveFollowersPanel) {
		saveFollowersPanel.setLayout(null);

		JLabel saveFollowerLabel =  new JLabel ("Process Save Follower Starting");
		saveFollowerLabel.setBounds(10,20, 300, 25);
		saveFollowersPanel.add(saveFollowerLabel);

		JButton stopProcessSaveFollowers = new JButton("Stop Process");
		stopProcessSaveFollowers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				instagramDriver.StopProcessUnFollow = 0;
				frame.remove(saveFollowersPanel);
				JPanel functionPanel = new JPanel();
				frame.add(functionPanel);
				functionForm(functionPanel);
				frame.revalidate();
				frame.repaint();

			}
		});
		stopProcessSaveFollowers.setBounds(10,50,200, 25);
		saveFollowersPanel.add(stopProcessSaveFollowers);

		final JLabel numberFollowersSaveLabel =  new JLabel ("Save Followers:" + instagramDriver.numberFollowersSave);
		numberFollowersSaveLabel.setBounds(10,120, 300, 25);
		saveFollowersPanel.add(numberFollowersSaveLabel);

		JButton refreshNumberSaveFollowers = new JButton ("Refresh");
		refreshNumberSaveFollowers.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				numberFollowersSaveLabel.setText("Save Followers:" + instagramDriver.numberFollowersSave);
				frame.revalidate();
				frame.repaint();
			}
		});

		refreshNumberSaveFollowers.setBounds(10, 80, 200, 25);
		saveFollowersPanel.add(refreshNumberSaveFollowers);
	}
	public void experatioAbonament (final JPanel experationAbonamentPanel )
	{

		experationAbonamentPanel.setLayout(null);
		// Create label user
		JTextArea messageTextArea = new JTextArea("Hey, your subscription is EXPERID! \n"
				+ "Contact your administrator to be extended \n"
				+ "skype: xxpiaraniaxx2\n"
				+ "email: nicucapcelea@gmail.com \n"
				+ "whatApp: +40 752 421 442 ");

		messageTextArea.setEditable(false);
		messageTextArea.setBounds(10,20,330,80);
		experationAbonamentPanel.add(messageTextArea);

		JButton refreshLogin = new JButton("Refresh");
		refreshLogin.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
					if(0>=instagramDriver.instaDataBase.verificateAbonament()) {

					} else {
						frame.remove(experationAbonamentPanel);
						JPanel functionPanel = new JPanel();
						frame.add(functionPanel);
						functionForm(functionPanel);
						frame.revalidate();
						frame.repaint();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		refreshLogin.setBounds(10,120,150,25);
		experationAbonamentPanel.add(refreshLogin);
	}


	public void loginError(final JPanel loginErrorPanel )
	{

		loginErrorPanel.setLayout(null);
		// Create label user
		JTextArea messageTextArea = new JTextArea("Your login or password is incorrect \n"
				+ "enter the BROWSER and correct it \n "
				+ "and  click button LOGIN from BROWSER\n"
				+ "after a successful login, click the refresh button below! ");

		messageTextArea.setEditable(false);
		messageTextArea.setBounds(10,20,330,80);
		loginErrorPanel.add(messageTextArea);

		JButton refreshLogin = new JButton("Refresh");
		refreshLogin.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(instagramDriver.verificationIfLogate()) {
					try {
						instagramDriver.intilizatioDB();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					frame.remove(loginErrorPanel);
					try {
						if(0>=instagramDriver.instaDataBase.verificateAbonament()) {
							JPanel experationAbonamentPanel = new JPanel();
							experatioAbonament(experationAbonamentPanel);
							frame.setTitle(instagramDriver.nameYourPage);
							frame.add(experationAbonamentPanel);
							frame.revalidate();
							frame.repaint();
						} else {
							JPanel functionPanel = new JPanel();
							frame.setTitle(instagramDriver.nameYourPage);
							frame.add(functionPanel);
							functionForm(functionPanel);
							frame.revalidate();
							frame.repaint();
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 

				}
			}
		});

		refreshLogin.setBounds(10,120,150,25);
		loginErrorPanel.add(refreshLogin);
	}

	public void loginForm(final JPanel loginPanel) {

		// instagramDriver.sendStatisticDB();
		// instagramDriver.addNewFollowesDB();
		loginPanel.setLayout(null);

		// Create label user
		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(10,20,80,25);
		loginPanel.add(userLabel);

		// Create text filed where write
		// User name
		final JTextField userTextField = new JTextField(30);
		userTextField.setBounds(100,20,165,25);
		loginPanel.add(userTextField);

		// Create label password
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10,50,80,25);
		loginPanel.add(passwordLabel);

		// Create password filed where write
		// Password
		final JPasswordField passwordField = new JPasswordField(30);
		passwordField.setBounds(100,50,165,25);
		loginPanel.add(passwordField);

		// Create login button
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener( new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				String userName = userTextField.getText();
				String passwordName = passwordField.getText();
				//Initialization Instagram driver
				instagramDriver = new Instagram();
				try {
					instagramDriver.Login(userName, passwordName);
					if(instagramDriver.verificationIfLogate()) {
						instagramDriver.intilizatioDB();
						frame.remove(loginPanel);
						if(0>=instagramDriver.instaDataBase.verificateAbonament()) {
							JPanel experationAbonamentPanel = new JPanel();
							experatioAbonament(experationAbonamentPanel);
							frame.setTitle(instagramDriver.nameYourPage);
							frame.add(experationAbonamentPanel);
							frame.revalidate();
							frame.repaint();
						} else {
							JPanel functionPanel = new JPanel();
							frame.setTitle(instagramDriver.nameYourPage);
							functionForm(functionPanel);
							frame.add(functionPanel);
							frame.revalidate();
							frame.repaint(); }
					}else {
						frame.remove(loginPanel);
						JPanel loginErroPanel = new JPanel();
						frame.add(loginErroPanel);
						loginError(loginErroPanel);
						frame.revalidate();
						frame.repaint();
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}





			}
		});
		loginButton.setBounds(10, 80, 80, 25);
		loginPanel.add(loginButton);
	}



}
