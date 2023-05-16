package com.ywzs.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpResource;
import cn.hutool.http.HttpResponse;
import com.ywzs.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 文件上床和下载
 */
@RestController
@Slf4j
@RequestMapping("/common")
public class commonController {
    @Value("${hongdou.filePath}")
    String filePath;


    @PostMapping("/upload")
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        //上传来的文件会暂存在某个临时目录，未处理就在请求结束后删除
        String originalFilename = file.getOriginalFilename();
        log.info("收到文件" + originalFilename + "-----" + file.getContentType());
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));  //获取后缀名
        String fileName = UUID.randomUUID().toString();  //随机文件名，预防重复
        log.info("文件保存位置：" + filePath + fileName + suffix);
        //如果目录不存在，需要创建目录
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            file.transferTo(new File(filePath + fileName + suffix));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName + suffix);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        File file = new File(filePath + name);
        if (!file.exists()){
            return;
        }
        try (//输入流
             FileInputStream inputStream = new FileInputStream(filePath + name);
             //输出流
             ServletOutputStream outputStream = response.getOutputStream();
        ) {
            int len = 0;
            byte[] buffer = new byte[1024];     //一次读取1mb
            while ((len = inputStream.read(buffer)) != -1) {   //判断是否读取完毕
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }
            response.setContentType("image/jpeg");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
