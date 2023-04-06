package com.homepainter.mapper;

import com.homepainter.pojo.UserFurniture;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserFurnitureMapper {

    @Insert("insert into userfurniture values (#{userId}, #{fp_id}, #{name}, #{time})")
    int insertUserFurniture(UserFurniture userFurniture);

}
