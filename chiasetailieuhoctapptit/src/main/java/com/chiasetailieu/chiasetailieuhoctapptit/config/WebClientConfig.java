package com.chiasetailieu.chiasetailieuhoctapptit.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private AIServiceConfig aiServiceConfig;

    @Bean
    public WebClient aiServiceClient(){
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000).responseTimeout(java.time.Duration.ofMinutes(10))
            .doOnConnected(conn ->
                conn.addHandlerLast(new ReadTimeoutHandler(600, TimeUnit.SECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(600, TimeUnit.SECONDS)));

        String url = aiServiceConfig.getServiceUrl();
        return WebClient.builder()
            .baseUrl(url)
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}
