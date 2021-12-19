package com.example.geocomply.viewmodels;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import androidx.lifecycle.Observer;

import com.example.geocomply.repository.ProcessUserComment;
import com.example.geocomply.test_utils.InstantTaskExecutorRule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(InstantTaskExecutorRule.class)
class MainViewModelTest {

    private MainViewModel viewModel;
    private ProcessUserComment processUserComment;

    @BeforeEach
    public void setup() {
        processUserComment = mock(ProcessUserComment.class);
        viewModel = new MainViewModel(processUserComment);
    }

    @Test
    void should_return_error_when_input_is_empty() {
        /* arrange */
        Observer<Boolean> observer = mocObserver();
        viewModel.error.observeForever(observer);
        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);

        /* act */
        viewModel.extractData("");

        /* assert */
        verify(observer, times(1)).onChanged(argumentCaptor.capture());
        Assertions.assertTrue(argumentCaptor.getValue());
    }

    @Test
    void should_return_loading_state_correctly() {
        /* arrange */
        makeProcessUserCommentSuccess("dummyResult");
        Observer<Boolean> observer = mocObserver();
        viewModel.loading.observeForever(observer);
        ArgumentCaptor<Boolean> argumentCaptor = ArgumentCaptor.forClass(Boolean.class);


        /* act */
        viewModel.extractData("dummyInput");

        /* assert */
        List<Boolean> expected = Arrays.asList(true, false);
        verify(observer, times(2)).onChanged(argumentCaptor.capture());
        Assertions.assertEquals(expected, argumentCaptor.getAllValues());
    }

    @Test
    void should_return_data_when_process_success() {
        /* arrange */
        makeProcessUserCommentSuccess("mockResult");
        Observer<String> observer = mocObserver();
        viewModel.jsonResult.observeForever(observer);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);


        /* act */
        viewModel.extractData("dummyInput");

        /* assert */
        verify(observer, times(1)).onChanged(argumentCaptor.capture());
        Assertions.assertEquals("mockResult", argumentCaptor.getValue());
    }

    private void makeProcessUserCommentSuccess(String mockResponse) {
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((ProcessUserComment.ProcessCommentCallback) args[1]).onProcessSuccess(mockResponse);
            return null;
        }).when(processUserComment).process(anyString(),
                any(ProcessUserComment.ProcessCommentCallback.class));
    }

    @SuppressWarnings("unchecked")
    private <T> Observer<T> mocObserver() {
        return mock(Observer.class);
    }
}