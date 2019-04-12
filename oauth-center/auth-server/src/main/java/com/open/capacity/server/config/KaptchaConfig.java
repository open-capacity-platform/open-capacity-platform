package com.open.capacity.server.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author zlt
 * 生成验证码配置
 * @date 2018-12-21 21:12:18
 */
@Configuration
public class KaptchaConfig {
    private static final String KAPTCHA_BORDER = "kaptcha.border";
    private static final String KAPTCHA_TEXTPRODUCER_FONT_COLOR = "kaptcha.textproducer.font.color";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_SPACE = "kaptcha.textproducer.char.space";
    private static final String KAPTCHA_IMAGE_WIDTH = "kaptcha.image.width";
    private static final String KAPTCHA_IMAGE_HEIGHT = "kaptcha.image.height";
    private static final String KAPTCHA_TEXTPRODUCER_CHAR_LENGTH = "kaptcha.textproducer.char.length";
    private static final Object KAPTCHA_IMAGE_FONT_SIZE = "kaptcha.textproducer.font.size";

    @Bean
    public DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put(KAPTCHA_BORDER, "no");
        properties.put(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, 5);
        properties.put(KAPTCHA_IMAGE_WIDTH, "100");
        properties.put(KAPTCHA_IMAGE_HEIGHT, "35");
        properties.put(KAPTCHA_IMAGE_FONT_SIZE, "30");
        properties.put(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
