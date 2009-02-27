import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class q2 {

	private static int s = 25;
	private static Cipher cipher;

	private static HashMap<String, byte[]> keylist;

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		
		cipher = Cipher.getInstance("AES/ECB/NoPadding");

		byte[] ciphertext;
		byte[] plaintext;
		
		//String filenameC = "/home/victor/workspace/AES/group7/ciphertext-1.bin";
		//String filenameP = "/home/victor/workspace/AES/group7/plaintext-1.bin";
		String filenameC = "C:/Users/David/Desktop/6.857/pset 2/group7/group7/ciphertext-25.bin";
		String filenameP = "C:/Users/David/Desktop/6.857/pset 2/group7/group7/plaintext-25.bin";
		String filenameOutKey1 = "C:/Users/David/Desktop/6.857/pset 2/group7/group7/k-25.bin";
		String filenameOutKey2 = "C:/Users/David/Desktop/6.857/pset 2/group7/group7/kp-25.bin";

		File file = new File(filenameC);
		long length = file.length();
		ciphertext = new byte[(int) length];
		FileInputStream fb = new FileInputStream(file);
		fb.read(ciphertext);

		file = new File(filenameP);
		length = file.length();
		fb = new FileInputStream(file);
		plaintext = new byte[(int) length];
		fb.read(plaintext);
		
		FileOutputStream fosk1;
		FileOutputStream fosk2;
		try {
			file = new File(filenameOutKey1);
			fosk1 = new FileOutputStream(file);
			
			file = new File(filenameOutKey2);
			fosk2 = new FileOutputStream(file);
		} catch (FileNotFoundException fe) {
			System.out.println("could not open output files for writing, exiting");
			return;
		}
		
		System.out.println("The length of ciphertext " + ciphertext.length);
		System.out.println("The length of plaintext " + plaintext.length);
		
		System.out.println("Doing " + Math.pow(2,s) + " Encryptions");
		
		int i = 0;
		
		int divisions;
		
		if(s < 24){
			divisions = 1;
		}
		else{
			divisions = (int)Math.pow(2,s-23);
		}
		
		for(int start = 0; start < Math.pow(2,s); start += (Math.pow(2,s)/divisions)){
			
			i++;
			System.out.println("Section " + i);
			
			keylist = new HashMap<String, byte[]>();
			
			// encrypt first
			for (int x = start; x < start + (Math.pow(2, s) / divisions); x++) {
				
				byte[] key = getkey(x);
				SecretKeySpec skeySpec1 = new SecretKeySpec(key, "AES");
				
				cipher.init(Cipher.ENCRYPT_MODE, skeySpec1);
				byte[] encrypted = cipher.doFinal(plaintext);
	
				keylist.put(new String(encrypted), key);
				
				if(x % 1000000 == 0)
					System.out.println("Encrypted Key " + x );
			}
			
			System.out.println("Done Encrypting");
	
			for (int x = 0; x < Math.pow(2, s); x++) {
				
				byte[] key = getkey(x);
	
				SecretKeySpec skeySpec2 = new SecretKeySpec(key, "AES");
	
				cipher.init(Cipher.DECRYPT_MODE, skeySpec2);
				byte[] decrypted = cipher.doFinal(ciphertext);			
				String decStr = new String(decrypted);
				
				if(x % 1000000 == 0)
					System.out.println("Decrypted Key " + x );
				
				if(keylist.containsKey(decStr)){
					byte[] storedKey = keylist.get(decStr);
					System.out.println("Found a potential match");
					
					SecretKeySpec skeySpec1 = new SecretKeySpec(storedKey, "AES");				
					cipher.init(Cipher.ENCRYPT_MODE, skeySpec1);
					byte[] middle_stage = cipher.doFinal(plaintext);
					
					skeySpec2 = new SecretKeySpec(key, "AES");
					cipher.init(Cipher.ENCRYPT_MODE, skeySpec2);
					byte[] complete = cipher.doFinal(middle_stage);
					
					if(byteArraysEqual(ciphertext,complete)){
						System.out.println("Match is valid");
						try {
							fosk1.write(storedKey);
							fosk2.write(key);
						} catch (IOException e) {
							System.out.println("Could not write keys");
						}
						return;
					}
					else{
						System.out.println("Match is no good");
					}
				}
			}
		}
	}

	private static byte[] getkey(int value) {

		byte[] b = new byte[16];
		b[12] = (byte) (value >>> 24);
		b[13] = (byte) (value >>> 16);
		b[14] = (byte) (value >>> 8);
		b[15] = (byte) value;
		return b;

	}
	
	private static boolean byteArraysEqual(byte[] a, byte[] b){
		if(a.length != b.length)
			return false;
		
		for(int i = 0; i < a.length; i++){
			if(a[i] != b[i])
				return false;
		}
		
		return true;
	}

}
