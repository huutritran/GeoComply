package com.example.geocomply.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import android.os.Handler;

import com.example.geocomply.data.CommentDataExtraction;
import com.example.geocomply.data.model.ExtractInformation;
import com.example.geocomply.data.model.Link;
import com.example.geocomply.data.remote.WebPageTitleApi;
import com.example.geocomply.test_utils.InstantTaskExecutorRule;
import com.google.gson.Gson;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(InstantTaskExecutorRule.class)
class ProcessUserCommentImplTest {
    private ProcessUserComment processUserComment;
    private WebPageTitleApi mockPageTitleApi;
    private CommentDataExtraction mockDataExtraction;
    private Handler mockHandler;
    private Gson gson;

    @BeforeEach
    void setup() {
        gson = new Gson();
        mockDataExtraction = new CommentDataExtraction();
        mockHandler = Mockito.mock(Handler.class);
        mockPageTitleApi = Mockito.mock(WebPageTitleApi.class);
        processUserComment = new ProcessUserCommentImpl(
                mockDataExtraction,
                mockPageTitleApi,
                mockHandler,
                gson
        );
    }


    @Test
    void should_return_mentions_and_empty_links_json() {
        /* arrange */
        makeDataProcessSuccess("dummyTitle");
        AtomicReference<String> actual = new AtomicReference<>("");
        List<String> mockMentions = Collections.singletonList("tagName");
        String expected = gson.toJson(new ExtractInformation(
                Collections.emptyList(),
                mockMentions
        ));

        /* act */
        processUserComment.process("this is sentence with @tagName", actual::set);
        pauseSeconds(2);

        /* assert */
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void should_return_links_and_empty_mentions_json() {
        /* arrange */
        makeDataProcessSuccess("dummyTitle");
        AtomicReference<String> actual = new AtomicReference<>("");
        List<Link> mockLinkList = Collections.singletonList(new Link("http://dummylink.com", "dummyTitle"));
        String expected = gson.toJson(new ExtractInformation(
                mockLinkList,
                Collections.emptyList()
        ));

        /* act */
        processUserComment.process("this is link http://dummylink.com", actual::set);
        pauseSeconds(2);

        /* assert */
        Assertions.assertEquals(expected, actual.get());
    }

    @Test
    void should_return_empty_links_and_empty_mention_json() {
        /* arrange */
        makeDataProcessSuccess("dummyWebTitle");
        AtomicReference<String> actual = new AtomicReference<>("");
        String expected = gson.toJson(new ExtractInformation(Collections.emptyList(), Collections.emptyList()));

        /* act */
        processUserComment.process("this is sentence don't have link and mention also", actual::set);
        pauseSeconds(2);

        /* assert */
        Assertions.assertEquals(expected, actual.get());
    }

    private void makeDataProcessSuccess(String dummyWebTitle) {
        when(mockPageTitleApi.getLink(any())).thenAnswer((Answer<List<Link>>) invocation -> {
            List<String> urls = invocation.getArgument(0);
            return urls.stream()
                    .map(url -> new Link(url, dummyWebTitle))
                    .collect(Collectors.toList());
        });

        when(mockHandler.post(any())).thenAnswer(invocation -> {
            Runnable runnable = invocation.getArgument(0);
            runnable.run();
            return true;
        });
    }

    private void pauseSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}