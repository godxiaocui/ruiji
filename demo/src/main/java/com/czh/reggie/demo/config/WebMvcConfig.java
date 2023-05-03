package com.czh.reggie.demo.config;

import com.czh.reggie.demo.common.JacksonObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * 这个类的作用就是，因为我的静态资源需要放在static和templates下，
     * 如果需要访问某一个特地文件夹就需要单独配置
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("静态资源映射");
        // addResourceHandler 表示前端需要访问的页面
        // addResourceLocations 后端文件放的路径
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    /**
     * 扩展mvc框架的消息转换器
     * 1. 创建消息转换器
     * 2. 设置对象转换器，底层用jackson将java对象转为json
     * 3. 将这个对象转换器，追到mvc的容器中，追加的时候注意序号，需要为0放在第一个
     */

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter=new MappingJackson2HttpMessageConverter();
        // 设置对象转换器，底层用jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将这个对象转换器，追到mvc的容器中，追加的时候注意序号，需要为0放在第一个
        converters.add(0,messageConverter);

    }
}
