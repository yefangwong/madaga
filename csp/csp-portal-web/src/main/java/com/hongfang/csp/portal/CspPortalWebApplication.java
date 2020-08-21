package com.hongfang.csp.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@ServletComponentScan
@MapperScan({"com.hongfang.**.mapper", "com.example.**.mapper"})
@SpringBootApplication(scanBasePackages = {"com.hongfang"})
public class CspPortalWebApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CspPortalWebApplication.class, args);
	}

}
