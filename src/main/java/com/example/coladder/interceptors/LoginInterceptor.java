package com.example.coladder.interceptors;

import com.example.coladder.untils.JwtUntil;
import com.example.coladder.untils.ThreadLocalUntil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

//拦截器
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        try {
            //从redis中获取token
            String redisToken = stringRedisTemplate.opsForValue().get(token);
            if (redisToken == null) {
                //token 过期
                throw new RuntimeException();
            }
            Map<String, Object> map = JwtUntil.parseToken(token);
            //数据存储到threadlocal中
            ThreadLocalUntil.set(map);

            return true;
        }catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清除ThreadLocal中的数据
        ThreadLocalUntil.remove();
    }
}
