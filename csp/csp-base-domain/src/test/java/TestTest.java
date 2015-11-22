
import java.text.MessageFormat;

import org.junit.Test;
import org.slf4j.Logger;

import base.util.BaseLogger;
import base.util.EncryptUtil;
public class TestTest {
	private static Logger logger = new BaseLogger(TestTest.class);

	@Test
	public void test() {
		String cardNum = "1234567890111";
		//test encrypt
		logger.info(MessageFormat.format("card num encode result {0}", EncryptUtil.encrypt(cardNum)));
		
		//test decrypt
		logger.info(MessageFormat.format("card num decode result {0}", EncryptUtil.decrypt("41JvM5-x3tQaZl9ZpU7LpQ")));
	}
}
