package com.hongfang.csp.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.hongfang", "com.dhf"})
@ImportResource("classpath:applicationContext.xml")
public class CspPortalWebApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CspPortalWebApplication.class, args);
	}

}
