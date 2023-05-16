package com.ywzs.config;


import com.ywzs.common.JacksonObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Configuration
public class webMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 静态资源映射
     * @param registry 资源处理
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展消息转换器
     * @param converters  转换器
     */
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //使用jackson对象转换器
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //把自定义的转换器追加到springmvc消息转化器的集合中
        converters.add(0,messageConverter);
    }
}
