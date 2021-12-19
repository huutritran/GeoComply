package com.example.geocomply.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocomply.repository.ProcessUserComment;

public class MainViewModelFactory implements ViewModelProvider.Factory {
    private final ProcessUserComment processUserComment;

    public MainViewModelFactory(ProcessUserComment processUserComment) {
        this.processUserComment = processUserComment;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(this.processUserComment);
        }
        throw new IllegalArgumentException("ViewModel Not Found");
    }
}
