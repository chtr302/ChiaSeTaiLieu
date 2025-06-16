package com.chiasetailieu.chiasetailieuhoctapptit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ai.service")
public class AIServiceConfig {
    private String serviceUrl = "http://localhost:8000";

    public String getServiceUrl(){
        return serviceUrl;
    }
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}
