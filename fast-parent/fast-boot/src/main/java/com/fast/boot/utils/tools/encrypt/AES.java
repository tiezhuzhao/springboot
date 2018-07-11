package com.fast.boot.utils.tools.encrypt;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static final String PASSWORD = "1234567890";

	public static String RevertAESCode(String content) {
		byte[] decryptFrom = parseHexStr2Byte(content);
		byte[] decryptResult = decrypt(decryptFrom, PASSWORD);
		String decryptString = new String(decryptResult);
		return decryptString;
	}

	public static String RevertAESCode(String content, String passcode) {
		byte[] decryptFrom = parseHexStr2Byte(content);
		byte[] decryptResult = decrypt(decryptFrom, passcode);
		String decryptString = new String(decryptResult);
		return decryptString;
	}

	public static String GetAESCode(String content) {
		byte[] encryptResult = encrypt(content, PASSWORD);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		return encryptResultStr;
	}

	public static String GetAESCode(String content, String passcode) {
		byte[] encryptResult = encrypt(content, passcode);
		String encryptResultStr = parseByte2HexStr(encryptResult);
		return encryptResultStr;
	}

	private static byte[] encrypt(String content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");
			byte[] byteContent = content.getBytes("utf-8");

			cipher.init(1, key);
			return cipher.doFinal(byteContent);
		} catch (Exception e) {
			System.out.println("出错了:" + e.getMessage());
		}
		return null;
	}

	private static byte[] decrypt(byte[] content, String password) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");

			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed(password.getBytes());
			kgen.init(128, random);
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");

			Cipher cipher = Cipher.getInstance("AES");

			cipher.init(2, key);
			return cipher.doFinal(content);
		} catch (Exception e) {
			System.out.println("出错了:" + e.getMessage());
		}
		return null;
	}

	private static String parseByte2HexStr(byte[] buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1) {
			return null;
		}
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = ((byte) (high * 16 + low));
		}
		return result;
	}
}
