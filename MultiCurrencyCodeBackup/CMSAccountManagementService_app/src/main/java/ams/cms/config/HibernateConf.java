package ams.cms.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@PropertySource("classpath:dbconfig.properties")
public class HibernateConf 
{
	@Autowired
	private Environment env;
	
	@Bean
    public LocalSessionFactoryBean sessionFactory()
	{
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        try
        {
			sessionFactory.setDataSource(dataSource());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //sessionFactory.setPackagesToScan("ams.cms.model");
        sessionFactory.setPackagesToScan("ams.cms.*");
        sessionFactory.setHibernateProperties(hibernateProperties());
        
        //System.out.println("sessionFactory::"+sessionFactory);
        return sessionFactory;
    }
	
	@Bean	
	@ConditionalOnProperty(name = "mode", havingValue = "local")
	@ConditionalOnMissingBean
	public DataSource dataSource() throws Exception {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getProperty("datasource.driver_class"));
		dataSource.setUrl(env.getProperty("datasource.localurl"));
		dataSource.setUsername(env.getProperty("datasource.localuser"));
		dataSource.setPassword(env.getProperty("datasource.localpassword"));

		HikariConfig config = new HikariConfig();
		config.setDataSource(dataSource);
		config.setConnectionTestQuery(env.getRequiredProperty("hikari.connectionTestQuery"));
		config.setDataSourceClassName(env.getRequiredProperty("hikari.dataSourceClassName"));
		config.setIdleTimeout(Long.parseLong(env.getRequiredProperty("hikari.idleTimeout")));
		config.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty("hikari.maximumPoolSize")));		
		config.setConnectionTimeout(Long.parseLong(env.getRequiredProperty("hikari.connectionTimeout")));
		config.setMaxLifetime(Long.parseLong(env.getRequiredProperty("hikari.maxLifetime")));
		config.addDataSourceProperty("cachePrepStmts", env.getRequiredProperty("hikari.dataSource.cachePrepStmts"));
		config.addDataSourceProperty("prepStmtCacheSize", env.getRequiredProperty("hikari.dataSource.prepStmtCacheSize"));
		config.addDataSourceProperty("prepStmtCacheSqlLimit", env.getRequiredProperty("hikari.dataSource.prepStmtCacheSqlLimit"));
		config.addDataSourceProperty("useServerPrepStmts", env.getRequiredProperty("hikari.dataSource.useServerPrepStmts"));
		config.setPoolName("LocalTxn");
		
		final HikariDataSource datasource = new HikariDataSource(config);
		
		return datasource;		
	}
	
	 private final Properties hibernateProperties() 
	 {
		 /*
	        Properties hibernateProperties = new Properties();
	        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
	        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));

	        return hibernateProperties;
	        */
		 	final Properties hibernateProperties = new Properties();

			hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
			hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
			hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql") != null
					? env.getProperty("hibernate.show_sql") : "false");
			hibernateProperties.setProperty("hibernate.current_session_context_class", env.getProperty("hibernate.current_session_context_class"));
			//hibernateProperties.put("hibernate.connection.pool_size", env.getRequiredProperty("hibernate.connection.pool_size"));
			//hibernateProperties.put("hibernate.connection.release_mode", env.getRequiredProperty("hibernate.connection.release_mode"));
			//hibernateProperties.put("hibernate.jdbc.batch_size", env.getRequiredProperty("hibernate.jdbc.batch_size"));
			hibernateProperties.put("hibernate.connection.autotocommit", env.getRequiredProperty("hibernate.connection.autotocommit"));
			hibernateProperties.put("hibernate.cache.use_second_level_cache", env.getRequiredProperty("hibernate.cache.use_second_level_cache"));
	        		
			return hibernateProperties;
	 }
	 
	@Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
	
	@Bean(name="jdbcTemplate")	
	@Autowired
	public JdbcTemplate getJdbcTemplate()
	{
		//System.out.println("---- Creating jdbc template ------");
		try 
		{
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
			//System.out.println("jdbcTemplate::"+jdbcTemplate);
			return jdbcTemplate;
		}
		catch (Exception e) {
			System.out.println("Exception during getJdbcTemplate::"+e);
			e.printStackTrace();
		}
		return null;
	}
}
