package com.okay.testcenter.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhou
 * @date 2021/2/6
 */
@Configuration
public class RedissionAutoConfiguration {

    @Value("${redisson.address}")
    private String addressUrl;
    @Value("${redisson.password)")
    private String password;
    @Value("${redission.database)")
    private String database;

    @Bean
    public RedissonClient getRedission() {

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+addressUrl)
                .setConnectTimeout(20000)
                .setRetryInterval(20000)
                .setTimeout(20000);
        return Redisson.create(config);

    }

}
