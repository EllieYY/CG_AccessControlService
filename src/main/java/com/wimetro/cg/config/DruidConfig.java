package com.wimetro.cg.config;

import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @title: DruidConfig
 * @author: Ellie
 * @date: 2022/06/08 16:31
 * @description:
 **/
@Configuration
public class DruidConfig {
    /**
     * 配置允许批量SQL
     * @return
     */
    @Bean
    public WallFilter wallFilter() {

        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(wallConfig());
        return wallFilter;
    }

    @Bean
    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        //允许一次执行多条语句
        config.setMultiStatementAllow(true);
        //允许非基本语句的其他语句
        config.setNoneBaseStatementAllow(true);
        return config;
    }
}
