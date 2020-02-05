package com.anuj.qrscanner;


import com.anuj.qrscanner.config.AppProperties;
import com.anuj.qrscanner.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.anuj.qrscanner"}, exclude = {SecurityAutoConfiguration.class})
@EnableConfigurationProperties({AppProperties.class, FileStorageProperties.class})
@EnableScheduling
public class QrScannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(QrScannerApplication.class, args);
	}

}
