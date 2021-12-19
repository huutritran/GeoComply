package com.example.geocomply.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.geocomply.repository.ProcessUserComment;

public class MainViewModel extends ViewModel {
    private final ProcessUserComment processUserComment;
    private MutableLiveData<String> mJsonResult = new MutableLiveData<>();
    public LiveData<String> jsonResult = mJsonResult;

    private MutableLiveData<Boolean> mLoading = new MutableLiveData<>();
    public LiveData<Boolean> loading = mLoading;

    private MutableLiveData<Boolean> mError = new MutableLiveData<>();
    public MutableLiveData<Boolean> error = mError;

    MainViewModel(ProcessUserComment processUserComment) {
        this.processUserComment = processUserComment;
    }

    public void extractData(String input) {
        if (input.isEmpty()) {
            mError.setValue(true);
        } else {
            mLoading.setValue(true);
            processUserComment.process(input, result -> {
                mLoading.setValue(false);
                mJsonResult.setValue(result);
            });
        }
    }
}
