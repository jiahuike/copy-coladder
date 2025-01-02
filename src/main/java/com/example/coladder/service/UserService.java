package com.example.coladder.service;

import com.example.coladder.pojo.User;

public interface UserService {

    User findById(Integer id);

    User findByUsername(String username);

    void register(String username, String password);

    void updateUserInfo(User user);

    //更新头像
    void updateAvatar(String avatarUrl);

    //密码更新
    void updatePassword(String newPassword);
}
