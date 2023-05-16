package com.ywzs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.ywzs.mapper")
@ServletComponentScan   //可以扫描过滤器
@EnableTransactionManagement //开启事务
public class WaimaiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WaimaiApplication.class, args);
    }

}
