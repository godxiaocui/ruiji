package com.czh.reggie.demo.controller;

import com.czh.reggie.demo.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 上传下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
@Value("${reggie.path}")
    private String basePath;
    /**
     * 上传的参数
     * 1. springboot 自带的分装 必须要叫MultipartFile file 参数类型和参数名称不变
     * 2. 获取文件名称、
     * 3. 获取原始文件目录
     * 4. uuid生成新的文件名
     * 5， 文件存储
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info(file.toString());
        // 原始文件名称(XXXX.jgp)
        String originalFilename = file.getOriginalFilename();
        // 代表目录
        File dir = new File(basePath);
        if (!dir.exists()){
        // 创建目录
            dir.mkdirs();
        }
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 使用uuid重新生产文件名
        String s = UUID.randomUUID().toString();
        String filename=s+substring;


        // 文件file是一个临时文件，需要转存到指定位置，否则本次请求临时文件会删除
        file.transferTo(new File(basePath+filename));
        return R.success(filename);
    }
    /**
     * 下载工作
     * 1.输入流获取文件内容
     * 2.定义输出流
     *  a. 输出的类型
     *  b. 标准输出流
     * 3.输出流回写
     * 4. 关闭输出流和输出
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        // 输入流获取文件内容
        File file = new File(basePath + name);
        try {
            //输入流获取文件内容
            FileInputStream fileInputStream = new FileInputStream(file);
            // 2.定义输出流
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            // 开始写出;
            int len =0;
            byte[] bytes = new byte[1024];
            // 循环输出，直到输出流是0
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            // 关闭输出流
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
