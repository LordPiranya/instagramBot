package instagram.project;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class InterfacePostari  extends JPanel{
	JFrame frame;
    InstagramMobile instagramMobileDriver;
    JFrame frameAddPost;
    JFrame frameInfoPost;
	InterfacePostari() {
		frame = new JFrame("Instagram_Bot_mobile");
		frame.setSize(350, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}

//  Logare
public void loginForm(final JPanel loginPanel) {
	 
	 
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
	         instagramMobileDriver = new InstagramMobile();
	         frame.remove(loginPanel);
	         JPanel functionPanel = new JPanel();
	         frame.add(functionPanel);
	         functionForm(functionPanel);
	         frame.revalidate();
	         frame.repaint();
	         
	         
	     }
	 });
	 loginButton.setBounds(10, 80, 80, 25);
	 loginPanel.add(loginButton);
}

// functionForm
public void functionForm(final JPanel functionPanel){
    
	 functionPanel.setLayout(null);
	 
	 // *Alegeti obtiunea dorita
	 JLabel chooseField = new JLabel("Alege functia dorită");
	 chooseField.setBounds(10, 20, 200, 25);
	 functionPanel.add(chooseField);
	 
	 // *addFolowing
	 JButton addPostButton = new JButton("Add Post");
	 addPostButton.addActionListener( new ActionListener()
	 {
	     public void actionPerformed(ActionEvent e)
	     {
	         frame.remove(functionPanel);
	         JPanel addPostPanel = new JPanel();
	         frame.add(addPostPanel);
	         //addPostForm(addPostPanel);
	         frame.revalidate();
	         frame.repaint();
	            
	     }
	 });
	 
	 addPostButton.setBounds(10, 50, 200, 25);
	 functionPanel.add(addPostButton);
	 
	 // unFollow
	 
	 JButton infoPostButton = new JButton("Info Post");
	 infoPostButton.addActionListener( new ActionListener()
	 {
	     public void actionPerformed(ActionEvent e)
	     {
	         frame.remove(functionPanel);
	         JPanel infoPostPanel = new JPanel();
	         frame.add(infoPostPanel);
	         //infoPostForm(infoPostPanel);
	         frame.revalidate();
	         frame.repaint();
	            
	     }
	 });


}

public void addPostForm(final JPanel addPostPanel) throws InterruptedException{
	
	// Create frame 
	frameAddPost = new JFrame("Adaugarea de Postari");
	frameAddPost.setSize(750, 750);
	frameAddPost.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frameAddPost.setLocationRelativeTo(null);
	
	addPostPanel.setLayout(null);
	
	
	// Create Post
	JLabel createPostLabel = new JLabel("Create and  programe and schedule");
	createPostLabel.setBounds(10, 20, 730, 25);
	addPostPanel.add(createPostLabel);
	
	// Image label
	JLabel imageLabel = new JLabel("image:");
	imageLabel.setBounds(10, 50 , 50 , 25);
	addPostPanel.add(imageLabel);
	
	//Addres image field
	final JTextField imageAddressField = new JTextField("");
	imageAddressField.setBounds(70, 50 , 500, 25);
	addPostPanel.add(imageAddressField);
	
	// Button open image
	JButton openImageButton = new JButton ("open image");
	openImageButton.addActionListener(new ActionListener() {

		public void actionPerformed(ActionEvent e) {
		   JFileChooser fileChooser = new JFileChooser();
		   
		   fileChooser.showOpenDialog(InterfacePostari.this);
		   String pathSelectedFile = fileChooser.getSelectedFile().getAbsolutePath();
		   imageAddressField.setText(pathSelectedFile);
		   frameAddPost.revalidate();
	       frameAddPost.repaint();	
		}
	});
	openImageButton.setBounds(580, 50, 75, 25);
	addPostPanel.add(openImageButton);
	
	// Text arria description
	JTextArea descriptionTextArea = new JTextArea();
	descriptionTextArea.setSize(580, 350);
	descriptionTextArea.setMargin(new Insets(5,5,5,5));
    descriptionTextArea.setEditable(true);
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setVisible(true);
    JScrollPane scroll =  new JScrollPane(descriptionTextArea);
    scroll.setBounds(70, 80, 580, 350);
    //scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    addPostPanel.add(scroll);
	
	frameAddPost.add(addPostPanel);
	frameAddPost.setVisible(true);
	Thread.sleep(30*1000);
	String ap = descriptionTextArea.getText();
	System.out.println(ap);
	
	
	
	
}
}

