package base.util;

import org.junit.Test;
import org.slf4j.Logger;

public class BaseLoggerTest {

	@Test
	public void testCustomLogFormat() {
		Logger logger = new BaseLogger(getClass());
		logger.info("test");
	}

}
