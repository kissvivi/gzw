package com.kk.api.service.impl;

import com.kk.api.mapper.TemplatesMapper;
import com.kk.api.entity.Templates;
import com.kk.api.service.TemplatesService;
import com.kk.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @author kk
* @date 2023/04/03
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class TemplatesServiceImpl extends AbstractService<Templates> implements TemplatesService {
@Resource
private TemplatesMapper templatesMapper;

    @Override
    public Templates findTempByTaskId(Long taskId) {
        return templatesMapper.findTempByTaskId(taskId);
    }
}
