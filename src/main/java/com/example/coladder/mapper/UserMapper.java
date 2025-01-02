package com.example.coladder.mapper;

import com.example.coladder.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    public User selectUserById(Integer id);

    @Select("select * from user where username = #{username}")
    User selectUserByName(String username);

    @Insert("insert into user (username,password,create_time,update_time)" +
            "values(#{username},#{password},now(),now() )")
    void addUser(String username, String password);

    @Update("update user set nickname=#{nickname},email=#{email},update_time=#{updateTime} where id = #{id}")
    void updateUserInfo(User user);

    @Update("update user set user_pic=#{avatarUrl},update_time=now() where id=#{id}")
    void updateAvatar(String avatarUrl,Integer id);

    @Update("update user set password=#{newPassword},update_time=now() where id=#{id}")
    void updatePassword(String newPassword, Integer id);
}
