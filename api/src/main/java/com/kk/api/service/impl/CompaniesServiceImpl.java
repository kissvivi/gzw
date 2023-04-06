package com.kk.api.service.impl;

import com.kk.api.mapper.CompaniesMapper;
import com.kk.api.entity.Companies;
import com.kk.api.service.CompaniesService;
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
public class CompaniesServiceImpl extends AbstractService<Companies> implements CompaniesService {
@Resource
private CompaniesMapper companiesMapper;

}
