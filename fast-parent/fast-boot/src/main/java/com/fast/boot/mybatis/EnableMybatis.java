package com.fast.boot.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

import org.springframework.context.annotation.Import;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({MybatisProperties.class, DataSourceConfig.class, MybatisConfig.class, MyBatisMapperScannerConfig.class})
@Documented
public @interface EnableMybatis {

}
