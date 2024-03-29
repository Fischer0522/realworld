package com.fischer.api;


import com.fischer.api.exception.BizException;
import com.fischer.assistant.ResultType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("file")
public class FileApi {
    @Value("${web.upload-path}")
    private String uploadPath;

    private SimpleDateFormat sdf=new SimpleDateFormat("/yyyy/MM/dd/");


    @PostMapping
    public ResponseEntity uploadFiles(MultipartFile[] files, HttpServletRequest request){
        if (files.length==0){
            throw new BizException(HttpStatus.BAD_REQUEST,"文件上传失败");
        }
        List<String> fileNames=new LinkedList<>();
        for(MultipartFile file: files){

            String s = uploadFile(file, request);
            fileNames.add(s);
        }
        /*return ResponseEntity.ok(
                new HashMap<String,Object>(){
                    {
                        put("imageList",fileNames);
                    }
                }
        );*/
        return ResponseEntity.ok(new ResultType(HttpStatus.OK.value(), fileNames,"上传成功"));
    }
    private String uploadFile(MultipartFile file,HttpServletRequest request){
        String format=sdf.format(new Date());
        File folder=new File(uploadPath+format);
        if(!folder.isDirectory()){
            folder.mkdirs();
        }
        String oldName=file.getOriginalFilename();
        String newName= UUID.randomUUID()
                .toString()+oldName
                .substring(oldName.lastIndexOf("."),oldName.length());
        try {
            file.transferTo(new File(folder,newName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return request.getScheme()+"://"+request.getServerName()
                +":"+request.getServerPort()+"/img"+format+newName;

    }


}
