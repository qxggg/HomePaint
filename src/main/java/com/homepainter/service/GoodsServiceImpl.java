package com.homepainter.service;

import com.homepainter.mapper.GoodsMapper;
import com.homepainter.mapper.Goods_imageMapper;
import com.homepainter.pojo.Goods;
import com.homepainter.pojo.Goods_image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    Goods_imageMapper goodsImageMapper;
    @Override
    public List<Goods> getAllGoods() {
        return goodsMapper.getAllGoods();
    }

    @Override
    public List<Goods> getGoodsByContent(String searchContent) {
        return goodsMapper.getGoodsByContent(searchContent);
    }

    @Override
    public Goods getGoodsById(int goodsId) {
        return goodsMapper.getGoodsById(goodsId);
    }

    public int insertGoods(Goods goods) {
        if (goodsMapper.insertGoods(goods) == 0) return 0;
        for (Goods_image goodsImage : goods.getImageUrl())
            if (goodsImageMapper.insertGoodsImage(goodsImage) == 0) return 0;
        return 1;
    }


}
