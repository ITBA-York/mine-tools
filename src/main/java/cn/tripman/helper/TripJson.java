package cn.tripman.helper;

import java.lang.annotation.*;

/**
 * @author liuyu
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TripJson {

    String value() default "";

    boolean tSerialize() default true;
}
