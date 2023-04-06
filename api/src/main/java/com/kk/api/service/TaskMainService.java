package com.kk.api.service;

import com.kk.api.core.response.Result;
import com.kk.api.entity.TaskMain;
import com.kk.api.core.service.Service;
import org.springframework.web.bind.annotation.RequestBody;

/**
* @author kk
* @date 2023/04/03
*/
public interface TaskMainService extends Service<TaskMain> {

    String cmdTask(TaskMain taskMain);
}


