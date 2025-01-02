package com.example.coladder.pojo.apiPojo2;

import lombok.Setter;

@Setter
public class ApiRequest2 {

    private String input;

    // 构造方法、getter和setter
    public ApiRequest2() {
    }

    public String getInput() {
        return"""
                {
                  "model": "generalv3.5",
                  "messages": [
                    {
                      "role": "user",
                      "content":""" + "\"" + input + "\"" + """
                
                    """ +
                    """
                    }
                  ]
                }""";

    }

}
