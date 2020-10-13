package com.microsoft.gbb.configclient;

import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.SubscriptionClient;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.microsoft.azure.servicebus.primitives.ServiceBusException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@SpringBootApplication
@EnableBinding
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}

}

@RestController
@RefreshScope
class HelloRestController {
	@Value("${message}")
	String message;

	@GetMapping("/hello")
	public String sayHi() {
		return message;
	}
}

@Configuration
class ServiceBusAutoConfiguration {
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "azure.servicebus",
			value = {"topic-name", "subscription-name", "subscription-receive-mode"})
	public SubscriptionClient subscriptionClient() throws ServiceBusException, InterruptedException {
		return new SubscriptionClient(new ConnectionStringBuilder("Endpoint=sb://scbus-test.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=V5YpxIGBluHDok1fUZiXtTGU5T0xzmYJKqI34OlN6eI=",
				"springcloudbus" + "/subscriptions/" + "springcloudbus-subscription"), ReceiveMode.PEEKLOCK);
	}
}
