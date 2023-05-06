package com.homepainter.mapper;

import com.homepainter.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {
    @Select("select * from user;")
    List<User> getUserList();

    @Select("select password from user where telephone = #{telephone}")
    String getPassByTelephone(String telephone);

    @Insert("insert into `user` values(#{username}, #{password}, #{telephone}, #{userId}, #{avatar}, #{HaveHouse})")
    int insertUser(User user);

    @Select("select userId from user where telephone = #{telephone}")
    int selectIdByTel(String telephone);

    @Select("select username from user where userId = #{userId}")
    String getUserById(int userId);

    @Select("select avatar from user where userId = #{userId}")
    String getAvatarById( int userId);

    @Select("select * from user where userId = #{userId}")
    User getAllById(int userId);

}
