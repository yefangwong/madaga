package base.util;
import java.text.MessageFormat;

import com.dehongfang.csp.base.util.AESCoder;
import org.junit.BeforeClass;
import org.junit.Test;

import common.lang.exception.CspMsg;

public class AESCoderTest {
	private static String cardnum = "";
	private static String key = "";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		cardnum = "1234567812345678";
		key = "jesusislord";
	}
	
	@Test
	public void testEncrypt() throws CspMsg {
		cardnum = AESCoder.encrypt(cardnum, key);
		System.out.println(MessageFormat.format("Encrypt String : {0}", cardnum));
	}
	
	@Test
	public void testDecrypt() throws CspMsg {
		cardnum = "Wuioer9at1D3gvN_Hk4-wOgmBB5Wpbyw8HHlHM5Wemk";
		System.out.println(MessageFormat.format("Decrypt Result : {0}", AESCoder.decrypt(cardnum, key)));
	}

}
