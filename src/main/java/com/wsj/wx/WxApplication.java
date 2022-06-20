package com.wsj.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WxApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxApplication.class, args);
    }

    @Bean
    public RestTemplate initRestTemplate(){
        return new RestTemplate();
    }
}
