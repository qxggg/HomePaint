package com.homepainter.service;

import com.homepainter.pojo.Collect;
import com.homepainter.pojo.Goods;
import com.homepainter.pojo.Goods_image;
import com.homepainter.pojo.Hot;

import java.util.List;

public interface GoodsService {
    List<Goods> getAllGoods();

    List<Goods> getGoodsByContent(String searchContent);

    Goods getGoodsById(int goodsId);

    Goods getGoodsByIdNoAp(int goodsId);

    int insertGoods(Goods goods);

    List<Hot> selectHotByType(String type);

    Goods getGoodsByModal(String modalId);

    int insertCollect(Collect collect);

    List<Collect> getAllCollect();

    int deleteCollect(int userId, String enumId, int collectId);

    void insertView(int userId, int goodsId, int viewTime);

    List<Collect> getCollectById(int userId, String enumId);
}
