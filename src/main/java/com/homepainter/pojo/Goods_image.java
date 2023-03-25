package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods_image {
    int goodsId;
    String imageUrl;

    int imageId;

    public Goods_image(int goodsId, String imageUrl) {
        this.goodsId = goodsId;
        this.imageUrl = imageUrl;
    }
}
