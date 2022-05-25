package com.wsj.wx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>描述</p>
 *
 * @author wsj
 * @date 2022/5/25 21:21
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping
    public String auth(@RequestBody Map map){
        System.out.println(11122);
        return map.get("echostr").toString();
    }
}
