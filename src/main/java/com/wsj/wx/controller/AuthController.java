package com.wsj.wx.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.wsj.wx.vo.WxMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>描述</p>
 *
 * @author wsj
 * @date 2022/5/25 21:21
 */
@RestController
@Slf4j
public class AuthController {
    @Resource
    RestTemplate restTemplate;

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
        String content = message.getContent();
        if (content.equals("ls") || content.equals("list") || content.equals("列表")) {
            message.setContent("由于个人公众号不能认证，无法定义菜单\n#VPN\n#每日笑话");
            return message;
        }else if(content.equals("VPN") || content.equals("vpn")) {
            message.setContent(getVpn());
            return message;
        }else if(content.equals("每日笑话") || content.equals("笑话")) {
            message.setContent(getJoker());
            return message;
        }
        message.setContent("未知命令,查看帮助请输入ls或list或列表");
        return message;
    }

    private String getVpn() {
        String url ="https://ekey.zielsmart.com/ekey/feishu-random-code";
        String test = "600417019\n" +
                "600417024\n" +
                "600416435\n" +
                "600416460\n" +
                "600445520\n" +
                "602205939\n" +
                "600495824";
        Map<String,String> map = new HashMap<>();
        map.put("600417019","61451");
        map.put("600417024","61452");
        map.put("600416435","61453");
        map.put("600416460","61454");
        map.put("600495824","61549");
        map.put("602205939","62242");
        String[] split = test.split("\n");
        //获取随机数
        int random = ThreadLocalRandom.current().nextInt(0, split.length);
        HttpHeaders headers = new HttpHeaders();
        //必须设置请求的推荐人(来源)Referer
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("User-Agent", "User-Agent,Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1");
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        String feishuId = split[random];
        postParameters.add("code", feishuId);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map<String,Object> body = exchange.getBody();
        if (body == null || body.get("success").toString().equals("false")) {
            return "获取失败，此用户已失效 请重新获取";
        }else{
            String code = map.get(feishuId);
            return code + "\n"+body.getOrDefault("data","获取密码失败 " + body.get("message")).toString();
        }
    }

    private String getJoker() {
        String url2 = "https://www.mxnzp.com/api/jokes/list/random?app_id=nnjn1jimprknskfr&app_secret=a2VqRE1OTlVqcXNieCtycUtsdWJ5dz09";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url2, String.class);
        JSONObject jsonObject = JSONUtil.parseObj(forEntity.getBody());
        JSONArray array = jsonObject.getJSONArray("data");
        JSONObject object = array.getJSONObject(0);
        return object.getStr("content");
    }

}
