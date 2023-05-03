package com.czh.reggie.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@ServletComponentScan 这个注解可以扫描到过滤器
@Slf4j
@SpringBootApplication
@ServletComponentScan
public class DemoApplication {

	public static void main(String[] args) {


		SpringApplication.run(DemoApplication.class, args);
		log.info("DemoApplication");
	}

}
