package com.homepainter.service;

import com.homepainter.mapper.AddressMapper;
import com.homepainter.mapper.OrderMapper;
import com.homepainter.pojo.Address;
import com.homepainter.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    AddressMapper addressMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    BehaveService behaveService;

    @Override
    public int insertAddress(Address address) {
        return addressMapper.insertAddress(address);
    }

    @Override
    public List<Address> getAllAddress() {
        return addressMapper.getAllAddress();
    }

    @Override
    public int updateAddress(Address address) {
        return addressMapper.updateAddress(address);
    }

    @Override
    public int deleteAddress(int addressId) {
        return addressMapper.deleteAddress(addressId);
    }

    @Override
    public List<Order> getOrderList() {
        return orderMapper.getOrderList();
    }

    @Override
    public Order getOrderDetail(int orderId) {
        Order order = orderMapper.getOrderDetail(orderId);
        if (order != null) {
            behaveService.updateAddGoods(order.getUserId(), order.getGoodsId(), "randomClick", 1);
            behaveService.updateAddStyle(order.getUserId(), order.getGoodsId(), "randomClick", 1);
        }
        return order;
    }

    @Override
    public int insertOrder(Order order) {
        if (orderMapper.insertOrder(order) == 0) return 0;
        else{
            behaveService.updateGoods(order.getUserId(), order.getGoodsId(), "randomConsume", 1);
            behaveService.updateAddStyle(order.getUserId(), order.getGoodsId(), "randomConsume", 1);
            return 1;
        }
    }
}
