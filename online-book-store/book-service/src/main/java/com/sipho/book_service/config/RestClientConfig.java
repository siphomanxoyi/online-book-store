package com.sipho.book_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;



@Configuration
public class RestClientConfig {
    @Bean
    public RestClient getRestClient(@Value("${service.host}") String host){
        RestClient restClient = RestClient.create(host);
        return restClient;
    }
}
