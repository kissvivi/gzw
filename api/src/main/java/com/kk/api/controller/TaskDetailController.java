package com.kk.api.controller;

import com.kk.api.core.response.Result;
import com.kk.api.core.response.ResultGenerator;
import com.kk.api.dto.AccountWithRole;
import com.kk.api.dto.AccountWithRolePermission;
import com.kk.api.entity.Account;
import com.kk.api.entity.TaskDetail;
import com.kk.api.entity.Templates;
import com.kk.api.service.AccountService;
import com.kk.api.service.RolePermissionService;
import com.kk.api.service.RoleService;
import com.kk.api.service.TaskDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author kk
* @date 2023/03/28
*/
@RestController
@RequestMapping("/task/detail")
@Slf4j
public class TaskDetailController {
@Resource
private TaskDetailService taskDetailService;
    @Resource
    private AccountService accountService;

    @Resource
    private RolePermissionService rolePermissionService;

@PostMapping
public Result add(@RequestBody TaskDetail taskDetail) {
taskDetailService.save(taskDetail);
return ResultGenerator.genOkResult();
}

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
taskDetailService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PutMapping
public Result update(@RequestBody TaskDetail taskDetail) {
    taskDetail.setUploadTime(new Date(System.currentTimeMillis()));
taskDetailService.update(taskDetail);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
TaskDetail taskDetail = taskDetailService.getById(id);
return ResultGenerator.genOkResult(taskDetail);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size,final Principal principal) {
PageHelper.startPage(page, size);
List<TaskDetail> list = taskDetailService.listAll();

    String username = principal.getName();
    AccountWithRole account = accountService.findByName(username);
    log.info("principal.getName();"+principal.getName());
    log.info("account.getComId()"+account);
    if (account.getComId()!=null && !account.getRoleName().equals("超级管理员")) {
        list = list.stream().filter(taskDetail -> taskDetail.getComId().longValue()==account.getComId()).collect(Collectors.toList());
    }

PageInfo<TaskDetail> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}


    /**
     * @param id     指想要下载的文件的任务号
     * @param response
     * @功能描述 下载文件:将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存
     */
    @GetMapping("/download/{id}")
    public void downloadLocal(@PathVariable Long id, HttpServletResponse response) throws IOException {

        String targetFilePath = "D:\\upload\\xls";
        var path = taskDetailService.getById(id).getFileUrl();

        targetFilePath = targetFilePath+"\\"+path;

        // 读到流中
        InputStream inputStream = new FileInputStream(targetFilePath);// 文件的存放路径
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(path).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[4097];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }


}
