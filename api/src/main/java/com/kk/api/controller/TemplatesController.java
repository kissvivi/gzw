package com.kk.api.controller;

import com.kk.api.core.response.Result;
import com.kk.api.core.response.ResultGenerator;
import com.kk.api.entity.Templates;
import com.kk.api.service.TemplatesService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.poi.util.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

/**
* @author kk
* @date 2023/04/03
*/
@RestController
@RequestMapping("/templates")
@Slf4j
public class TemplatesController {
@Resource
private TemplatesService templatesService;

@PostMapping
public Result add(@RequestBody Templates templates) {
templatesService.save(templates);
return ResultGenerator.genOkResult();
}/**
     * @param taskId     指想要下载的文件的任务号
     * @param response
     * @功能描述 下载文件:将输入流中的数据循环写入到响应输出流中，而不是一次性读取到内存
     */
    @GetMapping("/download/{taskId}")
    public void downloadLocal(@PathVariable Long taskId, HttpServletResponse response) throws IOException {

        Templates templates = templatesService.findTempByTaskId(taskId);
        String path = templates.getFileUrl();
        String targetFilePath = "D:\\upload\\xls";

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


    @PostMapping("/upload")
    public Result uploadExcel(@RequestParam("file")MultipartFile file) throws IOException {

        var in = file.getInputStream();
        var pname = file.getOriginalFilename();
        var pnameList =pname.split("\\.");
        var p = pnameList[pnameList.length-1];

        if (file.isEmpty()) {
            return ResultGenerator.genFailedResult("文件为空");
        }
        // 获取大小
        long size = file.getSize();
        log.info("文件大小： " + size);


        for (int i = pnameList.length - 1; i >= 0; i--) {
            log.info(pnameList[i]);
        }

        if (!(p.equals("xls") || p.equals("xlsx"))){
            log.info(p," == xlsx");
            return ResultGenerator.genFailedResult("p is errr");
        }

        System.out.println(file.getOriginalFilename());
        String targetFilePath = "D:\\upload\\xls\\";
        String fileName = UUID.randomUUID().toString().replace("-", "");
//        fileName = fileName+"."+p;
        fileName = fileName+pname;
        File targetFile = new File(targetFilePath  + fileName);

//        file.transferTo(targetFile);
//        byte[] ins = new byte[1024];
//        in.read(ins);

//        log.info(ins.toString());

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(targetFile);
            IOUtils.copy(in, fileOutputStream);
            log.info("------>>>>>>uploaded a file successfully!<<<<<<------");
        } catch (IOException e) {
            return ResultGenerator.genFailedResult(e.getMessage());
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                log.error("", e);
            }
        }

        return ResultGenerator.genOkResult(fileName);
    }

@DeleteMapping("/{id}")
public Result delete(@PathVariable Long id) {
templatesService.deleteById(id);
return ResultGenerator.genOkResult();
}

@PatchMapping
public Result update(@RequestBody Templates templates) {
templatesService.update(templates);
return ResultGenerator.genOkResult();
}

@GetMapping("/{id}")
public Result detail(@PathVariable Long id) {
Templates templates = templatesService.getById(id);
return ResultGenerator.genOkResult(templates);
}

@GetMapping
public Result list(@RequestParam(defaultValue = "0") Integer page,
@RequestParam(defaultValue = "0") Integer size) {
PageHelper.startPage(page, size);
List<Templates> list = templatesService.listAll();
PageInfo<Templates> pageInfo = PageInfo.of(list);
return ResultGenerator.genOkResult(pageInfo);
}
}
