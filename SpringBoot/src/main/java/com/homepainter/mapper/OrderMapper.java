package com.homepainter.mapper;

import com.homepainter.pojo.Address;
import com.homepainter.pojo.Goods;
import com.homepainter.pojo.Order;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository

public interface OrderMapper {

    @Select("select * from `order`")
    List<Order> getOrderList();

    @Select("select * from `order` where userId = #{userId}")
    @Results({
            @Result(
                    property = "goods",
                    column = "goodsId",
                    javaType = Goods.class,
                    many = @Many(select = "com.homepainter.mapper.GoodsMapper.getGoodsByIdNoAp")
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            )
    })
    List<Order> getOrderById(int userId);


    @Select("select * from `order`  where orderId = #{orderId}")
    @Results({
           @Result(
                 property = "goods",
                 column = "goodsId",
                 javaType = Goods.class,
                 many = @Many(select = "com.homepainter.mapper.GoodsMapper.getGoodsById")
         ),
            @Result(
                    property = "address",
                    column = "addressId",
                    javaType = Address.class,
                    many = @Many(select = "com.homepainter.mapper.AddressMapper.getAddressByAddressId")
            ),
            @Result(
                    property = "addressId",
                    column = "addressId"
            ),
            @Result(
                    property = "goodsId",
                    column = "goodsId"
            )

    })
    Order getOrderDetail(int orderId);

    @Insert("insert into `order` values(#{time}, #{userId}, #{orderId}, #{addressId}, #{goodsId}, #{count}, #{status}, #{yunfei}, #{AllPrice}, #{beizhu})")
    int insertOrder(Order order);
}
