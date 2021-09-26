package com.example;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author special.fy
 */
@SpringBootApplication
@NacosPropertySource(dataId = "mini-authz-server", autoRefreshed = true)
public class MiniAuthzServerApp {

    public static void main(String[] args) {
        SpringApplication.run(MiniAuthzServerApp.class, args);
    }
}
