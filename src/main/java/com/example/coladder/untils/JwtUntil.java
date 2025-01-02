package com.example.coladder.untils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

//获取加密令牌
public class JwtUntil {

    private static final String KEY = "coladder";

    /**
     * 接收数据，生成token
     * @param claims
     * @return
     */
    public static String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims",claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256(KEY));
    }

    /**
     * 接受token，返回数据
     * @param token
     * @return
     */
    public static Map<String, Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
