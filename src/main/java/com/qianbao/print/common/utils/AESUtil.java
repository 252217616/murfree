package com.qianbao.print.common.utils;


import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * AES对称加密算法实现
 * @author user
 *
 */
public class AESUtil {
	/**
	 * 生成加密秘钥串
	 * @return
	 */
	public static String generateKey(){
		//获取密匙生成器
		KeyGenerator kg;
		SecretKey key=null;
		String keyStr=null;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			key = kg.generateKey();	
		} catch (NoSuchAlgorithmException e) {
		}
		keyStr=ByteArrayUtil.base16(key.getEncoded());
		return keyStr;
	}
	/**
	 * 加密数据
	 * @param key
	 * @param plainText
	 * @return
	 * @throws Exception
	 */
	public static String encryptData(String keyStr, String plainText) throws Exception {
		//创建密码器
		Cipher cp;
		byte[] decryptTextBytes;
		byte[] plainTextBytes;
		String decryptText=null;
		SecretKey key;
		byte[] keyBytes;
		try {
			cp = Cipher.getInstance("AES");
			keyBytes=ByteArrayUtil.base16decode(keyStr);
			key = new SecretKeySpec(keyBytes,"AES");
			cp.init(Cipher.ENCRYPT_MODE, key);
			//加密
			plainTextBytes=plainText.getBytes();
			decryptTextBytes = cp.doFinal(plainTextBytes);
			decryptText = ByteArrayUtil.base16(decryptTextBytes);
		} catch (Exception e) {
			throw new Exception("加密异常:"+e.getMessage());
		}
		return decryptText;
	}
	/**
	 * 数据解密
	 * @param keyStr
	 * @param decryptText
	 * @return
	 * @throws Exception
	 */
	public static String decryptData(String keyStr, String decryptText) throws Exception {
		
		Cipher cp;
		byte[] plainTextBytes=null;
		byte[] decryptTextBytes;
		SecretKey key;
		byte[] keyBytes;
		try {
			//创建密码器
			cp = Cipher.getInstance("AES");
			keyBytes=ByteArrayUtil.base16decode(keyStr);
			key = new SecretKeySpec(keyBytes,"AES");
			cp.init(Cipher.DECRYPT_MODE, key);
			//解密
			decryptTextBytes=ByteArrayUtil.base16decode(decryptText);
			plainTextBytes = cp.doFinal(decryptTextBytes);
		} catch (Exception e) {
			throw new Exception("解密异常:"+e.getMessage());
		} 
		return new String(plainTextBytes);
	}
	
	/**
	 * 生成加密秘钥串
	 * @return
	 */
	public static String generateBase64Key(){
		//获取密匙生成器
		KeyGenerator kg;
		SecretKey key=null;
		String keyStr=null;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			key = kg.generateKey();	
		} catch (NoSuchAlgorithmException e) {
		}
		keyStr = Base64.encodeBase64String(key.getEncoded());
		return keyStr;
	}
	/**
	 * 加密数据
	 * @param keyStr 秘钥
	 * @param data 加密数据
	 * @param iv 向量
	 * @return
	 * @throws Exception
	 */
	public static String encryptBase64Data(String keyStr, String data, String iv) throws Exception {
		//创建密码器
		byte[] decrypt;
		byte[] plainTextBytes;
		String decryptText=null;
		try {
			Cipher cp = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cp.getBlockSize();
            plainTextBytes = data.getBytes();
            int plaintextLength = plainTextBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(plainTextBytes, 0, plaintext, 0, plainTextBytes.length);
            SecretKey key = new SecretKeySpec(Base64.decodeBase64(keyStr), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
            cp.init(Cipher.ENCRYPT_MODE, key, ivspec);
            decrypt = cp.doFinal(plaintext);
            decryptText = Base64.encodeBase64String(decrypt);
		} catch (Exception e) {
			throw new Exception("加密异常:"+e.getMessage());
		}
		return new String(decryptText);
	}

	/**
	 * 加密数据，用于享宇金服对接
	 * @param key 秘钥
	 * @param data 加密数据
	 * @return
	 * @throws Exception
	 */
	public static String buildAESEncryptForXyjf(String data, String key) {
        try {
            // 实例化Cipher对象,它用于完成实际的加密操作
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            // 还原密钥,并初始化Cipher对象,设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(key), "AES"));
            // 执行加密操作,加密后的结果通常都会用Base64编码进行传输
            // 将Base64中的URL非法字符如'+','/','='转为其他字符,详见RFC3548
            return Base64.encodeBase64URLSafeString(cipher.doFinal(data.getBytes("UTF-8")));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

	/**
	 * 数据解密，用于享宇金福对接
	 * @param keyStr 秘钥
	 * @param data 加密数据
	 * @return
	 * @throws Exception
	 */
	public static String decryptAESCipherForXyjf(String data, String keyStr) throws Exception {
		byte[] decrypt=null;
		try {
			//创建密码器
			Cipher cp = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKey key = new SecretKeySpec(Base64.decodeBase64(keyStr),"AES");
			cp.init(Cipher.DECRYPT_MODE, key);
			//解密
			decrypt = cp.doFinal(Base64.decodeBase64(data));
		} catch (Exception e) {
			throw new Exception("解密异常:"+e.getMessage());
		} 
		return new String(decrypt);
	}
	
	/**
	 * 数据解密
	 * @param keyStr 秘钥
	 * @param data 加密数据
	 * @param iv 向量
	 * @return
	 * @throws Exception
	 */
	public static String decryptBase64Data(String keyStr, String data, String iv) throws Exception {
		byte[] decrypt=null;
		try {
			//创建密码器
			Cipher cp = Cipher.getInstance("AES/CBC/NoPadding");
			SecretKey key = new SecretKeySpec(Base64.decodeBase64(keyStr),"AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
			cp.init(Cipher.DECRYPT_MODE, key, ivspec);
			//解密
			decrypt = cp.doFinal(Base64.decodeBase64(data));
		} catch (Exception e) {
			throw new Exception("解密异常:"+e.getMessage());
		} 
		return new String(decrypt);
	}
	
//	public static void main(String args[]){
//		String keyStr=generateBase64Key();
//		System.out.println(keyStr);
//		String plainText="测试AES加解密";
//		System.out.println(plainText);
//		String iv = "7365816230567182";
//		String decryptText;
//		try {
//			decryptText = encryptBase64Data(keyStr,plainText,iv);
//			System.out.println(decryptText);
//			plainText=decryptBase64Data(keyStr,decryptText,iv);
//			System.out.println(plainText);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
