package com.yangapi.sdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.yangapi.sdk.Utils.SignUtils;
import com.yangapi.sdk.entity.Total;

import java.util.HashMap;
import java.util.Map;

public class YangApiClient {
    //调用方
    private final String accessKey;
    private final String secretKey;
    public YangApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getRandomAvatar() {
        String avatarUrl = HttpUtil.get("http://localhost:8100/api/yangapi/random-avatar");
        return avatarUrl;
    };


    private Map<String,String> setHeader(String body){
        Map<String,String> hashMap = new HashMap<>();
        hashMap.put("accessKey",accessKey);
        //随机数
        hashMap.put("body",body);
        //时间戳
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        //签名
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("sign", SignUtils.getSign(body,secretKey));
        return hashMap;
    }

    public String getNewsRankingList( Total total) {
        String json = JSONUtil.toJsonStr(total);
        String body = HttpRequest.post("http://localhost:8100/api/yangapi/top-news")
                .addHeaders(setHeader(json))
                .body(json)
                .execute().body();
        return body;
    };
}
