package com.example.coladder.service.impl;

import com.example.coladder.pojo.Interactive;
import com.example.coladder.pojo.Result;
import com.example.coladder.pojo.User;
import com.example.coladder.pojo.apiPojo.ApiRequest;
import com.example.coladder.mapper.InteractiveMapper;
import com.example.coladder.pojo.apiPojo.ApiResponse;
import com.example.coladder.pojo.apiPojo.data;
import com.example.coladder.pojo.apiPojo.delta;
import com.example.coladder.pojo.apiPojo2.ApiRequest2;
import com.example.coladder.pojo.apiPojo2.ApiResponse2;
import com.example.coladder.service.ApiService;
import com.example.coladder.untils.ThreadLocalUntil;
import com.google.gson.Gson;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import okhttp3.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Service
public class ApiServiceImpl implements ApiService {

    @Value("${model.api.url}")
    private String apiUrl;

    @Value("${model.api.key}")
    private String apiKey; // 假设需要 API Key
    private final OkHttpClient httpClient = new OkHttpClient();

    private final ApiRequest apiRequest = new ApiRequest();
    private final ApiRequest2 apiRequest2 = new ApiRequest2();

    @Getter
    @Autowired
    private OkHttpClient okHttpClient;

    @Autowired
    private InteractiveMapper interactiveMapper;


    /**
     * 流式处理
     * @param sseEmitter
     * @param input
     * @throws IOException
     */
    @Override
    public void callModel(SseEmitter sseEmitter,String input) throws IOException {
        apiRequest.setInput(input);
        //System.out.println(apiRequest.getInput());
        RequestBody body = RequestBody.create(apiRequest.getInput(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        CountDownLatch latch = new CountDownLatch(1);

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
                latch.countDown();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try(ResponseBody responseBody = response.body();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(responseBody.byteStream()))) {
                        String line;
                        // 流式读取数据
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            // 处理每一行数据
                             // 这里可以做其他处理，例如解析 JSON
                            if (!line.contains("{"))continue;
                            System.out.println(line);
                            line = line.substring(line.indexOf("{"));
                            ApiResponse apiResponse = new Gson().fromJson(line, ApiResponse.class);
                            for(ApiResponse.choice item : apiResponse.getChoices()){
                                sb.append(item.getDelta().getContent());
                                sseEmitter.send(item.getDelta().getContent());
                            }
                        }
                        Map<String,Object> map = ThreadLocalUntil.get();
                        Integer id = (Integer) map.get("id");
                        interactiveMapper.addMessage(input,sb.toString(),id);
                }catch (IOException e) {
                    sseEmitter.completeWithError(e);
                }finally {
                    latch.countDown();
                    sseEmitter.complete();
                }
            }

        });
        try {
            latch.await();
            sseEmitter.complete();
        }catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        /*try (Response response = httpClient.newCall(request).execute()) {

            //
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String[] responseBody = response.body().string().split("\n");
            System.out.println(responseBody[0]);
            ApiResponse apiResponse = new Gson().fromJson(responseBody[0], data.class).getApiResponse();

            String answer = apiResponse.getChoices().get(0).getDelta().getContent();
            Map<String,Object> map = ThreadLocalUntil.get();
            Integer id = (Integer) map.get("id");
            interactiveMapper.addMessage(input,answer,id);
            return answer;
        }*/
    }

    @Override
    public Result callmodel2(String question) throws IOException {
        apiRequest2.setInput(question);
        //System.out.println(apiRequest.getInput());
        RequestBody body = RequestBody.create(apiRequest2.getInput(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {

            //
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            String[] responseBody = response.body().string().split("\n");
            System.out.println(responseBody[0]);
            ApiResponse2 apiResponse = new Gson().fromJson(responseBody[0], ApiResponse2.class);

            String answer = apiResponse.getChoices().get(0).getMessage().getContent();
            Map<String,Object> map = ThreadLocalUntil.get();
            Integer id = (Integer) map.get("id");
            interactiveMapper.addMessage(question,answer,id);
            return Result.success("成功",answer);
        }
    }


    @Override
    public Interactive[] selectByUserID(Integer id) {
        return interactiveMapper.selectByUserID(id);
    }

    @Override
    public Result getAnswer(String question){
        apiRequest2.setInput(question);
        //System.out.println(apiRequest.getInput());
        RequestBody body = RequestBody.create(apiRequest2.getInput(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()){
                System.out.println("response.message()");
                return Result.error("请求失败");
            }
            String[] responseBody = response.body().string().split("\n");
            System.out.println(responseBody[0]);
            ApiResponse2 apiResponse = new Gson().fromJson(responseBody[0], ApiResponse2.class);

            String answer = apiResponse.getChoices().get(0).getMessage().getContent();
            return Result.success("成功",answer);
        }catch (IOException e) {
            System.out.println(e.getMessage());
            return Result.error("请求失败");
        }

    }

}
