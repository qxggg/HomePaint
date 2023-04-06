package com.homepainter.service;

import com.homepainter.mapper.UserFurnitureMapper;
import com.homepainter.pojo.User;
import com.homepainter.pojo.UserFurniture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFurnitureServiceImpl implements UserFurnitureService{

    @Autowired
    UserFurnitureMapper userFurnitureMapper;

    @Override
    public int insertUserFurniture(UserFurniture userFurniture) {
        return userFurnitureMapper.insertUserFurniture(userFurniture);
    }

    @Override
    public List<UserFurniture> getById(int userId) {
        return userFurnitureMapper.getById(userId);
    }
}
