package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
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
    List<Goods_image> imageUrl;

    List<Goods_appraise> appraise;

    public Goods(int goodsId, String title, int storage, String detail, double price, String superCategory, String category, String subtitle, String style, String theme, String material, String modalId, List<Goods_image> imageUrl) {
        this.goodsId = goodsId;
        this.title = title;
        this.storage = storage;
        this.detail = detail;
        this.price = price;
        this.superCategory = superCategory;
        this.category = category;
        this.subtitle = subtitle;
        this.style = style;
        this.theme = theme;
        this.material = material;
        this.modalId = modalId;
        this.imageUrl = imageUrl;
    }

    public Goods(int goodsId, String title, int storage, String detail, double price, String superCategory, String category, String subtitle, String style, String theme, String material, String modalId) {
        this.goodsId = goodsId;
        this.title = title;
        this.storage = storage;
        this.detail = detail;
        this.price = price;
        this.superCategory = superCategory;
        this.category = category;
        this.subtitle = subtitle;
        this.style = style;
        this.theme = theme;
        this.material = material;
        this.modalId = modalId;
    }

    public Goods(String title, int storage, String detail, double price, String superCategory, String category, String subtitle, String style, String theme, String material, String modalId) {
        this.title = title;
        this.storage = storage;
        this.detail = detail;
        this.price = price;
        this.superCategory = superCategory;
        this.category = category;
        this.subtitle = subtitle;
        this.style = style;
        this.theme = theme;
        this.material = material;
        this.modalId = modalId;
    }

    public Goods( String title, int storage, String detail, double price, String superCategory, String category, String subtitle, String style, String theme, String material, String modalId, List<Goods_image> imageUrl) {
        this.title = title;
        this.storage = storage;
        this.detail = detail;
        this.price = price;
        this.superCategory = superCategory;
        this.category = category;
        this.subtitle = subtitle;
        this.style = style;
        this.theme = theme;
        this.material = material;
        this.modalId = modalId;
        this.imageUrl = imageUrl;
    }



}
