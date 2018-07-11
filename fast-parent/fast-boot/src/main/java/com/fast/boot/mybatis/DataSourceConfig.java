package com.fast.boot.mybatis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

/**
 * Druid数据源配置
 */
@Configuration
@AutoConfigureAfter({MybatisProperties.class})
public class DataSourceConfig {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);
    
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource druidDataSource(
    		@Value("${spring.datasource.driver-class-name}") String driver,
            @Value("${spring.datasource.url}") String url,
            @Value("${spring.datasource.username}") String username,
            @Value("${spring.datasource.password}") String password,
            @Value("${spring.datasource.initialSize}") Integer initialSize,
            @Value("${spring.datasource.maxActive}") Integer maxActive,
            @Value("${spring.datasource.minIdle}") Integer minIdle,
            @Value("${spring.datasource.validationQuery}") String validationQuery,
            @Value("${mybatis.wall}") Boolean wall) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setValidationQuery(validationQuery);
        if (wall!=null&&wall) {
        	WallConfig wallConfig = new WallConfig ();
    	    wallConfig.setMultiStatementAllow(true);
            WallFilter wallFilter = new WallFilter();
            wallFilter.setConfig(wallConfig);
        	List<Filter> filters = new ArrayList<Filter>();
        	// 支持批量更新
            filters.add(wallFilter);
            druidDataSource.setProxyFilters(filters);
        }
        try {
            LOGGER.debug("Setting 'application.properties' into druid");
            druidDataSource.setFilters("stat, wall");
        } catch (SQLException e) {
            throw new IllegalStateException("Could not initial Druid DataSource\n" + e);
        }
        return druidDataSource;
    }
    
    /**
     * 添加数据源统计信息查看
     * 根据配置文件，判断是否执行
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix=MybatisProperties.MYBATIS_PREFIX, value="druid")
    public ServletRegistrationBean druidServlet() {
    	LOGGER.debug("register druid StatViewServlet");
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

    /**
     * 添加数据源统计信息查看
     * 根据配置文件，判断是否执行
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix=MybatisProperties.MYBATIS_PREFIX, value="druid")
    public FilterRegistrationBean druidFilter() {
    	LOGGER.debug("register druid filter");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
