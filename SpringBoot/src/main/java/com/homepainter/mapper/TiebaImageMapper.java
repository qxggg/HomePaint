package com.homepainter.mapper;


import com.homepainter.pojo.TiebaImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface TiebaImageMapper {

    @Select("select * from tiebaImage where tiebaId = #{tiebaId}")
    List<TiebaImage> getTiebaImage(int tiebaId);
}
