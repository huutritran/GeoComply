package com.example.geocomply.data.model;

import java.util.Objects;

public class Link {
    private final String url;
    private final String title;

    public Link(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return url.equals(link.url) && title.equals(link.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, title);
    }

    @Override
    public String toString() {
        return "Link{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
