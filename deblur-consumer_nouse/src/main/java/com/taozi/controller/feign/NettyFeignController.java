package com.taozi.controller.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@FeignClient(value = "deblur-nettyClient-es")
@RestController
public interface NettyFeignController {

    @GetMapping("/deblur/doDeblur")
    String doDeblur(@RequestParam String imgPath);

}
