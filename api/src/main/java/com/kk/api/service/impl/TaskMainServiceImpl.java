package com.kk.api.service.impl;

import com.kk.api.entity.TaskDetail;
import com.kk.api.mapper.TaskDetailMapper;
import com.kk.api.mapper.TaskMainMapper;
import com.kk.api.entity.TaskMain;
import com.kk.api.service.TaskMainService;
import com.kk.api.core.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author kk
* @date 2023/04/03
*/
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class TaskMainServiceImpl extends AbstractService<TaskMain> implements TaskMainService {
@Resource
private TaskMainMapper taskMainMapper;

    @Resource
    private TaskDetailMapper taskDetailMapper;

    @Override
    public String cmdTask(TaskMain taskMain) {
        try {
            TaskDetail taskDetail = new TaskDetail();
            var comList = taskMain.getToCom().split(",");
            for (String com:
                    comList) {
                taskDetail.setTaskId(taskMain.getId());
                taskDetail.setName(taskMain.getTaskNo());
                taskDetail.setComId(Integer.valueOf(com));
//                taskDetail.setUploadTime(taskMain.getEndTime());
                taskDetail.setStatus(0);
                taskDetail.setId(null);
                taskDetailMapper.insert(taskDetail);
            }

            taskMainMapper.updateStatusById(taskMain.getId().longValue(),1);
        }catch (Exception e){
            log.info(e.getMessage());
            return e.getMessage();
        }

        return null;

    }
}
