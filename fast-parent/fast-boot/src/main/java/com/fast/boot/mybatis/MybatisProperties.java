package com.fast.boot.mybatis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis配置项
 */
@Configuration
@ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
public class MybatisProperties {
	
    public final static String MYBATIS_PREFIX = "mybatis";

    private String model;// model路径
    private String mapper;// dao包路径
    private String xml;// mapper配置文件路径
    private Boolean druid = false;// 是否显示Druid数据源webui监控页面
    private Boolean wall = false;// 是否开启批量更新
    
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getMapper() {
		return mapper;
	}
	public void setMapper(String mapper) {
		this.mapper = mapper;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public Boolean getDruid() {
		return druid;
	}
	public void setDruid(Boolean druid) {
		this.druid = druid;
	}
	public Boolean getWall() {
		return wall;
	}
	public void setWall(Boolean wall) {
		this.wall = wall;
	}

}

