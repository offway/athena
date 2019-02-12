package cn.offway.athena.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.offway.athena.properties.QiniuProperties;

@Configuration
@EnableConfigurationProperties(QiniuProperties.class)
public class QiniuConfig {

}
