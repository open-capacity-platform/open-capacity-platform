package com.open.capacity.config;

import org.flowable.engine.common.impl.persistence.StrongUuidGenerator;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * 配置主键生成策略
 * @author persie
 * @date 2018/07/19
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {


    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName("宋体");
        engineConfiguration.setLabelFontName("宋体");
        engineConfiguration.setAnnotationFontName("宋体");
        StrongUuidGenerator uuidGenerator = new StrongUuidGenerator();
        engineConfiguration.setIdGenerator(uuidGenerator) ;
    }
}
