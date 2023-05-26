package com.wimetro.cg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @title: WebMvcConfiguration
 * @author: Ellie
 * @date: 2022/03/29 15:36
 * @description:
 **/
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        //将所有/static/** 访问都映射到classpath:/static/ 目录下
        registry.addResourceHandler("/static/**")
                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX +"/static/");

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/META-INF/resources/");

        super.addResourceHandlers(registry);

    }
}
