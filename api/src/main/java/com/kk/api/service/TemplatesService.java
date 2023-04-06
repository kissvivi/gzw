package com.kk.api.service;

import com.kk.api.entity.Templates;
import com.kk.api.core.service.Service;
import org.apache.ibatis.annotations.Select;

/**
* @author kk
* @date 2023/04/03
*/
public interface TemplatesService extends Service<Templates> {

    Templates findTempByTaskId(Long taskId);
}
