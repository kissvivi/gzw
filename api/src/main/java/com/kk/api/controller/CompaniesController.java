package com.kk.api.controller;

import com.kk.api.core.response.Result;
import com.kk.api.core.response.ResultGenerator;
import com.kk.api.entity.Companies;
import com.kk.api.service.CompaniesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @author kk
* @date 2023/03/28
*/
@RestController
@RequestMapping("/companies")
public class CompaniesController {
@Resource
private CompaniesService companiesService;

@PostMapping
public Result add(@RequestBody Companies companies) {
companiesService.save(companies);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
companiesService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody Companies companies) {
companiesService.update(companies);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
Companies companies = companiesService.getById(id);
return ResultGenerator.genOkResult(companies);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<Companies> list = companiesService.listAll();
PageInfo<Companies> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
