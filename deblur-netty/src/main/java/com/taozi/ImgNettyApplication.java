package com.taozi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yizhou
 * @date 2022/6/16 20:40
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ImgNettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgNettyApplication.class, args);
    }

}