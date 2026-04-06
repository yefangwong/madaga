package com.hongfang.csp.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "madaga.home=${user.dir}/../..")
class CspPortalWebApplicationTests {

	@Test
	void contextLoads() {
	}

}
