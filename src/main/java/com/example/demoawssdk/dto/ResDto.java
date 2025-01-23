package com.example.demoawssdk.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ResDto {
    private String url;
    @Builder
    public ResDto(String url) {
        this.url = url;
    }
}
