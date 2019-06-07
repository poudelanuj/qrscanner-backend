package com.dallotech.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FileStorageProperties {
    private String dirKYC;
}
