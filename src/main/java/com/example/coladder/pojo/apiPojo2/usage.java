package com.example.coladder.pojo.apiPojo2;

import lombok.Data;

@Data
public class usage {
    private Integer prompt_tokens;
    private Integer complete_tokens;
    private Integer total_tokens;
}
