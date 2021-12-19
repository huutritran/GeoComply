package com.example.geocomply.repository;

import android.os.Handler;

import androidx.annotation.NonNull;

import com.example.geocomply.data.CommentDataExtraction;
import com.example.geocomply.data.model.ExtractInformation;
import com.example.geocomply.data.remote.WebPageTitleApi;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProcessUserCommentImpl implements ProcessUserComment {
    private final CommentDataExtraction dataExtraction;
    private final WebPageTitleApi pageTitleApi;
    private final Handler uiHandler;
    private final Gson gson;

    public ProcessUserCommentImpl(
            @NonNull CommentDataExtraction dataExtraction,
            @NonNull WebPageTitleApi pageTitleApi,
            @NonNull Handler uiHandler,
            @NonNull Gson gson
    ) {
        this.dataExtraction = dataExtraction;
        this.pageTitleApi = pageTitleApi;
        this.uiHandler = uiHandler;
        this.gson = gson;
    }

    @Override
    public void process(String input, ProcessCommentCallback callback) {
        List<String> mentions = dataExtraction.getMentions(input);
        List<String> urls = dataExtraction.getLinks(input);

        CompletableFuture
                .supplyAsync(() -> pageTitleApi.getLink(urls))
                .thenAccept(links -> {
                    ExtractInformation info = new ExtractInformation(links, mentions);
                    String result = gson.toJson(info);
                    uiHandler.post(() -> callback.onProcessSuccess(result));
                });
    }
}
