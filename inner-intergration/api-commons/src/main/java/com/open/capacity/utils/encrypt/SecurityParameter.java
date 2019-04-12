package com.open.capacity.utils.encrypt;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface SecurityParameter {
    boolean inDecode() default true;

    boolean outEncode() default true;
}
