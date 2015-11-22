package base.util;

import java.security.Key;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;

import common.enums.CommonMsgCodeEnums;
import common.lang.exception.CspMsg;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：AESCoder.java<br>
 * 描 述 ：<br>
 * 公 司 ：Hongfang intelligent technology.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : Mark Wong <br>
 * @version : 1.0.0 2015/11/02
 *          <P>
 */
public class AESCoder {
	public static Logger logger = new BaseLogger(AESCoder.class);
	
	public static final String KEY_ALGORITHM = "AES";
	public static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	private static Map<String, Cipher> cipherEncryptMap = new HashMap<String, Cipher>();
	private static Map<String, Cipher> cipherDecryptMap = new HashMap<String, Cipher>();

	public static synchronized String decrypt(String data, String key) throws CspMsg {
		try {
			if (cipherDecryptMap.get(key) == null) {
				Key aesKey = toKey(DigestUtils.sha256(key));
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, aesKey);
				cipherDecryptMap.put(key, cipher);
			}
			return new String(((Cipher)cipherDecryptMap.get(key)).doFinal(Base64.decodeBase64(data.getBytes("UTF-8"))));
		} catch (Exception ex) {
			logger.warn(MessageFormat.format("{0}, key={1}, value={2}", new Object[]{ex.getMessage(), key, data}));
			throw new CspMsg(CommonMsgCodeEnums.ERROR_SYSTEM_E9998);
		}
	}
	
	public static synchronized String encrypt(String data, String key) throws CspMsg {
		try {
			Key aesKey = toKey(DigestUtils.sha256(key));
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			cipherEncryptMap.put(key, cipher);
			return Base64.encodeBase64URLSafeString(((Cipher)cipherEncryptMap.get(key)).doFinal(data.getBytes("UTF-8")));
		} catch (Exception ex) {
			logger.warn(MessageFormat.format("{0}, key={1}, value={2}", new Object[]{ex.getMessage(), key, data}));
			throw new CspMsg(CommonMsgCodeEnums.ERROR_SYSTEM_E9997);
		}
	}

	private static Key toKey(byte[] key) {
		SecretKey secretKey = new SecretKeySpec(key, "AES");
		return secretKey;
	}

}