package com.d.order.mapper;

import com.d.order.entity.Msg;
import org.apache.ibatis.annotations.Insert;

public interface MsgMapper {
    @Insert("INSERT INTO msg (id,content,status,create_time) VALUES (#{id},#{content},#{status},now())")
    int insert(Msg msg);
}
