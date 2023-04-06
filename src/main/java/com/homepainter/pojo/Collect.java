package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Collect {

    int userId;

    String enumId;

    int collectId;

    Date time;


}
