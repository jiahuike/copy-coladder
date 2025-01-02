package com.example.coladder.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    private Integer id;
    private String categoryName;
    private String categoryAlias;//别名
    private Integer createUser;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
