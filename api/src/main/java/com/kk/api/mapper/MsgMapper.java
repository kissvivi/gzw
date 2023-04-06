package com.kk.api.mapper;

import com.kk.api.core.mapper.MyMapper;
import com.kk.api.entity.Msg;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MsgMapper extends MyMapper<Msg> {

    @Select("select msg_id from user_msg where user_id = #{uid}")
    List<Long> getMsgIdByUser(Long uid);
}