//package com.sparta.catchme.configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000", "https://catch-me-if-you-can-ten.vercel.app/", "https://dwrxcg0uq83xr.cloudfront.net/")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .exposedHeaders("*")
//                .exposedHeaders("Authorization", "Access-Token-Expire-Time")
//                .allowCredentials(true)
//                .maxAge(3000);
//    }
//}
