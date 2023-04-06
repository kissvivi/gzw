package com.kk.api.mapper;

import com.kk.api.core.mapper.MyMapper;
import com.kk.api.entity.Templates;
import org.apache.ibatis.annotations.Select;

public interface TemplatesMapper extends MyMapper<Templates> {

    @Select("select * from templates where id = ( " +
            "SELECT task_main.temp_id FROM `task_main` where task_main.id = #{taskId})")
    Templates findTempByTaskId(Long taskId);
}