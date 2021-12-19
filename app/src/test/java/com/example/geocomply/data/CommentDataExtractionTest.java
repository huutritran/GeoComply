package com.example.geocomply.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommentDataExtractionTest {
    private CommentDataExtraction commentDataExtraction;

    @BeforeEach
    public void setup() {
        commentDataExtraction = new CommentDataExtraction();
    }

    @Nested
    class MentionsExtraction {
        @Test
        void should_return_empty_list_when_mentions_not_found() {
            /* arrange */
            String input = "this sentence not contain mention";

            /* act */
            List<String> results = commentDataExtraction.getMentions(input);

            /* assert */
            Assertions.assertEquals(Collections.emptyList(), results);
        }

        @Test
        void mention_should_begin_with_atSign() {
            /* arrange */
            String input = "@john";
            List<String> expected = Collections.singletonList("john");

            /* act */
            List<String> results = commentDataExtraction.getMentions(input);

            /* assert */
            Assertions.assertEquals(expected, results);
        }

        @Test
        void before_mention_atSign_should_not_contain_any_character() {
            /* arrange */
            String input = "z@john";

            /* act */
            List<String> results = commentDataExtraction.getMentions(input);

            /* assert */
            Assertions.assertEquals(Collections.emptyList(), results);
        }

        @Test
        void mention_should_exclude_non_work_character_at_the_end() {
            /* arrange */
            String input = "@john?";
            List<String> expected = Collections.singletonList("john");

            /* act */
            List<String> results = commentDataExtraction.getMentions(input);

            /* assert */
            Assertions.assertEquals(expected, results);
        }

        @Test
        void should_return_mention_list_correctly() {
            /* arrange */
            String input = "this @john and this is @adam";
            List<String> expected = Arrays.asList("john", "adam");

            /* act */
            List<String> results = commentDataExtraction.getMentions(input);

            /* assert */
            Assertions.assertEquals(expected, results);
        }
    }

    @Nested
    class LinksExtraction {

        @Test
        void should_return_empty_list_when_links_not_found() {
            /* arrange */
            String input = "This sentence not include any links";

            /* act */
            List<String> results = commentDataExtraction.getLinks(input);

            /* assert */
            Assertions.assertEquals(Collections.emptyList(), results);
        }

        @Test
        void should_extract_link_correctly() {
            /* arrange */
            String input = "this sentence a link: http://google.com and https://google.com";
            List<String> expected = Arrays.asList("http://google.com", "https://google.com");

            /* act */
            List<String> results = commentDataExtraction.getLinks(input);

            /* assert */
            Assertions.assertEquals(expected, results);
        }
    }


}