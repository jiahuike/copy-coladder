package com.example.coladder.controller;

import com.example.coladder.pojo.Interactive;
import com.example.coladder.pojo.Result;
import com.example.coladder.pojo.User;
import com.example.coladder.service.ApiService;
import com.example.coladder.untils.ThreadLocalUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/model")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @PostMapping("/call-model2")
    public Result callModel(String input) {
        if (input == null || input.isEmpty()) {
            return Result.error("提问不能为空");
        }
        try {
            return apiService.callmodel2(input);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/call-model")
    public SseEmitter getAnswers(String input) {
        SseEmitter sseEmitter = new SseEmitter();
        System.out.println(12312312);
        new Thread(() -> {
            try {
                // 这里可以使用 OkHttp 流式获取数据
                apiService.callModel(sseEmitter,input);
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        }).start();
        return sseEmitter;
    }

    @GetMapping("/getCharts")
    public Result allCharts(){
        Map<String,Object> map = ThreadLocalUntil.get();
        Integer createUserId = (Integer) map.get("id");
        Interactive[] interactive = apiService.selectByUserID(createUserId);
        return Result.success("查询成功",interactive);
    }

    @PostMapping("/getAnswer")
    public Result getAnswer(String input) {

        //System.out.println(input.replace("\r\n",""));
        //TODO 浏览器返回的数据中，换行符可能是\n或 \r\n，在传给大模型时，需要去掉相关内容
        return apiService.getAnswer(input.replace("\n",";").replace("\r",";"));
    }


}
