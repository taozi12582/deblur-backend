package com.taozi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author yizhou
 * @date 2022/6/16 20:40
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ImgNettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgNettyApplication.class, args);
    }

}