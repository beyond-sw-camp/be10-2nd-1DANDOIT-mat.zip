package com.matzip.matzipback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // web root가 아닌 외부 경로에 있는 리소스를 url로 불러올 수 있도록 설정
    // 현재 localhost:8000/back/toastImage/1234.jpg
    // 로 접속하면 /Users/jiyoung/Desktop/toast_image/1234.jpg 파일을 불러온다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/back/toastImage/**")
                .addResourceLocations("file:///Users/jiyoung/Desktop/toast_image/");    // MacBook
             //   .addResourceLocations("file:///C:/toast_image/");                     // Windows 사용시 세팅 경로
    }
}
