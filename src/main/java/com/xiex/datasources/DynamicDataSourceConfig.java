package com.xiex.datasources;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import javax.transaction.SystemException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置多数据源
 *
 * @author xiex
 * @email 1487471733@qq.com
 * @date 2020/8/5 0:41
 */
@Configuration
public class DynamicDataSourceConfig  implements EnvironmentAware, ImportBeanDefinitionRegistrar {
    public static final String basePackage ="spring.datasource.druid.";
    @Value("${spring.datasource.type:com.alibaba.druid.pool.xa.DruidXADataSource}")
    String xaDataSourceClassName;
    //@Value("${spring.datasource.druid.defaultDataSource}")
    public static String defaultDataSource;


    private final Map<String, AtomikosDataSourceBean> dataSourceMap = new LinkedHashMap<>();

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        dataSourceMap.forEach((k,v) -> {
            BeanDefinitionBuilder dataSourceSimpleBuilder = BeanDefinitionBuilder.genericBeanDefinition(AtomikosDataSourceBean.class, () -> {
                return v;
            });
            AbstractBeanDefinition beanDefinition = dataSourceSimpleBuilder.getBeanDefinition();
            if (k.contains("big")) {
                beanDefinition.setPrimary(true);
            }
            beanDefinitionRegistry.registerBeanDefinition(k + "DataSource", beanDefinition);

        });
    }

    @Override
    public void setEnvironment(Environment environment) {
        //String sourceName = "bigdata,plat";
        String property = environment.getProperty(basePackage + "names", String.class);
        Assert.notNull(property, "数据源组名不可为空");
        String defaultDataSource = environment.getProperty(basePackage + "defaultDataSource", String.class);
        if (!StringUtils.isEmpty(defaultDataSource)) {
            DynamicDataSourceConfig.defaultDataSource = defaultDataSource;
        }
        else {
            DynamicDataSourceConfig.defaultDataSource = property.split(",")[0];
        }
        for (String name : property.split(",")) {
            if (!StringUtils.isEmpty(name)) {
                Properties prop = build(environment, basePackage+name+".");
                AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
                xaDataSource.setXaDataSourceClassName(xaDataSourceClassName);
                xaDataSource.setUniqueResourceName(name);
                xaDataSource.setPoolSize(5);
                xaDataSource.setXaProperties(prop);
                dataSourceMap.put(name, xaDataSource);
            }
        }

       // return xaDataSource;
    }

    // 配置数据源
    @Bean(name = "dynamicDataSource")
    public DynamicDataSource dataSource() {
        Map<Object, Object> targetDataSources = new LinkedHashMap<>();
        dataSourceMap.forEach((k, v) -> {
            targetDataSources.put(k, v);
        });
        AtomikosDataSourceBean atomikosDataSourceBean = dataSourceMap.get(defaultDataSource);
        Assert.notNull(atomikosDataSourceBean,"spring.datasource.druid.defaultDataSource对应的数据源不在数据源组名内，请检查");
        return new DynamicDataSource(atomikosDataSourceBean, targetDataSources);
    }
    @Primary
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamicDataSource") DataSource dataSource)
            throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTransactionFactory(new MultiDataSourceTransactionFactory());
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/**/*Dao.xml"));// 扫描指定目录的xml
        return bean.getObject();
    }
    @Bean(name="sqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 分布式事务使用JTA管理，不管有多少个数据源只要配置一个 JtaTransactionManager
     * @return
     */
    /*atomikos事务管理器*/
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    public UserTransactionImp userTransactionImp() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(5000);
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager jtaTransactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionImp());
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }


    private Properties build(Environment env, String prefix) {

        Properties prop = new Properties();
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driverClassName", ""));
        prop.put("initialSize", env.getProperty(prefix + "initialSize", Integer.class));
        prop.put("maxActive", env.getProperty(prefix + "maxActive", Integer.class));
        prop.put("minIdle", env.getProperty(prefix + "minIdle", Integer.class));
        prop.put("maxWait", env.getProperty(prefix + "maxWait", Integer.class));
        prop.put("poolPreparedStatements", env.getProperty(prefix + "poolPreparedStatements", Boolean.class));

        prop.put("maxPoolPreparedStatementPerConnectionSize",
                env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));

        prop.put("maxPoolPreparedStatementPerConnectionSize",
                env.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class));
        prop.put("validationQuery", env.getProperty(prefix + "validationQuery"));
        prop.put("validationQueryTimeout", env.getProperty(prefix + "validationQueryTimeout", Integer.class));
        prop.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "testOnReturn", Boolean.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle", Boolean.class));
        prop.put("timeBetweenEvictionRunsMillis",
                env.getProperty(prefix + "timeBetweenEvictionRunsMillis", Integer.class));
        prop.put("minEvictableIdleTimeMillis", env.getProperty(prefix + "minEvictableIdleTimeMillis", Integer.class));
        prop.put("filters", env.getProperty(prefix + "filters"));

        return prop;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");

        //控制台管理用户，加入下面2行 进入druid后台就需要登录
        //servletRegistrationBean.addInitParameter("loginUsername", "admin");
        //servletRegistrationBean.addInitParameter("loginPassword", "admin");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true); //slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setMergeSql(true); //SQL合并配置
        statFilter.setSlowSqlMillis(1000);//slowSqlMillis的缺省值为3000，也就是3秒。
        return statFilter;
    }

    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }
}
