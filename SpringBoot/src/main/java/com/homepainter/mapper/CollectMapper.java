package com.homepainter.mapper;

import com.homepainter.pojo.Collect;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CollectMapper {

    @Insert("insert into collect values(#{userId}, #{enumId}, #{collectId}, #{time})")
    int insertCollect(Collect collect);

    @Select("select * from collect")
    List<Collect> getAllCollect();

    @Delete("delete from collect where userId = #{userId} and enumId = #{enumId} and collectId = #{collectId}")
    int deleteCollect(int userId, String enumId, int collectId);

    @Select("select * from collect where userId = #{userId} and enumId = #{enumId}")
    List<Collect> getGoodsById(int userId, String enumId);



}
