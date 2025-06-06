package com.homepainter.mapper;

import com.homepainter.pojo.Goods_appraise;
import com.homepainter.pojo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface Goods_appraiseMapper {
    @Select("select * from goods_appraise")
    @Results({
            @Result(
                    property = "imageUrl",
                    column = "appraiseId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Appraise_imageMapper.getAllImage")
            ),

            @Result(
                    property = "appraiseId",
                    column = "appraiseId"
            )
    })
    List<Goods_appraise> getAllAppraise();

    @Select("select * from goods_appraise where goodsId = #{goodsId}")
    @Results({
            @Result(
                    property = "imageUrl",
                    column = "appraiseId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Appraise_imageMapper.getImageById")
            ),

            @Result(
                    property = "appraiseId",
                    column = "appraiseId"
            ),



    })
    List<Goods_appraise> getAppraiseById(String appraiseId);
}
