package com.example.geocomply.data.remote;

import com.example.geocomply.data.model.Link;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebPageTitleApiImpl implements WebPageTitleApi {

    @Override
    public List<Link> getLink(List<String> urls) {
        List<Link> links = new ArrayList<>();
        urls.forEach(url -> {
            try {
                String title = Jsoup.connect(url).get().title();
                links.add(new Link(url, title));
            } catch (IOException e) {
                links.add(new Link(url, "Not Found Title"));
            }
        });
        return links;
    }
}
