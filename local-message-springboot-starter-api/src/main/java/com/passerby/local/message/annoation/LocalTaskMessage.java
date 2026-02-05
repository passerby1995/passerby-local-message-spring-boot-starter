package com.passerby.local.message.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: sunjunyao
 * @Date: 2026/1/6 14:46
 * @Descrpition:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface LocalTaskMessage {

    String value() default "";
}
