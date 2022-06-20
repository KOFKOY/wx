package com.wsj.wx.controller;

import com.wsj.wx.vo.WxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = {MediaType.TEXT_XML_VALUE},produces = {MediaType.TEXT_XML_VALUE})
    public WxMessage post(@RequestBody WxMessage message){
        log.info("post接口调用");
        String fromUserName = message.getFromUserName();
        String toUserName = message.getToUserName();
        message.setFromUserName(toUserName);
        message.setToUserName(fromUserName);
        message.setContent(message.getFromUserName() + " hello world");
        log.info(message.toString());
        return message;
    }
}
