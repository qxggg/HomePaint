package com.homepainter.service;

import com.homepainter.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService {
    List<User> getUserList();

    String getPassByTelephone(String telephone);

    boolean ifExistsTel(String telephone);
    boolean insertUser(User user);

    void sendCode(String telephone);

    int getIdByTel(String telephone);

    String getUserById( int userId);

    String getAvatarById( int userId);

    User getAllById(int userId);



}
