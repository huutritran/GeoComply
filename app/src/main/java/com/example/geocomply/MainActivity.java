package com.example.geocomply;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.geocomply.data.remote.WebPageTitleApi;
import com.example.geocomply.data.remote.WebPageTitleApiImpl;
import com.example.geocomply.repository.ProcessUserComment;
import com.example.geocomply.repository.ProcessUserCommentImpl;
import com.example.geocomply.data.CommentDataExtraction;
import com.example.geocomply.viewmodels.MainViewModel;
import com.example.geocomply.viewmodels.MainViewModelFactory;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout container;
    private ProgressBar progressBar;
    private TextView tvJsonResult;
    private EditText etInput;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewModel();
        setupControls();
        subscribeData();
    }

    private void subscribeData() {
        viewModel.jsonResult.observe(this, json -> tvJsonResult.setText(json));

        viewModel.loading.observe(this, this::setLoadingState);

        viewModel.error.observe(this, this::setErrorState);
    }

    private void setErrorState(boolean isError) {
        if (isError) {
            String error = getString(R.string.error_user_input_blank);
            Snackbar snackbar = Snackbar.make(container, error, BaseTransientBottomBar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            snackbar.getView().setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

    private void setLoadingState(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setupControls() {
        container = findViewById(R.id.container);
        Button btnProcess = findViewById(R.id.btnProcess);
        progressBar = findViewById(R.id.progressBar);
        tvJsonResult = findViewById(R.id.tvJsonResult);
        etInput = findViewById(R.id.etInput);

        btnProcess.setOnClickListener(view -> {
            String input = etInput.getText().toString();
            viewModel.extractData(input);
        });
    }

    private void setupViewModel() {
        ProcessUserComment processUserComment = provideProcessUserComment();
        ViewModelProvider.Factory factory = new MainViewModelFactory(processUserComment);
        viewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);
    }

    private ProcessUserComment provideProcessUserComment() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        Handler uiHandler = new Handler(Looper.getMainLooper());
        CommentDataExtraction dataExtraction = new CommentDataExtraction();
        WebPageTitleApi pageTitleApi = new WebPageTitleApiImpl();

        return new ProcessUserCommentImpl(
                dataExtraction,
                pageTitleApi,
                uiHandler,
                gson
        );
    }

}