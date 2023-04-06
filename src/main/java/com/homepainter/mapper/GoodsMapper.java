package com.homepainter.mapper;

import com.homepainter.pojo.Goods;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsMapper {

    @Select("select style from goods where goodsId = #{goodsId}")
    String getStyleById(int goodsId);


    @Select("select * from goods limit 10")
    @Results({
            @Result(
                    property = "appraise",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_appraiseMapper.getAppraiseById")
            ),
            @Result(
                    property = "imageUrl",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_imageMapper.getImageById")
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            ),
            @Result(
                    property = "appraiseId",
                    column = "appraiseId"
            )


    })
    List<Goods> getAllGoods();
//    @Select("select * from goods where match(category, title, subtitle, style, detail)  against(#{searchContent}) ")
      @Select("select * from goods where title like concat('%', #{searchContent}, '%') or subtitle like concat('%', #{searchContent}, '%') or detail like concat('%', #{searchContent}, '%') or style like concat('%', #{searchContent}, '%') or theme like concat('%', #{searchContent}, '%') or material like concat('%', #{searchContent}, '%') or category like concat('%', #{searchContent}, '%') limit 20")
//
//    @Select("select * from goods where match(category) against(#{searchContent}) or match(title) against(#{searchContent}) or match(subtitle) against(#{searchContent}) or match(superCategory) against(#{searchContent}) or match(style) against(#{style})")
    @Results({
            @Result(
                    property = "appraise",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_appraiseMapper.getAppraiseById")
            ),
            @Result(
                    property = "imageUrl",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_imageMapper.getImageById")
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            ),
            @Result(
                    property = "appraiseId",
                    column = "appraiseId"
            )

    })
    List<Goods> getGoodsByContent(String searchContent);
    @Select("select * from goods where goodsId = #{goodsId}")
    @Results({
            @Result(
                    property = "appraise",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_appraiseMapper.getAppraiseById")
            ),
            @Result(
                    property = "imageUrl",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_imageMapper.getImageById")
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            ),
            @Result(
                    property = "appraiseId",
                    column = "appraiseId"
            )

    })
    Goods getGoodsById(int goodsId);

    @Select("select * from goods where goodsId = #{goodsId}")
    @Results({
            @Result(
                    property = "imageUrl",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_imageMapper.getImageById")
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            )

    })
    Goods getGoodsByIdNoAp(int goodsId);

    @Insert("insert into goods values (#{goodsId}, #{title}, #{storage}, #{detail}, #{price}, #{superCategory}, #{category}, #{subtitle}, #{style}, #{theme}, #{material}, #{modal_id})")
    int insertGoods(Goods goods);

    @Select("select * from goods where modalId = #{modalId}")
    @Results({
            @Result(
                    property = "imageUrl",
                    column = "goodsId",
                    javaType = List.class,
                    many = @Many(select = "com.homepainter.mapper.Goods_imageMapper.getImageById")
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            )

    })
    Goods getGoodsByModal(String modalId);


}
