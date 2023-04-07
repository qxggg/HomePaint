package com.homepainter.mapper;

import com.homepainter.pojo.Address;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AddressMapper {

    @Select("select * from address")
    List<Address> getAllAddress();

    @Select("select * from address where userId = #{userId}")
    List<Address> getAddressById(int userId);

    @Select("select * from address where addressId = #{addressId}")
    Address getAddressByAddressId(int addressId);

    @Insert("insert into address values (#{addressId}, #{addressCity}, #{address}, #{phone}, #{nickname}, #{userId}, #{isDefault})")
    int insertAddress(Address address);

    @Update("update address set addressCity = #{addressCity}, address = #{address}, phone = #{phone}, nickname = #{nickname}, isDefault = #{isDefault} where addressId = #{addressId}")
    int updateAddress(Address address);

    @Delete("delete from address where #{addressId} = addressId")
    int deleteAddress(int addressId);

    @Select("select * from address where isDefault = true and userId = #{userId}")
    Address getDefault(int userId);
}
