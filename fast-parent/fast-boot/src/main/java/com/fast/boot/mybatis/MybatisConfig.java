package com.fast.boot.mybatis;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.pagehelper.PageHelper;

@Configuration
@EnableTransactionManagement
@AutoConfigureAfter({DataSourceConfig.class})
public class MybatisConfig implements TransactionManagementConfigurer{

	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisConfig.class);
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	private MybatisProperties properties;
	
	@Bean(name={"sqlSessionFactory"})
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
	    LOGGER.debug("Registered Mybatis sqlSessionFactory");
	    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
	    bean.setDataSource(dataSource);
	    bean.setTypeAliasesPackage(this.properties.getModel());
	    //分页插件
	    PageHelper pageHelper = new PageHelper();
	    Properties pros = new Properties();
	    pros.setProperty("reasonable", "false");
	    pros.setProperty("supportMethodsArguments", "true");
	    pros.setProperty("returnPageInfo", "check");
	    pros.setProperty("params", "count=countSql");
	    pageHelper.setProperties(pros);
	    //添加插件
	    bean.setPlugins(new Interceptor[] { pageHelper });
	    //添加XML目录
	    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	    try
	    {
	      bean.setMapperLocations(resolver.getResources(this.properties.getXml()));
	      return bean.getObject();
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      throw new RuntimeException(e);
	    }
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
	    LOGGER.debug("Registered Mybatis SqlSessionTemplate");
	    return new SqlSessionTemplate(sqlSessionFactory);
	}
	  
	@Bean
	public PlatformTransactionManager annotationDrivenTransactionManager(){
	    LOGGER.debug("Registered Mybatis PlatformTransactionManager");
	    return new DataSourceTransactionManager(this.dataSource);
	}
}
