package com.kk.api.service.impl;

import com.kk.api.mapper.TaskDetailMapper;
import com.kk.api.entity.TaskDetail;
import com.kk.api.service.TaskDetailService;
import com.kk.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author kk
* @date 2023/03/28
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TaskDetailServiceImpl extends AbstractService<TaskDetail> implements TaskDetailService {
@Resource
private TaskDetailMapper taskDetailMapper;

}
