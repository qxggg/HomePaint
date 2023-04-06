package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserFurniture {

    int userId;

    String fp_id;

    String name;

    Date time;
}
