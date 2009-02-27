import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class q2 {

	private static int s = 1;
	private static Cipher cipher;

	private static HashMap<byte[], byte[]> keylist = new HashMap<byte[], byte[]>();

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		cipher = Cipher.getInstance("AES");

		byte[] ciphertext;
		byte[] plaintext;
		String filenameC = "/home/victor/workspace/AES/group7/ciphertext-1.bin";
		String filenameP = "/home/victor/workspace/AES/group7/plaintext-1.bin";
		//String filenameC = "C:/Users/David/Desktop/6.857/pset 2/group7/group7/ciphertext-1.bin";
		//String filenameP = "C:/Users/David/Desktop/6.857/pset 2/group7/group7/plaintext-1.bin";

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

		Integer i = 0;

		
		System.out.println("The length of ciphertext " + ciphertext.length);
		System.out.println("The length of plaintext " + plaintext.length);
		
		// encrypt first
		for (int x = 0; x < Math.pow(2, s); x++) {

			byte[] key = getkey(x);
			SecretKeySpec skeySpec1 = new SecretKeySpec(key, "AES");

			// Instantiate the cipher

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec1);
			byte[] encrypted = cipher.doFinal(plaintext);

			keylist.put(encrypted, key);

		}

		for (int x = 0; x < Math.pow(2, s); x++) {

			byte[] key = getkey(x);

			SecretKeySpec skeySpec2 = new SecretKeySpec(key, "AES");

			cipher.init(Cipher.DECRYPT_MODE, skeySpec2);
			byte[] decrypted = cipher.doFinal(ciphertext);

			Iterator<Entry<byte[], byte[]>> it = keylist.entrySet().iterator();
			while (it.hasNext()) {

				Entry<byte[], byte[]> ent = it.next();
				byte[] encrypted = ent.getKey();

				if (encrypted.length != decrypted.length) {
					System.out.println("Lenght did not match");
					return;
				}

				boolean same = true;

				for (int y = 0; y < encrypted.length; y++) {

					if (encrypted[y] != decrypted[y]) {
						same = false;
						break;
					}
					
				}
				if(same){
					System.out.println("FOUND A MATCH");
					
				}
			}

		}

	}

	private static byte[] getkey(int value) {

		byte[] b = new byte[16];
	//	b[12] = (byte) (value >>> 24);
		//b[13] = (byte) (value >>> 16);
		//b[14] = (byte) (value >>> 8);
		b[15] = (byte) value;

		return b;

	}

}
