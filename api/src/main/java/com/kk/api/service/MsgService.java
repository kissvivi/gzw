package com.kk.api.service;

import com.kk.api.entity.Msg;
import com.kk.api.core.service.Service;

import java.util.List;

/**
* @author kk
* @date 2023/04/05
*/
public interface MsgService extends Service<Msg> {
    List<Long> getMsgIdByUser(Long uid);
}
