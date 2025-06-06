package com.homepainter.service;

import com.homepainter.mapper.UserMapper;
import com.homepainter.pojo.User;
import com.homepainter.util.RedisUtil;
import com.homepainter.util.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    SendSms sendSms;

    @Autowired
    RedisUtil redisUtil;
    @Override
    public List<User> getUserList() {
        return userMapper.getUserList();
    }

    @Override
    public String getPassByTelephone(String telephone){
        return userMapper.getPassByTelephone(telephone);
    }

    @Override
    public boolean ifExistsTel(String telephone) {
        for (User user : userMapper.getUserList())
            if (telephone.equals(user.getTelephone())) return false;
        return true;
    }

    @Override
    public boolean insertUser(User user) {

        if (userMapper.insertUser(user) == 1) return true;
        else return false;
    }

    @Override
    public void sendCode(String telephone) {
        String verifyCode = String.valueOf((int)(Math.random() * 900000 + 100000));
        sendSms.send(telephone, verifyCode,"1540772");
        redisUtil.set("tel" + telephone, verifyCode);
        redisUtil.expire("tel" + telephone, 300);
    }

    @Override
    public int getIdByTel(String telephone) {
        return userMapper.selectIdByTel(telephone);
    }

    @Override
    public String getUserById( int userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public String getAvatarById( int userId) {
        return userMapper.getAvatarById(userId);
    }

    @Override
    public User getAllById(int userId) {
        return userMapper.getAllById(userId);
    }


}
