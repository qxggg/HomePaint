package com.homepainter.mapper;

import com.homepainter.pojo.RandomConsume;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RandomConsumeMapper {
    @Insert("insert into randomconsume values (#{userId}, #{randomConsume})")
    int insertConsume(RandomConsume randomConsume);


}
