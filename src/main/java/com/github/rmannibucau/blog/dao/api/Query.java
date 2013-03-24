package com.github.rmannibucau.blog.dao.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target( { METHOD })
@Retention(RUNTIME)
public @interface Query {
    boolean needTransaction() default true;
    String countQuery() default "";
}
