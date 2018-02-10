package com.github.luecy1.androidstudyapp.api;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.github.luecy1.androidstudyapp.util.LiveDataCallAdapterFactory;
import com.github.luecy1.androidstudyapp.vo.User;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.github.luecy1.androidstudyapp.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by you on 2018/02/10.
 */

// TODO
public class GithubServiceTest {

    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private GithubService service;

    private MockWebServer mockWebServer;

    public void createService() throws IOException {
        mockWebServer = new MockWebServer();
        service = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                .create(GithubService.class);
    }

    public void stopService() throws IOException {
        mockWebServer.shutdown();
    }

    public void getUser() throws IOException, InterruptedException {
        enqueueResponse("user-yigit.json");
        User yigit = getValue(service.getUser("yigit")).body;

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getPath(), is("users/yigit"));

        assertThat(yigit, notNullValue());
        assertThat(yigit.avatarUrl, is("https://avatars3.githubusercontent.com/u/89202?v=3"));
        assertThat(yigit.company, is("Google"));
        assertThat(yigit.blog, is("birbit.com"));
    }

    public void getRepos() throws IOException, InterruptedException {
        //todo
    }

    public void getContributors() throws IOException, InterruptedException {
        //todo
    }

    public void search() throws IOException, InterruptedException {
        //todo
    }

    private void enqueueResponse(String filename) throws IOException {
        enqueueResponse(filename, Collections.emptyMap());
    }

    private void enqueueResponse(String filename, Map<String, String> headers) throws IOException {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("api-response/" + filename);
        BufferedSource source = Okio.buffer(Okio.source(inputStream));
        MockResponse mockResponse = new MockResponse();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            mockResponse.addHeader(header.getKey(), header.getValue());
        }
        mockWebServer.enqueue(mockResponse
        .setBody(source.readString(StandardCharsets.UTF_8)));
    }
}
