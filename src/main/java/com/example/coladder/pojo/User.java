package com.example.coladder.pojo;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

@Data
public class User {
    @NotNull
    private Integer id;

    private String username;

    @JsonIgnore //在查询返回的时候忽略这个字段
    private String password;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(regexp = "^\\S{5,16}$")
    private String nickname;

    private String userPic;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
