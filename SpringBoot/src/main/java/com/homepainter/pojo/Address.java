package com.homepainter.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private int addressId;
    private String addressCity;
    private String address;
    private String phone;
    private String nickname;
    int userId;
    boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Address(String addressCity , String address, String phone, String nickname, int userId) {
        this.addressCity = addressCity;
        this.address = address;
        this.phone = phone;
        this.nickname = nickname;
        this.userId = userId;
    }

    public Address(String addressCity , String address, String phone, String nickname, int userId, boolean isDefault) {
        this.addressCity = addressCity;
        this.address = address;
        this.phone = phone;
        this.nickname = nickname;
        this.userId = userId;
        this.isDefault = isDefault;
    }

    public Address(int addressId, String addressCity , String address, String phone, String nickname, int userId) {
        this.addressId = addressId;
        this.addressCity = addressCity;
        this.address = address;
        this.phone = phone;
        this.nickname = nickname;
        this.userId = userId;
    }
}
