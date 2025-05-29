package com.project.products.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
public class S3ImageUploader {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${app.s3.bucket}")
    public String bucketName;


    public String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("file is null");
        }
        String actualFilename = file.getOriginalFilename();
        String filename = UUID.randomUUID().toString() + actualFilename.substring(actualFilename.lastIndexOf("."));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, filename, file.getInputStream(),
                    objectMetadata));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filename;
    }

    public String preSignedUrl(String fileName) {
        Date expire=new Date();
        long time=expire.getTime();
        int hour=2;
        time=time+hour*60*60*1000;
        expire.setTime(time);
        GeneratePresignedUrlRequest generatePresignedUrlRequest=
                new GeneratePresignedUrlRequest(bucketName,fileName).
                        withMethod(HttpMethod.GET).withExpiration(expire);
        URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
