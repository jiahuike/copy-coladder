package com.example.coladder.pojo.apiPojo;

import lombok.Data;

import java.util.List;


@Data
public class ApiResponse {
    private Integer code;
    private String message;
    private String sid;
    private String id;
    private Integer created;
    private List<choice> choices;

    @Data
    public static class choice{
        private delta delta;
        private int index;
    }
}

