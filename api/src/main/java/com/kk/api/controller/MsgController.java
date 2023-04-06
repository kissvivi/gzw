package com.kk.api.controller;

import com.kk.api.core.response.Result;
import com.kk.api.core.response.ResultGenerator;
import com.kk.api.entity.Msg;
import com.kk.api.service.MsgService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author kk
* @date 2023/04/05
*/
@RestController
@RequestMapping("/msg")
public class MsgController {
@Resource
private MsgService msgService;

@PostMapping
public Result add(@RequestBody Msg msg) {
msgService.save(msg);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
msgService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody Msg msg) {
msgService.update(msg);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
Msg msg = msgService.getById(id);
return ResultGenerator.genOkResult(msg);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size,Long comId) {
PageHelper.startPage(page, size);
List<Msg> list = msgService.listAll();

list = list.stream().filter(msg -> msg.getToCom().longValue() == comId).collect(Collectors.toList());

PageInfo<Msg> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}

    @GetMapping("/userMsg")
    public Result listMsg(@RequestParam(defaultValue = "0") Long uid,@RequestParam(defaultValue = "0") Integer page,
                       @RequestParam(defaultValue = "0") Integer size) {
        PageHelper.startPage(page, size);
        List<Msg> list = msgService.listAll();

        List<Long> msgIdList = msgService.getMsgIdByUser(uid);

        for (Long id:
             msgIdList) {
            list = list.stream().filter(msg -> msg.getId().longValue() == id && msg.getStatus()==0
            ).collect(Collectors.toList());
            list.stream().forEach(msg -> {
                msg.setStatus(1);
                msgService.update(msg);
            });
        }

        PageInfo<Msg> pageInfo = PageInfo.of(list);
        return ResultGenerator.genOkResult(pageInfo);
    }
}
