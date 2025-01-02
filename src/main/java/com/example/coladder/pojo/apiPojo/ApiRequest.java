package com.example.coladder.pojo.apiPojo;

import lombok.Setter;

@Setter
public class ApiRequest {

    private String input;

    // 构造方法、getter和setter
    public ApiRequest() {
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
                  ],
                  "stream": true
                }""";

    }

}
