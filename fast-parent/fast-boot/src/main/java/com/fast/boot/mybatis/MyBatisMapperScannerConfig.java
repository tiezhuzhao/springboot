package com.fast.boot.mybatis;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import tk.mybatis.spring.mapper.MapperScannerConfigurer;

/**
 * MyBatis扫描接口
 */
@Configuration
@AutoConfigureAfter({MybatisConfig.class})
public class MyBatisMapperScannerConfig implements EnvironmentAware{

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisConfig.class);
	
	private RelaxedPropertyResolver propertyResolver;
	
	private String mapperPackage;
	
	public void setEnvironment(Environment environment){
	    this.propertyResolver = new RelaxedPropertyResolver(environment, "mybatis.");
	    this.mapperPackage = this.propertyResolver.getProperty("mapper");
	}
	  
	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer(){
		LOGGER.debug("Registered Mybatis MapperScannerConfigurer");
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		mapperScannerConfigurer.setBasePackage(this.mapperPackage);
		Properties props = new Properties();
		props.setProperty("mappers", "com.fast.boot.mybatis.bean.MyMapper");
		props.setProperty("notEmpty", "false");
		props.setProperty("IDENTITY", "MYSQL");
		mapperScannerConfigurer.setProperties(props);
		return mapperScannerConfigurer;
	}
}
