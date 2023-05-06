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
        if (addressMapper.getAddressById(address.getUserId()).isEmpty())
            address.setDefault(true);
        else {
            if (address.isDefault() == true && addressMapper.getDefault(address.getUserId()) != null) {
                Address address1 = addressMapper.getDefault(address.getUserId());
                address1.setDefault(false);
                addressMapper.updateAddress(address1);
            }
        }
        System.out.println(address.isDefault());
        return addressMapper.insertAddress(address);
    }

    @Override
    public List<Address> getAllAddress() {
        return addressMapper.getAllAddress();
    }

    @Override
    public List<Address> getAddressById(int userId) {
        return addressMapper.getAddressById(userId);
    }

    @Override
    public int updateAddress(Address address) {
        Address address1 = addressMapper.getDefault(address.getUserId());
        if (address1 != null && address1.isDefault() == true){
            address1.setDefault(false);
            addressMapper.updateAddress(address1);
        }
        System.out.println(address);
        return addressMapper.updateAddress(address);
    }

    @Override
    public int deleteAddress(int addressId) {
        Address address = addressMapper.getAddressByAddressId(addressId);
        if (addressMapper.deleteAddress(addressId) == 0) return 0;
        List<Address> addresses = addressMapper.getAddressById(address.getUserId());
        if (addresses.isEmpty());
        else {
            if (address.isDefault() == true){
                Address address1 = addresses.get(0);
                address1.setDefault(true);
                addressMapper.updateAddress(address1);
            }
        }
        return 1;
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

    @Override
    public Address getDefault(int userId) {
        return addressMapper.getDefault(userId);
    }

    @Override
    public List<Order> getOrderById(int userId) {
        return orderMapper.getOrderById(userId);
    }
}
