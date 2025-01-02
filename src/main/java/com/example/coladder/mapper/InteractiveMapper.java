package com.example.coladder.mapper;


import com.example.coladder.pojo.Interactive;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface InteractiveMapper {

    @Insert("insert into interactive (question,answer,create_time,create_user)" +
            "values(#{question},#{answer},now(),#{id} )")
    void addMessage(String question, String answer, Integer id);

    @Select("select * from interactive where create_user = #{id}")
    Interactive[] selectByUserID(Integer id);
}
