package com.depromeet.wepet.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "google.api")
public class GoogleConfig {
    private String key;
    private String url;
}
