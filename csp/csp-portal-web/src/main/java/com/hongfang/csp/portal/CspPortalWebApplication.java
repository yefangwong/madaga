package com.hongfang.csp.portal;

import com.dehongfang.csp.base.util.EncryptUtil;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import common.lang.exception.CspMsg;
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
import org.springframework.beans.factory.annotation.Value;

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
    @Value("${chatgpt.apiKey}")
    private List<String> apiKey;
    @Value("${chatgpt.apiHost}")
    private String apiHost;

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CspPortalWebApplication.class, args);
	}

	@Bean
	public OpenAiStreamClient openAiStreamClient() throws CspMsg {
		//本地端開發需要設定代理地址
		//
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
		HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
		//!!!!!!測試或者發布到伺服器千萬不要配置Level == BODY!!!!
		//!!!!!!測試或者發布到伺服器千萬不要配置Level == BODY!!!!
		httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
		OkHttpClient okHttpClient = new OkHttpClient
			.Builder()
			//.proxy(proxy)
			.addInterceptor(httpLoggingInterceptor)
			.connectTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(600, TimeUnit.SECONDS)
			.readTimeout(600, TimeUnit.SECONDS)
			.build();
		for (int i = 0; i < apiKey.size(); i++) {
			String s = (String) EncryptUtil.decrypt(apiKey.get(i));
			apiKey.set(i, s);
		}
		return OpenAiStreamClient
			.builder()
			.apiHost(apiHost)
			.apiKey(apiKey)
			//自定義key使用策略 預設隨機策略
			.keyStrategy(new KeyRandomStrategy())
			.okHttpClient(okHttpClient)
			.build();
	}

}
