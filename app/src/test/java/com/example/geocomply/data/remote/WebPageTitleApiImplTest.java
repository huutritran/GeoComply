package com.example.geocomply.data.remote;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.example.geocomply.data.model.Link;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class WebPageTitleApiImplTest {
    private WebPageTitleApi webPageTitleApi;
    private MockedStatic<Jsoup> jsoupMock;

    @BeforeEach
    void setup() {
        jsoupMock = mockStatic(Jsoup.class);
        webPageTitleApi = new WebPageTitleApiImpl();
    }

    @AfterEach
    public void cleanup() {
        jsoupMock.close();
    }

    @Test
    void should_return_correct_title_when_read_title_success() throws IOException {
        /* arrange */
        Map<String, String> titleWithUrl = new HashMap<>();
        titleWithUrl.put("google title", "https://google.com");
        titleWithUrl.put("yahoo title", "https://yahoo.com");
        makeReadTitleSuccess(titleWithUrl);

        List<String> urls = new ArrayList<>(titleWithUrl.values());
        List<Link> expected = titleWithUrl.entrySet()
                .stream()
                .map(web -> new Link(web.getValue(), web.getKey()))
                .collect(Collectors.toList());

        /* act */
        List<Link> links = webPageTitleApi.getLink(urls);

        /* assert */
        Assertions.assertEquals(expected, links);
    }

    @Test
    void should_return_Not_Found_Title_when_read_title_fail() throws IOException {
        /* arrange */
        List<String> urls = Arrays.asList("https://google.com", "https://yahoo.com");
        makeReadTitleFail();
        List<Link> expected = urls.stream()
                .map(it -> new Link(it, "Not Found Title"))
                .collect(Collectors.toList());

        /* act */
        List<Link> links = webPageTitleApi.getLink(urls);

        /* assert */
        Assertions.assertEquals(expected, links);
    }

    private void makeReadTitleFail() throws IOException {
        final HttpConnection mockConnection = Mockito.mock(HttpConnection.class);
        when(mockConnection.get()).thenThrow(new IOException("Dummy Exception"));

        jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
    }

    private void makeReadTitleSuccess(Map<String, String> titleWithUrl) throws IOException {
        List<Document> mockDocuments = new ArrayList<>();
        titleWithUrl.forEach((key, value) -> {
            Document temp = new Document(value);
            temp.title(key);
            mockDocuments.add(temp);
        });

        final HttpConnection mockConnection = Mockito.mock(HttpConnection.class);
        when(mockConnection.get()).thenAnswer(new Answer<Object>() {
            private int count = 0;

            @Override
            public Object answer(InvocationOnMock invocation) {
                return mockDocuments.get(count++);
            }
        });

        jsoupMock.when(() -> Jsoup.connect(anyString())).thenReturn(mockConnection);
    }
}