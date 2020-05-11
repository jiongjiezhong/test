package com.zjj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.Charset;

/**
 * DES工具类
 *
 * @author CYF
 * @Date 2018年12月7日
 */
public class DesUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(DesUtil.class);
	public static final String SKEY = "birdccbk";
	public static final String ETC2_SKEY = "ccbetc2k";

	/** 助力小程序userId加密key */
	public static final String ICBC_BOOST_USER_KEY = "yaphets_";
	private static final Charset CHARSET = Charset.forName("gb2312");

	public static void main(String[] args) {
		System.out.println(encrypt("6",ICBC_BOOST_USER_KEY ));
	}
	/**
	 * 加密
	 * 
	 * @param srcStr
	 * @param sKey
	 * @return
	 */
	public static String encrypt(String srcStr, String sKey) {
		byte[] src = srcStr.getBytes(CHARSET);
		byte[] buf = encrypt(src, sKey);
		return parseByte2HexStr(buf);
	}

	/**
	 * 解密
	 *
	 * @param hexStr
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String hexStr, String sKey) {
		try {
			if (StringUtil.isEmpty(hexStr))
				return null;
			byte[] src = parseHexStr2Byte(hexStr);
			byte[] buf = decrypt(src, sKey);
			return new String(buf, CHARSET);
		} catch (Exception e) {
			LOGGER.error("DES 解密出错！", e);
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param data
	 * @param sKey
	 * @return
	 */
	private static byte[] encrypt(byte[] data, String sKey) {
		try {
			byte[] key = sKey.getBytes();
			// 初始化向量
			IvParameterSpec iv = new IvParameterSpec(key);
			DESKeySpec desKey = new DESKeySpec(key);
			// 创建一个密匙工厂，然后用它把DESKeySpec转换成securekey
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey securekey = keyFactory.generateSecret(desKey);
			// Cipher对象实际完成加密操作
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			// 用密匙初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, securekey, iv);
			// 现在，获取数据并加密
			// 正式执行加密操作
			return cipher.doFinal(data);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param src
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] src, String sKey) throws Exception {
		byte[] key = sKey.getBytes();
		// 初始化向量
		IvParameterSpec iv = new IvParameterSpec(key);
		// 创建一个DESKeySpec对象
		DESKeySpec desKey = new DESKeySpec(key);
		// 创建一个密匙工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		// 将DESKeySpec对象转换成SecretKey对象
		SecretKey securekey = keyFactory.generateSecret(desKey);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, iv);
		// 真正开始解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 将二进制转换成16进制
	 *
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
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

	/**
	 * 将16进制转换为二进制
	 *
	 * @param hexStr
	 * @return
	 */
	private static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
