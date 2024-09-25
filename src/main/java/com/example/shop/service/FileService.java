package com.example.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFilename, byte[] fileData) {
        UUID uuid = UUID.randomUUID();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;

        try {
            FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
            fos.write(fileData);
            fos.close();
            return savedFileName;
        } catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    public void deleteFile(String filePath) {
        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제하셨습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
