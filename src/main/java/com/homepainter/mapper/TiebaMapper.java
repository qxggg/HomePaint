package com.homepainter.mapper;


import com.homepainter.pojo.Tieba;
import com.homepainter.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TiebaMapper {

    @Select("select * from tieba")
    @Results({
            @Result(
                    property = "tiebaFlags",
                    column = "tiebaId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.TiebaFlagsMapper.getTiebaFlags")
            ),

            @Result(
                    property = "tiebaImage",
                    column = "tiebaId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.TiebaImageMapper.getTiebaImage")
            ),

            @Result(
                    property = "tiebaId",
                    column = "tiebaId"
            ),
            @Result(
                    property = "user",
                    column = "userId",
                    javaType = User.class,
                    one = @One(select = "com.homepainter.mapper.UserMapper.getAllById")
            ),
            @Result(
                    property = "userId",
                    column = "userId"
            )
    })
    List<Tieba> getTiebaList();

    @Select("select * from tieba where tiebaId = #{tiebaId}")
    @Results({
            @Result(
                    property = "tiebaFlags",
                    column = "tiebaId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.TiebaFlagsMapper.getTiebaFlags")
            ),

            @Result(
                    property = "tiebaImage",
                    column = "tiebaId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.TiebaImageMapper.getTiebaImage")
            ),

            @Result(
                    property = "tiebaId",
                    column = "tiebaId"
            )
    })
    Tieba getTiebaByIdNoAp(int tiebaId);

    @Update("update tieba set favorites = favorites + 1 where tiebaId = #{tiebaId}")
    int givePrice(int tiebaId);

}
