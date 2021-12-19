package com.example.geocomply.repository;

public interface ProcessUserComment {
    interface ProcessCommentCallback {
        void onProcessSuccess(String result);
    }

    void process(String input, ProcessCommentCallback callback);
}
