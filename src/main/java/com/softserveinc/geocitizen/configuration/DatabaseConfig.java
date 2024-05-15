/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableSpringDataWebSupport
@EnableTransactionManagement
@ComponentScan(basePackages = "com.softserveinc.geocitizen")
@EnableJpaRepositories("com.softserveinc.geocitizen.repository")
@PropertySource("classpath:application.properties")
public class DatabaseConfig {

	private static final String HIBERNATE_DIALECT_PROP_NAME = "hibernate.dialect";
	private static final String HIBERNATE_SHOW_SQL_PROP_NAME = "hibernate.show_sql";

	@Value("${db.driver}")
	private String databaseDriver;

	@Value("${db.password}")
	private String databasePass;

	@Value("${db.url}")
	private String databaseUrl;

	@Value("${db.username}")
	private String databaseUsername;

	@Value("${" + HIBERNATE_DIALECT_PROP_NAME + "}")
	private String hibernateDialect;

	@Value("${" + HIBERNATE_SHOW_SQL_PROP_NAME + "}")
	private String hibernateShowSql;

	@Value("${entity_manager.packages.to.scan}")
	private String entityManagerPackagesToScan;

	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();

		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setDataSource(getDataSource());
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.setPackagesToScan(entityManagerPackagesToScan);
		entityManagerFactoryBean.setJpaProperties(getHibProperties());
		return entityManagerFactoryBean;
	}

	@Bean
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(databaseDriver);
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername(databaseUsername);
		dataSource.setPassword(databasePass);
		return dataSource;
	}

	private Properties getHibProperties() {
		Properties properties = new Properties();

		properties.put(HIBERNATE_DIALECT_PROP_NAME, hibernateDialect);
		properties.put(HIBERNATE_SHOW_SQL_PROP_NAME, hibernateShowSql);
		return properties;
	}

	@Bean
	public SpringLiquibase liquibase() {
		SpringLiquibase liquibase = new SpringLiquibase();

		liquibase.setChangeLog("classpath:liquibase/mainChangeLog.xml");
		liquibase.setDataSource(getDataSource());
		return liquibase;
	}
}
