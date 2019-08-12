package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class UploadController {

    @Value("${upload.path}")
    private String uploadPath;

    public String upload(@RequestParam("file")MultipartFile file){
        if (file.isEmpty()) {
            return "上传失败，请重新上传";
        }
        String fileName = file.getOriginalFilename();

        File fileDir = new File(uploadPath);

        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        String filePath = uploadPath;

        File dest = new File(filePath + fileName);

        try {
            file.transferTo(dest);
            return "文件上传成功，文件路径" + uploadPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";

    }


    @PostMapping("/multiUpload")
    public String multiUpload(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = uploadPath;
        for (int i = 0; i <files.size() ; i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "第"+i+"个文件上传失败";
            }

            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);

            try {
                file.transferTo(dest);
            } catch (IOException e) {
                return "第"+i+"个文件上传失败";
            }
        }
        return "文件上传成功,文件路径" + uploadPath;
        
    }

}
