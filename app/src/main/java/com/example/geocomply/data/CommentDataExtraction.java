package com.example.geocomply.data;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommentDataExtraction {
    public static final String MENTION_REGEX = "\\B@\\w+";
    public static final String LINK_REGEX = "(http|https):\\/\\/([\\w_-]+(?:(?:\\.[\\w_-]+)+))" +
            "([\\w" +
            ".,@?^=%&:\\/~+#-]*[\\w@?^=%&\\/~+#-])";

    private List<String> getMatches(String input, String regex) {
        List<String> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            results.add(matcher.group());
        }
        return results;
    }

    public List<String> getMentions(String input) {
        return getMatches(input, MENTION_REGEX)
                .stream()
                .map(value -> value.replace("@", ""))
                .collect(Collectors.toList());
    }

    public List<String> getLinks(String input) {
        return getMatches(input, LINK_REGEX);
    }
}
