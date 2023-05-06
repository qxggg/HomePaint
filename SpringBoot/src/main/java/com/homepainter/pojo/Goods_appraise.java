package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods_appraise {
    int appraiseId;
    int goodsId;
    String appraise;

    int userId;

    Date time;

    User user;

    boolean ifCollect;

    List<Appraise_image> imageUrl;

}
