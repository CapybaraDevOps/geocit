package com.softserveinc.geocitizen.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@EnableTransactionManagement
@ComponentScan(basePackages = "com.softserveinc.geocitizen")
@EnableJpaRepositories("com.softserveinc.geocitizen.repository")
@PropertySource("classpath:testdb.properties")
public class TestDatabaseConfig {

	@Value("${h2-jdbc.driverClassName}")
	private String databaseDriver;

	@Value("${h2-jdbc.url}")
	private String databaseUrl;

	@Value("${h2-jdbc.username}")
	private String databaseUsername;

	@Value("${h2-jdbc.password}")
	private String databasePass;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(databaseDriver);
		dataSource.setUrl(databaseUrl);
		dataSource.setUsername(databaseUsername);
		dataSource.setPassword(databasePass);

		return dataSource;
	}
}
