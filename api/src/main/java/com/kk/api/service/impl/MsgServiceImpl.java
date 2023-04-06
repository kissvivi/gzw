package com.kk.api.service.impl;

import com.kk.api.mapper.MsgMapper;
import com.kk.api.entity.Msg;
import com.kk.api.service.MsgService;
import com.kk.api.core.service.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @author kk
* @date 2023/04/05
*/
@Service
@Transactional(rollbackFor = Exception.class)
public class MsgServiceImpl extends AbstractService<Msg> implements MsgService {
@Resource
private MsgMapper msgMapper;

    @Override
    public List<Long> getMsgIdByUser(Long uid) {
        return msgMapper.getMsgIdByUser(uid);
    }
}
