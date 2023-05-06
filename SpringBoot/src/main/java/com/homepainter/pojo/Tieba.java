package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tieba {
    String userId;
    String content;
    int tiebaId;
    String favorites;
    Date time;
    int collect;

    String title;

    List<TiebaFlags> tiebaFlags;

    List<TiebaImage> tiebaImage;

    List<TiebaEvaluate> tiebaEvaluates;

    User user;
}
