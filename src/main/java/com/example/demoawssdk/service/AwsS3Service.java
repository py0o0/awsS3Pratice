package com.example.demoawssdk.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

@Service
public class AwsS3Service {
    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @SneakyThrows
    public String upload(MultipartFile file) {
        if(file.isEmpty() || Objects.isNull(file.getOriginalFilename())) {
            throw new Exception("파일이 비어있음");
        }
        return checkAndUpload(file);
    }

    private String checkAndUpload(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();

        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1); //확장자

        String s3UploadName = UUID.randomUUID().toString().substring(0,12) + "-" + originalFileName; //저장되는 파일명

        String url = "";
        try {
            // 파일 -> 스트림구성(ByteArrayInputStream)
            InputStream is = file.getInputStream();
            byte[] bytes = IOUtils.toByteArray(is);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/" + ext);
            metadata.setContentLength(bytes.length);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            PutObjectRequest obj = new PutObjectRequest(
                    bucketName,     // 버킷 이름
                    s3UploadName,   // 업로드 될 파일명
                    byteArrayInputStream,             // 실 데이터를 전달할 스트림 -> 업로드된 파일과 연결, 다양하게 api 제공함
                    metadata);      // 메타 정보(파일)
            amazonS3.putObject(obj);// 업로드

            byteArrayInputStream.close();

            url = amazonS3.getUrl(bucketName,s3UploadName).toString();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        return url;
    }
}
