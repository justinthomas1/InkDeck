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
		private JLabel privateName;
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
		
		/*
		
		TODO:
		-Add config file?
		
		*/
		
		
		
		
		
		
	public static void main(String[] args) throws Exception{
		
		try{
		File config= new File("config");
		
		Scanner configScan= new Scanner(config);
		
		publicFile= new File(configScan.nextLine());
		privateFile= new File(configScan.nextLine());
		
		Scanner publicScan = new Scanner(publicFile);
		Scanner privateScan = new Scanner(privateFile);
		
		e=new BigInteger(publicScan.nextLine());
		public_n=new BigInteger(publicScan.nextLine());
		
		d=new BigInteger(privateScan.nextLine());
		private_n=new BigInteger(privateScan.nextLine());
		
		}
		catch(Exception e){

		}
		
		
		jf= new JFrame();
		spy= new SuperSecretSpyCoder();
		jf.add(spy);
		jf.pack();
		jf.setSize(900,600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setTitle("InkDeck");
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
		
		
		if(!splashes.isEmpty()){
			JOptionPane.showMessageDialog(new JFrame(),("Splash Message #" + (splashSelector+1) + "\n" + splashes.get(splashSelector)));
		}
		else{
			JOptionPane.showMessageDialog(new JFrame(),("Splash Message #0\nMissingNo."));
		}
		
		
		
	}
	
	
	
	public SuperSecretSpyCoder(){
		try{			
			Image image= new ImageIcon(getClass().getResource("Icon.png")).getImage();
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//The text area
		textArea= new JTextArea("");
		JScrollPane scrollablePane= new JScrollPane(textArea);
		textArea.setFont(new Font("Serif", Font.PLAIN, 16));
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
			splashesReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Splashes.txt")));
			helpReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Help.txt")));
			copyrightReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Copyright.txt")));
			
		
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
		
				BigInteger phi_of_n= p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
				
				//For some reason, e needs to be initlized to some value, even though it's going to be reset in the next few lines.
				BigInteger e= new BigInteger("6");
		
				boolean goodExp=false;
				while(goodExp==false){
					e=new BigInteger(Integer.toString((int) (Math.random()*60000)));
					if((e.gcd(phi_of_n)).equals(BigInteger.ONE)){
						goodExp=true;
					}
				}
			
				BigInteger d=e.modInverse(phi_of_n);
				
				publicFile(e, n, codeName);
				privateFile(d, n, codeName);
			}
			
		}
		
	}
	
	private class SplashScreenListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JOptionPane.showMessageDialog(new JFrame(),("Splash Message #" + (splashSelector+1) + "\n" + splashes.get(splashSelector)));
			
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