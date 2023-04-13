package com.homepainter.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {
    private String username;
    private String password;
    private String telephone;

    int userId;

    String avatar;

    String gender;

    boolean HaveHouse;

    public User(String telephone, String password){
        this.telephone = telephone;
        this.password = password;
    }

    public User(String telephone){
        this.telephone = telephone;
    }

    public User(String username, String password, String telephone, boolean HaveHouse) {
        this.username = username;
        this.password = password;
        this.telephone = telephone;
        this.HaveHouse = HaveHouse;
    }
}
