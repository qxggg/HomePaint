package com.homepainter.service;

import com.homepainter.mapper.*;
import com.homepainter.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    BehaveService behaveService;

    @Autowired
    GoodsMapper goodsMapper;

    @Autowired
    Goods_imageMapper goodsImageMapper;

    @Autowired
    HotMapper hotMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CollectMapper collectMapper;

    @Autowired
    TiebaMapper tiebaMapper;
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
        Goods goods = goodsMapper.getGoodsById(goodsId);
        List<Goods_appraise> goods_appraises = goods.getAppraise();
        for (Goods_appraise goodsAppraise : goods_appraises) {
            User user = userMapper.getAllById(goodsAppraise.getUserId());
            if (user != null)
                user.setPassword(null);
            goodsAppraise.setUser(user);
        }
        goods.setAppraise(goods_appraises);
        return goods;
    }

    @Override
    public Goods getGoodsByIdNoAp(int goodsId) {
        return goodsMapper.getGoodsByIdNoAp(goodsId);
    }

    public int insertGoods(Goods goods) {
        if (goodsMapper.insertGoods(goods) == 0) return 0;
        for (Goods_image goodsImage : goods.getImageUrl())
            if (goodsImageMapper.insertGoodsImage(goodsImage) == 0) return 0;
        return 1;
    }

    @Override
    public List<Hot> selectHotByType(String type) {
        return hotMapper.getHotById(type);
    }

    @Override
    public Goods getGoodsByModal(String modalId) {
        return goodsMapper.getGoodsByModal(modalId);
    }

    @Override
    public int insertCollect(Collect collect) {
        if (collectMapper.insertCollect(collect) == 0)  return 0;
        else {
            if(collect.getEnumId().equals("goods")) {
                behaveService.updateGoods(collect.getUserId(), collect.getCollectId(), "randomCollect", 1);
                behaveService.updateAddStyle(collect.getUserId(), collect.getCollectId(),  "randomCollect",   1);
            }
            return 1;
        }
    }

    @Override
    public List<Collect> getAllCollect() {
        return collectMapper.getAllCollect();
    }

    @Override
    public int deleteCollect(int userId, String enumId, int collectId) {
        if (collectMapper.deleteCollect(userId, enumId, collectId) == 0) return 0;
        else {
            if (enumId.equals("goods")) {
                behaveService.updateGoods(userId, collectId, "randomCollect", 0);
                behaveService.updateAddStyle(userId, collectId, "randomCollect", -1);
            }
            return 1;
        }
    }

    @Override
    public void insertView(int userId, int goodsId, int viewTime) {
        behaveService.updateAddGoods(userId, goodsId, "randomView", viewTime);
        behaveService.updateAddStyle(userId, goodsId, "randomView", viewTime);
    }

    @Override
    public List<Collect> getCollectById(int userId, String enumId) {
        List<Collect> l = collectMapper.getGoodsById(userId, enumId);
        if (enumId == "goods") {
            for (Collect c : l)
                c.setGoods(goodsMapper.getGoodsByIdNoAp(c.getCollectId()));
        }
        else {
            for (Collect c : l)
                c.setTieba(tiebaMapper.getTiebaByIdNoAp(c.getCollectId()));
        }
        return l;
    }

}
