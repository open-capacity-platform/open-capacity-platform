package com.open.capacity.autoconfigure.port;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;

import com.open.capacity.autoconfigure.port.props.RandomServerPortPropertySource;

/**
 * 用于监听随机端口问题
 * Created by ace on 2017/7/8.
 */
public class PortApplicationEnvironmentPreparedEventListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        event.getEnvironment().getPropertySources().addLast(new RandomServerPortPropertySource());
    }
}
