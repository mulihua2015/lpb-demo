package com.example.demo.access;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccessColumns {

    String value() default "";
}
