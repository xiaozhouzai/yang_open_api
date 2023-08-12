package com.yangapi.sdk;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;
import com.yangapi.sdk.Utils.SignUtils;
import com.yangapi.sdk.entity.Total;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Map<String,String> setHeader(String body){
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("accessKey","lcy");
        //随机数
        hashMap.put("body",body);
        //时间戳
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        //签名
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(body,"zhouzhoubao"));
        return hashMap;
    }
    public static void main(String[] args) {
        Total total = new Total();
        total.setTopNum(5);
        String json = JSONUtil.toJsonStr(total);
        String body = HttpRequest.post("http://localhost:8100/api/yangapi/top-news")
                .addHeaders(setHeader(json))
                .body(json)
                .execute().body();
        System.out.println(body);
    }
}
