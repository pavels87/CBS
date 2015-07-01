package com.cbs.controller.utils.instant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * User: PSpiridonov
 * Date: 25.05.15
 * Time: 16:18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD})
public @interface DatePattern {
    String value() default "";
}
