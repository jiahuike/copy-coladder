package com.example.coladder.pojo.apiPojo2;

import lombok.Data;

import java.util.List;


@Data
public class ApiResponse2 {
    private Integer code;
    private String message;
    private String sid;
    private List<choice> choices;
    private usage usage;

    @Data
    public static class choice{
        private message message;
        private int index;
    }
}

