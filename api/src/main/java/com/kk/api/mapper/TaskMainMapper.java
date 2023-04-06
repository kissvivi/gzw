package com.kk.api.mapper;

import com.kk.api.core.mapper.MyMapper;
import com.kk.api.entity.TaskMain;
import org.apache.ibatis.annotations.Update;

public interface TaskMainMapper extends MyMapper<TaskMain> {

    @Update("update task_main set status = #{status} where id = #{id}")
    int updateStatusById(Long id,int status);
}