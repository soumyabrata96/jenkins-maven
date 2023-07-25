package com.multithreading.demo.controller;

import com.multithreading.demo.entity.TopStories;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
public class TestController {
    @GetMapping("/test/news")
    public Set<TopStories> getNews(){
        Set<TopStories> topStoriesList=new HashSet<>();
        Map news=new RestTemplate().getForObject("https://api.nytimes.com/svc/topstories/v2/home.json?api-key=NQfFfMD9rdrN7vRvBdUnm1nharocQM20", Map.class);
        JSONObject jsonObject=new JSONObject(news);
        JSONArray jsonArray=jsonObject.getJSONArray("results");
        for(int i=0;i<jsonArray.length();i++){
            TopStories topStories=new TopStories();
            String section=jsonArray.getJSONObject(i).getString("section");
            topStories.setSection(section);
            topStories.setTitle(jsonArray.getJSONObject(i).getString("title"));
                for (int j = i + 1; j < jsonArray.length(); j++) {
                    if (section.equals(jsonArray.getJSONObject(j).getString("section"))) {
                        topStories.setTitle(jsonArray.getJSONObject(j).getString("title"));
                    }
                }
                topStoriesList.add(topStories);
            }
        return topStoriesList;
    }
}
