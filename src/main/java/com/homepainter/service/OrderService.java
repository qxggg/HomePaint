package com.homepainter.service;

import com.homepainter.pojo.Address;
import com.homepainter.pojo.Order;

import java.util.List;

public interface OrderService {
    int insertAddress(Address address);
    List<Address> getAllAddress();

    List<Address> getAddressById(int userId);
    int updateAddress(Address address);

    int deleteAddress(int addressId);

    List<Order> getOrderList();

    Order getOrderDetail(int orderId);

    int insertOrder(Order order);

    Address getDefault(int userId);

    List<Order> getOrderById(int userId);

}
