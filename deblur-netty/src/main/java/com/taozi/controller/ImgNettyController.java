package com.taozi.controller;

import com.taozi.netty.ImgNettyClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
@RequestMapping("/deblur")
public class ImgNettyController {
    @Resource
    private ImgNettyClient imgNettyClient;

    @GetMapping("/doDeblur")
    public void nettyClient(@RequestParam String imgPath) {
        imgNettyClient.send(imgPath);
    }
}
