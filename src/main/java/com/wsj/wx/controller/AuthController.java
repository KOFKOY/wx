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
        return "测试接口 Github Actions";
    }

    @PostMapping(consumes = {MediaType.TEXT_XML_VALUE},produces = {MediaType.TEXT_XML_VALUE})
    public WxMessage post(@RequestBody WxMessage message){
        log.info("post请求");
        String fromUserName = message.getFromUserName();
        String toUserName = message.getToUserName();
        message.setFromUserName(toUserName);
        message.setToUserName(fromUserName);
        String content = message.getContent();
        if (content.equals("ls") || content.equals("list") || content.equals("列表")) {
            message.setContent("由于个人公众号不能认证，无法定义菜单\n#VPN\n#体验\n#养生\n#干饭\n#每日笑话\n#每日一句");
            return message;
        }else if(content.equals("VPN") || content.equals("vpn")) {
            message.setContent(getVpn());
            return message;
        }else if(content.equals("每日笑话") || content.equals("笑话")) {
            message.setContent(getJoker());
            return message;
        } else if (content.equals("体验")) {
            message.setContent("账号：wsj\n密码：123456\n地址：http://www.wsjcm.top");
            return message;
        }else if(content.equals("养生")) {
            message.setContent("涌泉穴：每天睡前热水洗脚后按压\n" +
                    "关元穴：仰卧，拇指点按一分钟，每日或隔日\n" +
                    "肾俞穴：双手拇指均匀按揉，每次2-3分钟早晚各一次\n" +
                    "三阴交穴：坐姿或卧姿，右腿竖起，右手拇指按摩右腿，慢慢按压，7秒后慢慢松开，十五次后按压左腿\n" +
                    "命门穴：俯卧，双手大拇指反复按压，每次3-5分钟后交换双手按压，早晚各一次\n" +
                    "太溪穴：对侧手拇指按揉五分钟，17-19点效果最好，力度要按出酸胀加麻麻的感觉，两手握拳，手臂往后，两拇指掌关节突出部位按摩腰眼，向内做环形旋转按摩，逐渐用力，持续十分钟，早中晚各一次");
            return message;
        }
        else if(content.equals("干饭")) {
            message.setContent("一斤半鸡腿\n" +
                    "蚝油20g（李锦记）\n" +
                    "海鲜酱25g（海天）\n" +
                    "排骨酱15g（李锦记）\n" +
                    "鲍鱼汁10g（凤球唛）\n" +
                    "浓缩鸡汁5g（家乐）\n" +
                    "400—500ml水\n" +
                    "生抽15—25g（李锦记）\n" +
                    "老抽5克（李锦记）");
            return message;
        }else if(content.equals("每日一句") || content.equals("一句")) {
            message.setContent(dailyWord());
            return message;
        }
        message.setContent("未知命令,查看帮助请输入ls或list或列表");
        return message;
    }

    private String getVpn() {
        String url ="https://ekey.zielsmart.com/ekey/feishu-random-code";
        String test = "601184607\n" +
                "600797996\n" +
                "600279802\n" +
                "600280145\n" +
                "600719088\n" +
                "600279855\n" +
                "600621983";
        Map<String,String> map = new HashMap<>();
        map.put("601184607","62019");
        map.put("600797996","61835");
        map.put("600279802","61385");
        map.put("600280145","60281");
        map.put("600719088","61761");
        map.put("600279855","60759");
        map.put("600621983","61811");
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
            return code + "\t\t"+body.getOrDefault("data","获取密码失败 " + body.get("message")).toString();
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

    private String dailyWord(){
        String url = "https://www.mxnzp.com/api/daily_word/recommend?count=1&app_id=nnjn1jimprknskfr&app_secret=a2VqRE1OTlVqcXNieCtycUtsdWJ5dz09";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        JSONObject jsonObject = JSONUtil.parseObj(forEntity.getBody());
        if (jsonObject.getInt("code") == 1) {
            String str = jsonObject.getJSONArray("data").getJSONObject(0).getStr("content");
            return str;
        }
        return "获取失败";
    }

}
