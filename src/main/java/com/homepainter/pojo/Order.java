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

    String status;

    Address address;

    public Order(Date time, int userId, int addressId, int goodsId, int count, String status) {
        this.time = time;
        this.userId = userId;
        this.addressId = addressId;
        this.goodsId = goodsId;
        this.count = count;
        this.status = status;
    }

    Goods goods;


}
