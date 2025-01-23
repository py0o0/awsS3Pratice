package com.example.demoawssdk.controller;

import com.example.demoawssdk.dto.ResDto;
import com.example.demoawssdk.service.AwsS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequestMapping("/api/s3")
@RestController
public class AwsS3Controller {
    @Autowired
    private AwsS3Service awsS3Service;

    @PostMapping("/upload")
    public ResDto upload(@RequestParam("file") MultipartFile file) {
        // IO -> Spring Boot -> aws sdk -> s3 -> sb
        String url = ""; // 업로드 결과
        try{
            // 1.업로드 처리
            url = awsS3Service.upload(file);
            // 2.응답

        }catch (Exception e){
            log.info("업로드 오류 " + e.getMessage());
        }
        return ResDto.builder()
                .url(url)
                .build();
    }
}
