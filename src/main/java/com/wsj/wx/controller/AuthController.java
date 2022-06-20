package com.wsj.wx.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>描述</p>
 *
 * @author wsj
 * @date 2022/5/25 21:21
 */
@RestController
@Slf4j
public class AuthController {

    @GetMapping
    public String auth(@RequestParam(required = false) Map map){
        log.info("认证");
        return map.get("echostr").toString();
    }

    @GetMapping("/test")
    public String test(){
        return "测试接口Github Actions";
    }

    @PostMapping
    public String post(@RequestBody(required = false) HashMap map){
        log.info("post接口调用");
        map.keySet().stream().forEach(key -> {
            log.info("key:{},value:{}",key,map.get(key));
        });
        return "接口调用";
    }
}
