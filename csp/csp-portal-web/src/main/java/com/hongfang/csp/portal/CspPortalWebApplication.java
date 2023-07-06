package com.hongfang.csp.portal;

import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import lombok.Value;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

@EnableAsync
@EnableScheduling
@EnableTransactionManagement
@EnableConfigurationProperties
@ServletComponentScan
@SpringBootApplication(scanBasePackages = {"com.hongfang", "com.dhf"})
@ImportResource("classpath:applicationContext.xml")
public class CspPortalWebApplication {
    //@Value("${chatgpt.apiKey}")
    private List<String> apiKey;
    //@Value("${chatgpt.apiHost}")
    private String apiHost;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CspPortalWebApplication.class, args);
	}

	@Bean
	public OpenAiStreamClient openAiStreamClient() {
		//本地端開發需要設定代理地址
		//
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
		//!!!!!!测试或者发布到服务器千万不要配置Level == BODY!!!!
		//!!!!!!测试或者发布到服务器千万不要配置Level == BODY!!!!
		httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
		/*OkHttpClient okHttpClient = new OkHttpClient
			.Builder()
			//                .proxy(proxy)
			.addInterceptor(httpLoggingInterceptor)
			.connectTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(600, TimeUnit.SECONDS)
			.readTimeout(600, TimeUnit.SECONDS)
			.build();/*
		return OpenAiStreamClient
			.builder()
			.apiHost(apiHost)
			.apiKey(apiKey)
			//自定義key使用策略 預設隨機策略
			.keyStrategy(new KeyRandozmStrategy())
			.okHttpClient(okHttpClient)
			.build();*/
		return null;
	}

}
