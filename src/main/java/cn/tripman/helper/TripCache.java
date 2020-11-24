package cn.tripman.helper;

import java.lang.annotation.*;

/**
 * @author hero
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TripCache {

    int expire() default 3600;

    String region() default "";

    String method() default "";

    String key() default "";


}
