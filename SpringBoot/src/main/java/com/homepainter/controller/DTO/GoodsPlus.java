package com.homepainter.controller.DTO;

import com.homepainter.pojo.Goods_appraise;
import com.homepainter.pojo.Goods_image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GoodsPlus {
    int goodsId;
    String title;
    int storage;
    String detail;

    double price;

    String superCategory;

    String category;

    String subtitle;

    String style;

    String theme;

    String material;

    String modalId;

    List<Goods_appraise> appraise;

    String imageURL;



}
