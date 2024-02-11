package com.example.dw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.dir}")
    private String goodsImgPath;
    @Value("${file.que}")
    private String questionPath;
    @Value("${file.free}")
    private String freeBoardPath;
    @Value("${file.review}")
    private String reviewPath;
    @Value("${file.user}")
    private String userFilePath;
    @Value("${file.pet}")
    private String petImgPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //addResourceHandlers() 리소스 경로와 연결된 URL경로를 작성한다.
        //리소스는 자원(이미지)
        registry.addResourceHandler("/jpa_dw/**")
                .addResourceLocations("file:" + goodsImgPath)
                .addResourceLocations("file:" + questionPath)
                .addResourceLocations("file:" + freeBoardPath)
                .addResourceLocations("file:"+ userFilePath)
                .addResourceLocations("file:"+petImgPath)
                .addResourceLocations("file:"+reviewPath);

        //로컬 디스크 경로는 file: 을 반드시 사용해야한다.
        registry.addResourceHandler("/common/**")
                .addResourceLocations("classpath:/static/common/");
    }
}
