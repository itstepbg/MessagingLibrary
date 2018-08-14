package library.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

	public static String generateHash(String input) {
		String sha256 = "";
		try {
			MessageDigest hash = MessageDigest.getInstance("SHA-256");
			hash.reset();
			hash.update(input.getBytes("UTF-8"));
			sha256 = byteToHex(hash.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha256;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	// TODO Add the session ID to the salting process for extra security.
	public static String saltPassword(String salt, String passwordHash, int iterations) {
		String result = passwordHash;

		for (int i = 0; i < iterations; i++) {
			result = generateHash(salt + result);
		}

		return result;
	}

	public static byte[] generateRandomSalt() {
		byte[] salt = new byte[8];
		try {
			SecureRandom.getInstanceStrong().nextBytes(salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return salt;

	}

	public static int getRandomIterations() {
		Random random = new Random();
		int maximum = 4096;
		int minimum = 1024;

		return random.nextInt((maximum - minimum) + 1) + minimum;

	}

	public static byte[] intToBytes( final int i ) {
	    ByteBuffer bb = ByteBuffer.allocate(4);
	    bb.putInt(i);
	    return bb.array();
	}

	public static byte[] getInitVector(){

		int ivSize = 16;
        byte[] iv = new byte[ivSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        System.out.println();

        return ivParameterSpec.getIV();

	}

	// encrypt data with AES - 256
	public static String encryptAES256(String value, byte[] initVector, String key) {
		try {
			String keyFirst16Bit = key.substring(0, 32);
			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(keyFirst16Bit.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	// decrypt data with AES - 256
	public static String decryptAES256(String encrypted, byte[] initVector, String key) {
		try {
			String keyFirst16Bit = key.substring(0, 32);
			IvParameterSpec iv = new IvParameterSpec(initVector);
			SecretKeySpec skeySpec = new SecretKeySpec(keyFirst16Bit.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));

			return new String(original);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


}
