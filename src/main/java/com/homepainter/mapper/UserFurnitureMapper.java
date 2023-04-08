package com.homepainter.mapper;

import com.homepainter.pojo.UserFurniture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserFurnitureMapper {

    @Insert("insert into userfurniture values (#{userId}, #{fpId}, #{name}, #{time})")
    int insertUserFurniture(UserFurniture userFurniture);

    @Select("select * from userfurniture where userId = #{userId}")
    List<UserFurniture> getById(int userId);

}
