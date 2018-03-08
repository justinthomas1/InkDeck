import java.io.*;
import java.util.*;
import java.math.*;

import javax.swing.*;

public class KeyMaker{
	
	private static BigInteger p;
	private static BigInteger q;
	private static BigInteger n;
	
	private static BigInteger e;
	private static BigInteger phi_of_n;
	private static BigInteger d;
	
	private static String codeName;
	
	public static void main(String[] args){
		
		//Dialog prompting the user to input a unique name.
		codeName = JOptionPane.showInputDialog(new JFrame(), "Enter a secret codename.", "KeyMaker", JOptionPane.WARNING_MESSAGE);
		
			if(codeName!=null || !codeName.equals("")){
		
			String s="";
	
			for(int i=0; i<200; i++){
				s+="355";
			}
	
			BigInteger ss= new BigInteger(s);

			do{
				p= new BigInteger((int)(3.32*300.0), 100, new Random()); //1 / 2^100 percent chance of being prime.
				q= new BigInteger((int)(3.32*300.0), 100, new Random()); //Why is (int)(3.32*300.0) needed? I don't know. It's weird!
				n= p.multiply(q);
			}
			while(n.toString().length()<599 || n.compareTo(ss)!=1);
		
			phi_of_n= p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		
			boolean goodExp=false;
			while(goodExp==false){
				e=new BigInteger(Integer.toString((int) (Math.random()*60000)));
				if((e.gcd(phi_of_n)).equals(BigInteger.ONE)){
					goodExp=true;
				}
			}
		
			d=e.modInverse(phi_of_n);
			
		
			publicFile();
			privateFile();
		}
		
		System.exit(0);
		
		}
		
		
		
	public static void publicFile(){
		try{
				FileWriter writer= new FileWriter(codeName+"-Public.txt");
				writer.write(e.toString());
				writer.write(System.getProperty("line.separator"));
				writer.write(n.toString());
				writer.close();
		}
		catch(Exception e){
				
		}
	}
	
	public static void privateFile(){
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
	
		
	
}