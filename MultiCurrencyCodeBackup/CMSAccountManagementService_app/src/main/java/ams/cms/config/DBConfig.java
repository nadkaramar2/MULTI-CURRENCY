package ams.cms.config;
/*
 * package com.cms.config;
 * 
 * 
 * import java.util.Properties;
 * 
 * import javax.persistence.EntityManagerFactory; import javax.sql.DataSource;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.boot.autoconfigure.AutoConfigureOrder; import
 * org.springframework.boot.autoconfigure.condition.ConditionalOnBean; import
 * org.springframework.boot.autoconfigure.condition.ConditionalOnClass; import
 * org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
 * import
 * org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
 * import
 * org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
 * import org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.context.annotation.PropertySource; import
 * org.springframework.core.Ordered; import
 * org.springframework.core.env.Environment; import
 * org.springframework.jdbc.datasource.DriverManagerDataSource; import
 * org.springframework.orm.hibernate5.HibernateTransactionManager; import
 * org.springframework.orm.jpa.JpaTransactionManager; import
 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean; import
 * org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean; import
 * org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
 * 
 * import com.zaxxer.hikari.HikariConfig; import
 * com.zaxxer.hikari.HikariDataSource;
 * 
 * @Configuration
 * 
 * @ConditionalOnClass(DataSource.class)
 * 
 * @AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
 * 
 * @PropertySource("classpath:dbconfig.properties") public class DBConfig {
 * 
 * @Autowired private Environment env;
 * 
 * @Bean
 * 
 * @ConditionalOnProperty(name = "mode", havingValue = "local")
 * 
 * @ConditionalOnMissingBean public DataSource dataSource() throws Exception {
 * final DriverManagerDataSource dataSource = new DriverManagerDataSource();
 * 
 * dataSource.setDriverClassName(env.getProperty("datasource.driver_class"));
 * dataSource.setUrl(env.getProperty("datasource.localurl"));
 * dataSource.setUsername(env.getProperty("datasource.localuser"));
 * dataSource.setPassword(env.getProperty("datasource.localpassword"));
 * 
 * HikariConfig config = new HikariConfig(); config.setDataSource(dataSource);
 * config.setConnectionTestQuery(env.getRequiredProperty(
 * "hikari.connectionTestQuery"));
 * config.setDataSourceClassName(env.getRequiredProperty(
 * "hikari.dataSourceClassName"));
 * config.setIdleTimeout(Long.parseLong(env.getRequiredProperty(
 * "hikari.idleTimeout")));
 * config.setMaximumPoolSize(Integer.parseInt(env.getRequiredProperty(
 * "hikari.maximumPoolSize")));
 * config.setConnectionTimeout(Long.parseLong(env.getRequiredProperty(
 * "hikari.connectionTimeout")));
 * config.setMaxLifetime(Long.parseLong(env.getRequiredProperty(
 * "hikari.maxLifetime"))); config.addDataSourceProperty("cachePrepStmts",
 * env.getRequiredProperty("hikari.dataSource.cachePrepStmts"));
 * config.addDataSourceProperty("prepStmtCacheSize",
 * env.getRequiredProperty("hikari.dataSource.prepStmtCacheSize"));
 * config.addDataSourceProperty("prepStmtCacheSqlLimit",
 * env.getRequiredProperty("hikari.dataSource.prepStmtCacheSqlLimit"));
 * config.addDataSourceProperty("useServerPrepStmts",
 * env.getRequiredProperty("hikari.dataSource.useServerPrepStmts"));
 * config.setPoolName("LocalTxn");
 * 
 * final HikariDataSource datasource = new HikariDataSource(config);
 * 
 * return datasource; }
 * 
 * @Bean
 * 
 * @ConditionalOnBean(name = "dataSource")
 * 
 * @ConditionalOnMissingBean public LocalContainerEntityManagerFactoryBean
 * entityManagerFactory() throws Exception { final
 * LocalContainerEntityManagerFactoryBean em = new
 * LocalContainerEntityManagerFactoryBean(); em.setDataSource(dataSource());
 * em.setPackagesToScan("ams.cms.model"); em.setJpaVendorAdapter(new
 * HibernateJpaVendorAdapter()); em.setJpaProperties(hibernateProperties());
 * return em; }
 * 
 * 
 * @Bean
 * 
 * @ConditionalOnMissingBean(type = "JpaTransactionManager")
 * JpaTransactionManager transactionManager(final EntityManagerFactory
 * entityManagerFactory) { final JpaTransactionManager transactionManager = new
 * JpaTransactionManager();
 * transactionManager.setEntityManagerFactory(entityManagerFactory); return
 * transactionManager; }
 * 
 * @ConditionalOnResource(resources = "classpath:dbconfig.properties") final
 * Properties hibernateProperties() { final Properties hibernateProperties = new
 * Properties();
 * 
 * hibernateProperties.setProperty("hibernate.hbm2ddl.auto",
 * env.getProperty("hibernate.hbm2ddl.auto"));
 * hibernateProperties.setProperty("hibernate.dialect",
 * env.getProperty("hibernate.dialect"));
 * hibernateProperties.setProperty("hibernate.show_sql",
 * env.getProperty("hibernate.show_sql") != null ?
 * env.getProperty("hibernate.show_sql") : "false");
 * hibernateProperties.setProperty("hibernate.current_session_context_class",
 * env.getProperty("hibernate.current_session_context_class"));
 * //hibernateProperties.put("hibernate.connection.pool_size",
 * env.getRequiredProperty("hibernate.connection.pool_size"));
 * //hibernateProperties.put("hibernate.connection.release_mode",
 * env.getRequiredProperty("hibernate.connection.release_mode"));
 * //hibernateProperties.put("hibernate.jdbc.batch_size",
 * env.getRequiredProperty("hibernate.jdbc.batch_size"));
 * hibernateProperties.put("hibernate.connection.autotocommit",
 * env.getRequiredProperty("hibernate.connection.autotocommit"));
 * hibernateProperties.put("hibernate.cache.use_second_level_cache",
 * env.getRequiredProperty("hibernate.cache.use_second_level_cache"));
 * 
 * return hibernateProperties; }
 * 
 * @Bean public HibernateJpaSessionFactoryBean sessionFactory() { return new
 * HibernateJpaSessionFactoryBean(); }
 * 
 * @Bean public HibernateTransactionManager transactionManager() {
 * HibernateTransactionManager transactionManager = new
 * HibernateTransactionManager();
 * transactionManager.setSessionFactory(sessionFactory().getObject()); return
 * transactionManager; }
 * 
 * }
 */