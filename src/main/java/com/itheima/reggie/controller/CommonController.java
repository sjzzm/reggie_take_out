package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
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

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String BasePath;


    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){


        //原先的名字
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名，防止文件名重复造成文件覆盖
        String filename = UUID.randomUUID().toString() + substring;

        File file1 = new File(BasePath);

        if(!file1.exists()){
            file1.mkdir();
        }

        try {
            file.transferTo(new File(BasePath+filename));
        } catch (IOException e) {
            e.printStackTrace();
        }


        return R.success(filename);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){


        try {
            //输入流，通过输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(new File(BasePath + name));


            //输出流，将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            int length = 0;
            byte[] bytes = new byte[1024];
            while ((length = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
