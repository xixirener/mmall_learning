package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
 * Created by Administrator on 2017/7/30.
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService{

    private org.slf4j.Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    //文件上传
    public String upload(MultipartFile file,String path){
        String fileName= file.getOriginalFilename();
        //扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件，上传文件的文件名：{},上传的路径：{}，新文件名：{}",fileName,path,uploadFileName);
        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try {
            //文件已经上传成功
            file.transferTo(targetFile);

            //将targetFile上传到FTP文件服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));

            //上传完后删除upload下面的文件
            targetFile.delete();

        } catch (IOException e) {
            logger.error("上传文件异常",e);
            return  null;
        }
        return targetFile.getName();
    }
}
