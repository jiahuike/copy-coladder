package com.example.coladder.pojo;

//响应类

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private int code; // 0成功 1失败
    private String message;
    private T data;

    //带响应数据的返回成功结果
    public static <E> Result<E> success(String message,E data) {
        return new Result<E>(0, message, data);
    }

    //快速返回结果
    public static  Result success() {
        return new Result<>(0, "success", null);
    }
    public static  Result error(String message) {
        return new Result<>(1, message, null);
    }
}
