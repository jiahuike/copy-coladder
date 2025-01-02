package com.example.coladder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen() {
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", 1);
        claims.put("username", "wangba");
        //生成jwt
        String token = JWT.create()
                .withClaim("user",claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .sign(Algorithm.HMAC256("coladder"));
        System.out.println(token);


    }

    @Test
    public void testParse() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ1c2V" +
                "ybmFtZSI6IndhbmdiYSJ9LCJleHAiOjE3MzQwMDI2Nzl9.CRPo2MeAL7v0WqAgk65bzZufZAIR6dzZlYr-6xbUod0";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("coladder")).build();

        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        System.out.println(claims.get("user"));
    }
}
