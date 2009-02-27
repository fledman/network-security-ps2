import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class q2 {

	private static int s = 29;
	private static Cipher cipher;

	

	/**
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		cipher = Cipher.getInstance("AES/ECB/NoPadding");

		byte[] ciphertext;
		byte[] plaintext;
		String filenameC = "C:/Users/victorw/Documents/6.857/group7/ciphertext-29.bin";
		String filenameP = "C:/Users/victorw/Documents/6.857/group7/plaintext-29.bin";
		// String filenameC =
		// "C:/Users/David/Desktop/6.857/pset 2/group7/group7/ciphertext-1.bin";
		// String filenameP =
		// "C:/Users/David/Desktop/6.857/pset 2/group7/group7/plaintext-1.bin";

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
		System.out.println("Math.pow(2, " + s + ") = " + Math.pow(2, s));

		outer:
		for (int j = 0; j < 21; j++) {
			System.out.println("PHASE j = " + j);
			HashMap<String, byte[]> keylist = new HashMap<String, byte[]>();
			System.gc();
			int startval = j;
			boolean overflow = false;
			// encrypt first
			for (int x = startval; x< Math.pow(2,s); x += 64) {

				byte[] key = getkey(x);
				if (x % 10000000 == 0) {
					System.out.println(x);
					/*
					 * for (byte b: key){ if (b < 0) System.out.print(b + " ");
					 * } System.out.println("");
					 */
				}
				if (x == startval && !overflow) {
					overflow = true;
				} else if (x == startval && overflow) {
					break;
				}
				SecretKeySpec skeySpec1 = new SecretKeySpec(key, "AES");

				// Instantiate the cipher

				cipher = Cipher.getInstance("AES/ECB/NoPadding");

				cipher.init(Cipher.ENCRYPT_MODE, skeySpec1);
				byte[] encrypted = cipher.doFinal(plaintext);

				keylist.put(new String(encrypted), key);

			}

			System.out.println("ENCRYPTION PHASE COMPLETE");
			overflow = false;
			for (int x = 0; x < Math.pow(2,s); x++) {
				if (x % 10000000 == 0) {
					System.out.println(x);
				}
				if (x == 0 && !overflow) {
					overflow = true;
				} else if (x == 0 && overflow) {
					break;
				}
				byte[] key = getkey(x);
				
				SecretKeySpec skeySpec2 = new SecretKeySpec(key, "AES");

				cipher.init(Cipher.DECRYPT_MODE, skeySpec2);
				byte[] decrypted = cipher.doFinal(ciphertext);

				String decStr = new String(decrypted);

				if (keylist.containsKey(decStr)) {
					byte[] storedKey = keylist.get(decStr);
					System.out.println("Found a potential match");

					SecretKeySpec skeySpec1 = new SecretKeySpec(storedKey,
							"AES");
					cipher.init(Cipher.ENCRYPT_MODE, skeySpec1);
					byte[] middle_stage = cipher.doFinal(plaintext);

					skeySpec2 = new SecretKeySpec(key, "AES");
					cipher.init(Cipher.ENCRYPT_MODE, skeySpec2);
					byte[] complete = cipher.doFinal(middle_stage);

					if (byteArraysEqual(ciphertext, complete)) {
						System.out.print("Match is valid: k = ");
						for (byte kpiece : storedKey) {
							String kstr = Integer.toBinaryString(kpiece);
							System.out.print(kstr);
						}

						System.out.print("\nk= ");
						for (byte kpiece : key) {
							String kstr = Integer.toBinaryString(kpiece);
							System.out.print(kstr);
						}

						break outer;
					} else {
						System.out.println("Match is no good");
					}
				}

			}
			System.out.println("DECRYPTION PHASE COMPLETE");
		}
		System.out.println("DONE");
	
	}

	private static byte[] getkey(int value) {

		byte[] b = new byte[16];
		b[12] = (byte) (value >>> 24);
		b[13] = (byte) (value >>> 16);
		b[14] = (byte) (value >>> 8);
		b[15] = (byte) value;

		return b;

	}

	private static boolean byteArraysEqual(byte[] a, byte[] b) {
		if (a.length != b.length)
			return false;

		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i])
				return false;
		}

		return true;
	}
}