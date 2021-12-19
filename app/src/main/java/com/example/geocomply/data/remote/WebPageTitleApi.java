package com.example.geocomply.data.remote;

import com.example.geocomply.data.model.Link;

import java.util.List;

public interface WebPageTitleApi {
    List<Link> getLink(List<String> urls);
}
