package com.hisun.util;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.SecureRandom;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;

/**
 * 
* @ClassName: DesPassUtil 
* @Description: DES加解密算法
* @author jamin30
* @date 2015年06月11日
 */
public class DesPassUtil {
	
	private final static String DES = "DES";

    public final static String PRIVATE_KEY = ")!(@*#&$^%";

    /**
	 * 根据key进行加密
	 * @param data
	 * @param key 至少64个字节
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data,String key) throws Exception
	{
		byte[] bt = encrypt(data.getBytes(),key.getBytes());
		String newStr = new BASE64Encoder().encode(bt);
		return newStr;
	}

	/**
	 * 根据key进行解密
	 * @param data
	 * @param key 至少64个字节
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String decrypt(String data,String key) throws IOException, Exception
	{
		if(null==data)
		{
			return null;
		}
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] buf = decoder.decodeBuffer(data);
		byte[] bt = decrypt(buf, key.getBytes());
		return new String(bt);
	}
	
	
	/**
	 * 解密
	 * @param data
	 * @param key 
	 * @return
	 * @throws Exception
	 */
	private static byte[] decrypt(byte[] data, byte[] key) throws Exception{
		// TODO Auto-generated method stub
		//生成一个随机数
		SecureRandom sr = new SecureRandom();
		
		//根据key创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		
		//创建密钥工厂
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey secureKey = keyFactory.generateSecret(dks);
		
		//解密
		Cipher cipher = Cipher.getInstance(DES);
		
		//初始化
		cipher.init(Cipher.DECRYPT_MODE, secureKey,sr);
		return cipher.doFinal(data);
	}

	
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
		//生成一个随机数
		SecureRandom sr = new SecureRandom();
		
		//从key创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		
		//创建一个密钥工厂，然后转换成secretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey secureKey = keyFactory.generateSecret(dks);
		
		//实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		//初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, secureKey,sr);
		
		return cipher.doFinal(data);
	}

}
