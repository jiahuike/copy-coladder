package com.example.coladder.service.impl;

import com.example.coladder.mapper.UserMapper;
import com.example.coladder.pojo.User;
import com.example.coladder.service.UserService;
import com.example.coladder.untils.ThreadLocalUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findById(Integer id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.selectUserByName(username);
    }

    @Override
    public void register(String username, String password) {
        userMapper.addUser(username,password);
    }

    @Override
    public void updateUserInfo(User user){
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateUserInfo(user);
    }

    @Override
    public void updateAvatar(String avatarUrl){
        Map<String,Object> map = ThreadLocalUntil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updateAvatar(avatarUrl,id);
    }

    @Override
    public void updatePassword(String newPassword) {
        Map<String,Object> map = ThreadLocalUntil.get();
        Integer id = (Integer) map.get("id");
        userMapper.updatePassword(newPassword,id);
    }
}
