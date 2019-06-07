package com.dallotech;

import com.dallotech.config.AppProperties;
import com.dallotech.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.dallotech"}, exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties({AppProperties.class, FileStorageProperties.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
