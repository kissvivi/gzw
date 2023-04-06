package com.kk.api.controller;

import com.kk.api.core.response.Result;
import com.kk.api.core.response.ResultGenerator;
import com.kk.api.entity.TaskMain;
import com.kk.api.service.TaskMainService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kk.api.util.TaskNoUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @author kk
* @date 2023/04/03
*/
@RestController
@RequestMapping("/task/main")
public class TaskMainController {
@Resource
private TaskMainService taskMainService;

@PostMapping
public Result add(@RequestBody TaskMain taskMain) {
    String taskNo = TaskNoUtils.getOrderNum();
    taskMain.setTaskNo(taskNo);
taskMainService.save(taskMain);
return ResultGenerator.genOkResult();
}

    @PostMapping("cmd")
    public Result cmdTask(@RequestBody TaskMain taskMain) {
        taskMainService.cmdTask(taskMain);
        return ResultGenerator.genOkResult();
    }

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
taskMainService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody TaskMain taskMain) {
taskMainService.update(taskMain);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
TaskMain taskMain = taskMainService.getById(id);
return ResultGenerator.genOkResult(taskMain);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<TaskMain> list = taskMainService.listAll();
PageInfo<TaskMain> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
