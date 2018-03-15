//Borrowing some handy-dandy code from a previous assignment.

import java.io.*;
import java.util.*;
import java.math.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SuperSecretSpyCoder extends JPanel{

		private static BigInteger public_n;
		private static BigInteger private_n;
		
		private static BigInteger phi_n;
		
		private static BigInteger e;
		private static BigInteger d;
		
		private JFileChooser jfc;
		
		//Just GUI stuff
		private JPanel sideBar;
		private JPanel publicBar;
		private JPanel privateBar;
		private JLabel publicName;
		private static JLabel privateName;
		private JButton publicChooser;
		private JButton privateChooser;
		private JTextArea textArea;
		
		private static SuperSecretSpyCoder spy;
		
		private static JFrame jf;
		
		private static File publicFile;
		private static File privateFile;
		
		
		private static ArrayList<String> splashes;
		private static int splashSelector;
		
		private BufferedReader splashesReader;
		private BufferedReader helpReader;
		private BufferedReader copyrightReader;
		
		
		private static Properties properties;
		private static OutputStream output = null;
		private static InputStream input = null;
		
		//Defaults for font
		final private static String[] fontChoices= {"Serif", "Monospaced", "Dialog"};
		private static int fontChoice= 0;
		final private static String[] fontSizeChoices= {"8","9","10","11","12","14","16","18","20","22","24","26","28","36","48","72"};
		private static int fontSizeChoice= 6;
		
		
		
		
	public static void main(String[] args) throws Exception{
		
		jf= new JFrame();
		spy= new SuperSecretSpyCoder();
		jf.add(spy);
		jf.pack();
		jf.setSize(900,600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("InkDeck");
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		properties = new Properties();
		
		File config= new File("config.properties");
		
		if(config.exists()){
			
		input = new FileInputStream("config.properties");

		// load a properties file
		properties.load(input);

		
		//Try to find the public key based on the preferences
		try{
			publicFile= new File(properties.getProperty("publicKey"));
		}
		catch(Exception exc){
			
		}
		
		//If it exists, read in the public key contents
		if(publicFile.exists()){
			Scanner publicScan = new Scanner(publicFile);
			e=new BigInteger(publicScan.nextLine());
			public_n=new BigInteger(publicScan.nextLine());
		}
		else{
			publicFile=null;
		}
		
			
		
		
		//Try to find the private key based on the preferences
		try{
			privateFile= new File(properties.getProperty("privateKey"));
		}
		catch(Exception exc){
			
		}
		
		
		//If it exists, read in the private key contents
		if(privateFile.exists()){
			Scanner privateScan = new Scanner(privateFile);
			d=new BigInteger(privateScan.nextLine());
			private_n=new BigInteger(privateScan.nextLine());
		}
		else{
			privateFile=null;
		}
		
		
		//Try to find the font based on the preferences
		try{
			fontChoice= Integer.parseInt(properties.getProperty("font"));
		}
		catch(Exception exc){
			
		}
		
		
		//Try to find the font size based on the preferences
		try{
			fontSizeChoice= Integer.parseInt(properties.getProperty("fontSize"));
		}
		catch(Exception exc){
			
		}
		
		
		if (input != null) {
			try {
				input.close();
			}
			catch(Exception exc){
				
			}
		}
		
		}
		else{
			try{
			createKeys();
			}
			catch(Exception ex){
				
			}
		}
		
		
		if(!splashes.isEmpty()){
			JOptionPane.showMessageDialog(new JFrame(),("Splash Message #" + (splashSelector+1) + "\n" + splashes.get(splashSelector)));
		}
		else{
			JOptionPane.showMessageDialog(new JFrame(),("Splash Message #0\nMissingNo."));
		}
		
		
		
	}
	
	
	
	public SuperSecretSpyCoder(){
		try{			
			Image image= new ImageIcon(getClass().getResource("/resources/Icon.png")).getImage();
			jf.setIconImage(image);
		}
		catch(Exception e){
			
		}
		
		this.setFocusable(true);
		
		jfc= new JFileChooser();
		
		//Constructing the menubar
		JMenuBar menuBar= new JMenuBar();
		JMenu fileMenu= new JMenu("File");
		menuBar.add(fileMenu);
		
		//New
		JMenuItem newMenuItem= new JMenuItem("New");
		fileMenu.add(newMenuItem);
		NewFileListener newList= new NewFileListener();
		newMenuItem.addActionListener(newList);
		
		//Open
		JMenuItem openMenuItem= new JMenuItem("Open (a plaintext file)");
		fileMenu.add(openMenuItem);
		OpenFileListener openList= new OpenFileListener();
		openMenuItem.addActionListener(openList);
		
		//Save
		JMenuItem saveMenuItem= new JMenuItem("Save (as a plaintext file)");
		fileMenu.add(saveMenuItem);
		SaveFileListener saveList= new SaveFileListener();
		saveMenuItem.addActionListener(saveList);
		
		//------------------------------------------------------------------------------------------------------
		//Separator
		fileMenu.addSeparator();
		//------------------------------------------------------------------------------------------------------
		
		//Encrypt
		JMenuItem encryptMenuItem= new JMenuItem("Encrypt (as a ciphertext file)");
		fileMenu.add(encryptMenuItem);
		EncryptListener encList= new EncryptListener();
		encryptMenuItem.addActionListener(encList);
		
		//Decrypt
		JMenuItem decryptMenuItem= new JMenuItem("Decrypt (a ciphertext file)");
		fileMenu.add(decryptMenuItem);
		DecryptListener decList= new DecryptListener();
		decryptMenuItem.addActionListener(decList);
		
		
		//------------------------------------------------------------------------------------------------------
		//Separator
		fileMenu.addSeparator();
		//------------------------------------------------------------------------------------------------------
		
		
		//Help
		JMenuItem helpMenuItem= new JMenuItem("Help");
		fileMenu.add(helpMenuItem);
		HelpListener helpList= new HelpListener();
		helpMenuItem.addActionListener(helpList);
		
		//Copyright
		JMenuItem copyrightMenuItem= new JMenuItem("Copyright");
		fileMenu.add(copyrightMenuItem);
		CopyrightListener copyList= new CopyrightListener();
		copyrightMenuItem.addActionListener(copyList);
		
		//Splash screen
		JMenuItem splashScreenMenuItem= new JMenuItem("View Splash Message");
		fileMenu.add(splashScreenMenuItem);
		SplashScreenListener splashList= new SplashScreenListener();
		splashScreenMenuItem.addActionListener(splashList);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//NEW MENU
		JMenu rsaMenu= new JMenu("RSA");
		menuBar.add(rsaMenu);
		
		//Generate New Keys
		JMenuItem generateNewKeysMenuItem= new JMenuItem("Generate New Keys");
		rsaMenu.add(generateNewKeysMenuItem);
		GenerateListener genList= new GenerateListener();
		generateNewKeysMenuItem.addActionListener(genList);
		
		
		//------------------------------------------------------------------------------------------------------
		//Separator
		rsaMenu.addSeparator();
		//------------------------------------------------------------------------------------------------------
		
		
		//Select Public Key
		JMenuItem selectPublicKeyMenuItem= new JMenuItem("Select Public Key");
		rsaMenu.add(selectPublicKeyMenuItem);
		PublicKeyButtonActionListener pubList= new PublicKeyButtonActionListener();
		selectPublicKeyMenuItem.addActionListener(pubList);
		
		//Select Private Key
		JMenuItem selectPrivateKeyMenuItem= new JMenuItem("Select Private Key");
		rsaMenu.add(selectPrivateKeyMenuItem);
		PrivateKeyButtonActionListener privList= new PrivateKeyButtonActionListener();
		selectPrivateKeyMenuItem.addActionListener(privList);
		
		
		
		
		
		
		
		
		
		
		
		
		//NEW MENU
		JMenu viewMenu= new JMenu("View");
		menuBar.add(viewMenu);
		
		//Select Public Key
		JMenuItem selectFontMenuItem= new JMenuItem("Font");
		viewMenu.add(selectFontMenuItem);
		FontActionListener fontList= new FontActionListener();
		selectFontMenuItem.addActionListener(fontList);
		
		//Select Private Key
		JMenuItem selectFontSizeMenuItem= new JMenuItem("Font Size");
		viewMenu.add(selectFontSizeMenuItem);
		FontSizeActionListener fontSizeList= new FontSizeActionListener();
		selectFontSizeMenuItem.addActionListener(fontSizeList);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//The text area
		textArea= new JTextArea("");
		JScrollPane scrollablePane= new JScrollPane(textArea);
		textArea.setFont(new Font(fontChoices[fontChoice], Font.PLAIN, Integer.parseInt(fontSizeChoices[fontSizeChoice])));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
		setLayout(new BorderLayout());
		
		
		
		
		
		
		//Constructing the sidebar
		sideBar= new JPanel();
		sideBar.setLayout(new GridLayout(5,0));
		
		//Adding a filler
		sideBar.add(new JLabel(""));
		
		//Adding the public section
		publicBar= new JPanel();
		publicBar.setLayout(new GridLayout(2,0));
		
		if(publicFile!=null){
			publicName= new JLabel(publicFile.getName());
		}
		else{
			publicName= new JLabel("Undeclared");
		}
		publicChooser= new JButton("Select Public Key");
		publicChooser.addActionListener(pubList);
		publicBar.add(publicName);
		publicBar.add(publicChooser);
		
		sideBar.add(publicBar);
		
		//Adding a filler
		sideBar.add(new JLabel(""));
		
		//Adding the private section
		privateBar= new JPanel();
		privateBar.setLayout(new GridLayout(2,0));
		if(privateFile!=null){
			privateName= new JLabel(privateFile.getName());
		}
		else{
			privateName= new JLabel("Undeclared");
		}
		privateChooser= new JButton("Select Private Key");
		privateChooser.addActionListener(privList);
		privateBar.add(privateName);
		privateBar.add(privateChooser);
		
		sideBar.add(privateBar);
		
		//One more filler!
		sideBar.add(new JLabel(""));
		
		jf.setJMenuBar(menuBar);
		
		splashes= new ArrayList<String>();
		
		try{
			splashesReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/Splashes.txt")));
			helpReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/Help.txt")));
			copyrightReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/Copyright.txt")));
			
		
			String line="";
			
			while ((line = splashesReader.readLine()) != null) {
				splashes.add(line);
			}
			
			splashSelector= (int) (Math.random()*splashes.size());
		}
		catch(Exception e){
			
		}
		
		add(sideBar, BorderLayout.LINE_START);
		add(scrollablePane, BorderLayout.CENTER);
	}
	
	
	
	
	private class NewFileListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(!textArea.getText().equals("") || textArea.getText()!=null){
				int chosen= JOptionPane.showConfirmDialog(new JFrame(), "Start a new message? This will erase any text you've already entered.");
				
				if(chosen==JOptionPane.YES_OPTION){
					textArea.setText(null);
				}
				
			}
		}

	}
	
	
	private class OpenFileListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//open filechooser here.
			jfc.setSelectedFile(null);
			int result= jfc.showOpenDialog(SuperSecretSpyCoder.this);
			
			if(result == JFileChooser.APPROVE_OPTION){
				try{
					updateText(jfc.getSelectedFile());
				}
				catch(Exception e){
				
				}
			}
		}

	}
	
	private class SaveFileListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			jfc.setSelectedFile(null);
			int result= jfc.showSaveDialog(SuperSecretSpyCoder.this);
			
			if(result == JFileChooser.APPROVE_OPTION){
			
				try{
				
					FileWriter writer;
				
					if(jfc.getSelectedFile().getName().endsWith(".txt")){
						writer= new FileWriter(jfc.getSelectedFile());
					}
					else{
						writer= new FileWriter(jfc.getSelectedFile() + ".txt");
					}
				
					writer.write(textArea.getText());
					writer.close();
				
				}
				catch(Exception e){
				
				}
			
			}
			
		}
		
	}
	
	
	private class EncryptListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(publicFile!=null){
			
				jfc.setSelectedFile(null);
				int result= jfc.showSaveDialog(SuperSecretSpyCoder.this);
			
				if(result == JFileChooser.APPROVE_OPTION){
			
					ArrayList<String> normalText= toStringBlocks(textArea.getText());
					ArrayList<BigInteger> normalTextInNumberForm= toBigIntBlocks(normalText);
					ArrayList<BigInteger> cipherText= encrypt(normalTextInNumberForm);

			
					String toWrite= "";
				
					for(int i=0; i<cipherText.size(); i++){
						toWrite+=cipherText.get(i);
						toWrite+="\n";
					}
			
			
					try{
				
						FileWriter writer;
				
						if(jfc.getSelectedFile().getName().endsWith(".txt")){
							writer= new FileWriter(jfc.getSelectedFile());
						}
						else{
							writer= new FileWriter(jfc.getSelectedFile() + ".txt");
						}
				
						writer.write(toWrite);
						writer.close();
				
					}
					catch(Exception e){
				
					}
			
				}
			
			
			}
			else{
				JOptionPane.showMessageDialog(new JFrame(), "Please select a public key first.", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
	}
	
	private class DecryptListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(privateFile!=null){
				//open filechooser here.
				jfc.setSelectedFile(null);
				int result= jfc.showOpenDialog(SuperSecretSpyCoder.this);
			
				if(result == JFileChooser.APPROVE_OPTION){
					ArrayList<BigInteger> cipherText= new ArrayList<BigInteger>();
			
					try{
						Scanner in = new Scanner(jfc.getSelectedFile());
		
						while(in.hasNext()){
							cipherText.add(new BigInteger(in.nextLine()));
						}
					}
					catch(Exception e){
				
					}
			
					ArrayList<BigInteger> noncipherTextOrJustNormalTextInNumberForm= decrypt(cipherText);
					ArrayList<String> normalTextAgain= plainTextToStringBlocks(noncipherTextOrJustNormalTextInNumberForm);	
			
					String decText= "";
					for(int i=0; i<normalTextAgain.size(); i++){
						decText+=normalTextAgain.get(i);
					}
			
					textArea.setText(decText);
			
				}
			}
			else{
				JOptionPane.showMessageDialog(new JFrame(), "Please select a private key first.", "Error", JOptionPane.WARNING_MESSAGE);
			}
			
		}
		
	}
	
	private class PublicKeyButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//open filechooser here.
			jfc.setSelectedFile(null);
			int result= jfc.showOpenDialog(SuperSecretSpyCoder.this);
			
			if(result == JFileChooser.APPROVE_OPTION){
				try{
					publicFile= jfc.getSelectedFile();
					
					Scanner publicScan = new Scanner(publicFile);
					
					e=new BigInteger(publicScan.nextLine());
					public_n=new BigInteger(publicScan.nextLine());
					
					publicName.setText(publicFile.getName());
					
					updateConfig();
				}
				catch(Exception e){
				
				}
			}
		}

	}
	
	private class PrivateKeyButtonActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//open filechooser here.
			jfc.setSelectedFile(null);
			int result= jfc.showOpenDialog(SuperSecretSpyCoder.this);
			
			if(result == JFileChooser.APPROVE_OPTION){
				try{
					privateFile= jfc.getSelectedFile();
					
					Scanner privateScan = new Scanner(privateFile);
					
					d=new BigInteger(privateScan.nextLine());
					private_n=new BigInteger(privateScan.nextLine());
					
					privateName.setText(privateFile.getName());
					
					updateConfig();
				}
				catch(Exception e){
				
				}
			}
		}

	}
	
	private class HelpListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			try{
			
			String text= "";
			
			String line="";
			
			text+=helpReader.readLine();
			
			while ((line = helpReader.readLine()) != null) {
				text+="\n";
				text+=line;
			}
			
			JOptionPane.showMessageDialog(new JFrame(), text);
			}
			catch(Exception e){
				
			}
			
		}
		
	}
	
	private class CopyrightListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			try{
			
			String text= "";
			
			String line="";
			
			text+=copyrightReader.readLine();
			
			while ((line = copyrightReader.readLine()) != null) {
				text+="\n";
				text+=line;
			}
			
			JOptionPane.showMessageDialog(new JFrame(), text);
			}
			catch(Exception e){
				
			}
			
		}
		
	}
	
	private class GenerateListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try{
			createKeys();
			}
			catch(Exception ex){
				
			}
		}
		
	}
	
	private class SplashScreenListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JOptionPane.showMessageDialog(new JFrame(),("Splash Message #" + (splashSelector+1) + "\n" + splashes.get(splashSelector)));
			
		}
		
	}
	
	private class FontActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String result = (String) JOptionPane.showInputDialog(null, "Choose a font.", "Choose Font", JOptionPane.QUESTION_MESSAGE, null, fontChoices, fontChoices[fontChoice]);
			
			if(result!=null){
			int i;
			
			for(i=0; i<fontChoices.length; i++){
				if(fontChoices[i].equals(result)){
					break;
				}
			}
			
			fontChoice=i;
			
			
			textArea.setFont(new Font(fontChoices[fontChoice], Font.PLAIN, Integer.parseInt(fontSizeChoices[fontSizeChoice])));
			
			try{
			updateConfig();
			}
			catch(Exception ex){
				
			}
			}
		}
	}
	
	private class FontSizeActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			String result = (String) JOptionPane.showInputDialog(null, "Choose a font size.", "Choose Font Size", JOptionPane.QUESTION_MESSAGE, null, fontSizeChoices, fontSizeChoices[fontSizeChoice]);
			
			if(result!=null){
			int i;
			
			for(i=0; i<fontSizeChoices.length; i++){
				if(fontSizeChoices[i].equals(result)){
					break;
				}
			}
			
			fontSizeChoice=i;
			
			
			textArea.setFont(new Font(fontChoices[fontChoice], Font.PLAIN, Integer.parseInt(fontSizeChoices[fontSizeChoice])));
			
			try{
			updateConfig();
			}
			catch(Exception ex){
				
			}
			}
		}
	}
	
	public void updateText(File file){	
		
		try{
			
		String output="";
		Scanner in = new Scanner(file);
		
		while(in.hasNext()){
			output+=in.nextLine();
			output+="\n";
		}
		
		textArea.setText(output);
		
		}
		catch(Exception e){
			
		}
		
	}
	
	public static void updateConfig() throws Exception{
		
		output = new FileOutputStream("config.properties");

		// set the properties value
		if(privateFile!=null){
			properties.setProperty("privateKey", privateFile.getAbsolutePath());
		}
		else{
			properties.setProperty("privateKey", "null");
		}
		

		if(publicFile!=null){
			properties.setProperty("publicKey", publicFile.getAbsolutePath());
		}
		else{
			properties.setProperty("publicKey", "null");
		}
		
		properties.setProperty("font", Integer.toString(fontChoice));
		
		properties.setProperty("fontSize", Integer.toString(fontSizeChoice));
		
		
		//properties.setProperty("prefFont", /*preferred font*/);
		//properties.setProperty("prefFontSize", /*preferred font size*/);

		// save properties to project root folder
		properties.store(output, null);
		
		if (output != null) {
				output.close();
		}
	}
	
	public static void createKeys() throws Exception{
		
		//Dialog prompting the user to input a unique name.
			String codeName = JOptionPane.showInputDialog(new JFrame(), "Enter a secret codename.", "KeyMaker", JOptionPane.WARNING_MESSAGE);
		
			if(codeName!=null || !codeName.equals("")){
		
				String s="";
	
				for(int i=0; i<200; i++){
					s+="355";
				}
	
				BigInteger ss= new BigInteger(s);

				BigInteger p;
				BigInteger q;
				BigInteger n;
			
				do{
					p= new BigInteger((int)(3.32*300.0), 100, new Random()); //1 / 2^100 percent chance of being prime.
					q= new BigInteger((int)(3.32*300.0), 100, new Random()); //Why is (int)(3.32*300.0) needed? I don't know. It's weird!
					n= p.multiply(q);
				}
				while(n.toString().length()<599 || n.compareTo(ss)!=1);
		
				phi_n= p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
				
		
				boolean goodExp=false;
				while(goodExp==false){
					e=new BigInteger(Integer.toString((int) (Math.random()*60000)));
					if((e.gcd(phi_n)).equals(BigInteger.ONE)){
						goodExp=true;
					}
				}
			
				d=e.modInverse(phi_n);
				
				publicFile(e, n, codeName);
				privateFile(d, n, codeName);
				
				
				//Set the private key to the newly generated one.
				try{
				
				privateFile= new File(codeName+"-Private.txt");
				
				Scanner privateScan = new Scanner(privateFile);
					
				d=new BigInteger(privateScan.nextLine());
				private_n=new BigInteger(privateScan.nextLine());
					
				privateName.setText(privateFile.getName());
				
				updateConfig();
					
				}
				catch(Exception exc){
					
				}
				
			}
		
	}
	
	
	
	
	
	//Making Public and Private keys
	public static void publicFile(BigInteger e, BigInteger n, String codeName){
		try{
				FileWriter writer= new FileWriter(codeName+"-Public.txt");
				writer.write(e.toString());
				writer.write(System.getProperty("line.separator"));
				writer.write(n.toString());
				writer.close();
		}
		catch(Exception ex){
				
		}
	}
	
	public static void privateFile(BigInteger d, BigInteger n, String codeName){
		try{
				FileWriter writer= new FileWriter(codeName+"-Private.txt");
				writer.write(d.toString());
				writer.write(System.getProperty("line.separator"));
				writer.write(n.toString());
				writer.close();
		}
		catch(Exception e){
				
		}
	}
	
	
	
	
	
	
	
	
	//Encryption and Decryption
	public static ArrayList<String> toStringBlocks(String output){
		ArrayList<String> normalText= new ArrayList<String>();
		
		boolean done=false;
		int stringLength= output.length();
		int currentJump= 0;
		while(!done){
			if((currentJump+200) < stringLength){
				//System.out.println(output.substring(currentJump, currentJump+200));
				normalText.add(output.substring(currentJump, currentJump+200));
				currentJump+=200;
			}
			else{
				//System.out.println(output.substring(currentJump, stringLength));
				String thingy= output.substring(currentJump, stringLength);
				int addTo= 200-(stringLength-currentJump);
				/* for(int i=0; i<addTo; i++){
					thingy+=" ";
				} */
				normalText.add(thingy);
				done=true;
			}
		}
		
		return normalText;
	}
	
	public static ArrayList<BigInteger> toBigIntBlocks(ArrayList<String> plainStrings){
		ArrayList<BigInteger> plainStringNumbers= new ArrayList<BigInteger>();
		for(int i=0; i<plainStrings.size(); i++){
			String plainTextBlock= "";
			String curString= plainStrings.get(i);
			for(int j=0; j<curString.length(); j++){
				plainTextBlock+=toPaddedAscii(curString.charAt(j));
			}
			while(plainTextBlock.length()!=600){
				plainTextBlock+="100";
			}
			//System.out.println("blockSize= " + plainTextBlock.length());
			plainStringNumbers.add(new BigInteger(plainTextBlock));
		}
		return plainStringNumbers;
	}
	
	public static ArrayList<BigInteger> encrypt(ArrayList<BigInteger> array){
		ArrayList<BigInteger> toSend= new ArrayList<BigInteger>();
		for(int i=0; i<array.size(); i++){
			toSend.add(array.get(i).modPow(e,public_n));
		}
		return toSend;
	}
	
	public static ArrayList<BigInteger> decrypt(ArrayList<BigInteger> array){
		ArrayList<BigInteger> toSend= new ArrayList<BigInteger>();
		for(int i=0; i<array.size(); i++){
			toSend.add(array.get(i).modPow(d,private_n));
		}
		return toSend;
	}
	
	public static ArrayList<String> plainTextToStringBlocks(ArrayList<BigInteger> array){
		ArrayList<String> toReturn= new ArrayList<String>();
		for(int j=0; j<array.size(); j++){
			String curBlock= array.get(j).toString();
			ArrayList<Character> charList= new ArrayList<Character>();
			for(int an=0; an<curBlock.toString().length(); an+=3){
				if(an+3<=curBlock.toString().length()){
					charList.add(unPaddedAscii(Integer.parseInt(curBlock.substring(an,an+3))));
				}
				else{
					charList.add(unPaddedAscii(Integer.parseInt(curBlock.substring(an,curBlock.toString().length()))));
				}
			}
			String add="";
			for(int q=0; q<charList.size(); q++){
				add= new StringBuilder().append(add).append(charList.get(q)).toString();
			}
			toReturn.add(add);
		}
		return toReturn;
	}
	
	public static char unPaddedAscii(int letter){
		return ((char)(letter - 100));
	}
	
	public static int toPaddedAscii(char letter){
		return (((int) letter) + 100);
	}
	
}