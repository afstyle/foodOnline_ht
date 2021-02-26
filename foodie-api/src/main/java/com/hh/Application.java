package com.hh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author HuangHao
 * @date 2020/10/12 0:07
 */

@SpringBootApplication/*(exclude = {SecurityAutoConfiguration.class})*/
@MapperScan(basePackages = "com.hh.mapper")
// 扫描所有包和相关组件包
@ComponentScan(basePackages = {"com.hh", "org.n3r.idworker"})
@EnableScheduling       // 开启定时任务
//@EnableRedisHttpSession // 开启redis为spring session
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
