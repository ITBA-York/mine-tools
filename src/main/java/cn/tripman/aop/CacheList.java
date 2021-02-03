package cn.tripman.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CacheList {

    String key();

    String method();

    int expire() default 3600;
}
