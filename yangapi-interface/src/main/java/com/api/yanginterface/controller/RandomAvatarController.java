package com.api.yanginterface.controller;

import com.yangapi.sdk.client.YangApiClient;
import com.yangapi.sdk.entity.Total;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.api.yanginterface.constent.AvatarConstent.URL_PREFIX;
import static com.api.yanginterface.constent.AvatarConstent.WB_TOP_NEWS_URL_PREFIX;

@RestController
@RequestMapping("/yangapi")
public class RandomAvatarController {
    private final List<String> avatars = Arrays.asList(
            "avatar1.png", "avatar2.png", "avatar3.png", "avatar4.png", "avatar5.png"
            , "avatar6.png", "avatar7.png", "avatar8.png", "avatar9.png"
    );

    /**
     * 获取随机头像链接
     *
     * @return
     */
    //localhost:8100/api/yangapi/random-avatar
    @GetMapping("/random-avatar")
    public String getRandomAvatar(HttpServletRequest request) {
        System.out.println(request.getHeader("gateway-env"));
        Random random = new Random();
        String randomAvatar = avatars.get(random.nextInt(avatars.size()));
        return URL_PREFIX + randomAvatar;
    }
    //todo 输入需求生成文章的接口

    /**
     * 获取微博热搜榜的内容和链接
     * @param total 获取数据对象
     * @return List<String> 详情
     * @throws IOException 异常
     */
    //localhost:8100/api/yangapi/top-news
    @PostMapping("/top-news")
    public List<String> getNewsRankingList(@RequestBody Total total) throws IOException {
        if (total == null) {
            return null;
        }
        Document doc = Jsoup.connect("https://tophub.today/n/KqndgxeLl9").get();
        Element body = doc.body();
        Elements elementsByClass = body.getElementsByClass("al");
        List<String> topList = new ArrayList<>();
        Integer topNum = total.getTopNum();
        for (int i = 0; i < topNum; i++) {
            String text = elementsByClass.get(i).text();
            Element link = elementsByClass.get(i).selectFirst("a[href]");
            String hrefEnd = link.attr("href");
            String links = WB_TOP_NEWS_URL_PREFIX + hrefEnd;
            String top = "热搜标题: " + text + ",  详情页面链接: " + links;
            topList.add(top);
        }
        return topList;
    }

    @GetMapping("/fallback")
    public String fallbackService() {
        return "Service is currently unavailable. Please try again later.";
    }
}