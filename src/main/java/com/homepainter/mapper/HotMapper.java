package com.homepainter.mapper;

import com.homepainter.pojo.Hot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HotMapper {

    @Select("select * from hot where type = #{type}")
    List<Hot> getHotById(String type);
}
