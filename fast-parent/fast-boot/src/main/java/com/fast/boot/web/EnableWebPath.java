package com.fast.boot.web;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({WebPathProperties.class})
@Documented
public @interface EnableWebPath {

}
