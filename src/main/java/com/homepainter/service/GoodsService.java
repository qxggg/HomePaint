package com.homepainter.service;

import com.homepainter.pojo.Goods;
import com.homepainter.pojo.Goods_image;

import java.util.List;

public interface GoodsService {
    List<Goods> getAllGoods();

    List<Goods> getGoodsByContent(String searchContent);

    Goods getGoodsById(int goodsId);

    int insertGoods(Goods goods);


}
