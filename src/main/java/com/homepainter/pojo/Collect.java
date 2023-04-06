package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collect {

    int userId;

    String enumId;

    int collectId;

    Date time;

    Goods goods;

    public Collect(int userId, String enumId, int collectId, Date time) {
        this.userId = userId;
        this.enumId = enumId;
        this.collectId = collectId;
        this.time = time;
    }

    Tieba tieba;


}
