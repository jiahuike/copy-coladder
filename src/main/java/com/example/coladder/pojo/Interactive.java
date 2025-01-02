package com.example.coladder.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;

@Data
public class Interactive {
    @NotNull
    private Integer id;

    @NotNull
    private Integer createUser;

    @NotNull
    private String question;

    @NotNull
    private String answer;

    @NotNull
    private LocalDateTime createTime;



}
