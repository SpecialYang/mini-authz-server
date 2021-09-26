package com.example.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author special.fy
 */
@Configuration
@ConfigurationProperties(prefix = "mini-authz-server.settings")
@Data
public class Config {

    private String salt = "test";

}
