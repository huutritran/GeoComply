package com.example.geocomply.data.model;

import java.util.List;

public class ExtractInformation {
    private final List<Link> links;
    private final List<String> mentions;

    public ExtractInformation(List<Link> links, List<String> mentions) {
        this.links = links;
        this.mentions = mentions;
    }
}
