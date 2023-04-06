package com.homepainter.service;

import com.homepainter.mapper.UserFurnitureMapper;
import com.homepainter.pojo.UserFurniture;

import java.util.List;

public interface UserFurnitureService {
    int insertUserFurniture(UserFurniture userFurniture);

    List<UserFurniture> getById(int userId);
}
