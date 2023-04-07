package com.homepainter.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    Date time;
    int userId;
    int orderId;
    int addressId;

    int goodsId;

    int count;

    double yunfei;

    double AllPrice;

    String status;

    Address address;


    Goods goods;

    public Order(Date time, int userId, int addressId, int goodsId, int count, String status) {
        this.time = time;
        this.userId = userId;
        this.addressId = addressId;
        this.goodsId = goodsId;
        this.count = count;
        this.status = status;
    }

    public Order(Date time, int userId, int addressId, int goodsId, int count, double yunfei, double allprice, String status) {
        this.time = time;
        this.userId = userId;
        this.addressId = addressId;
        this.goodsId = goodsId;
        this.count = count;
        this.yunfei = yunfei;
        AllPrice = allprice;
        this.status = status;
    }


}
