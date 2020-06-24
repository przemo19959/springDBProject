package application.configs.root;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@Profile("test")
@PropertySource("classpath:test-jdbc.properties")
public class TestConfig {

	@Autowired
	private Environment env;

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		return dataSource;
	}

	@Bean
	public SessionFactory sessionFactory() throws IOException {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource());
		sessionFactoryBean.setPackagesToScan("application.entities");
		sessionFactoryBean.setHibernateProperties(hibernateProperties());
		sessionFactoryBean.afterPropertiesSet();
		return sessionFactoryBean.getObject();
	}

	private Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProp.put("hibernate.max_fetch_depth", env.getProperty("hibernate.max_fetch_depth"));
		hibernateProp.put("hibernate.jdbc.fetch_size", env.getProperty("hibernate.jdbc.fetch_size"));
		hibernateProp.put("hibernate.jdbc.batch_size", env.getProperty("hibernate.jdbc.batch_size"));
		hibernateProp.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		hibernateProp.put("hibernate.formate_sql", env.getProperty("hibernate.formate_sql"));
		hibernateProp.put("hibernate.use_sql_comments", env.getProperty("hibernate.use_sql_comments"));
		hibernateProp.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		return hibernateProp;
	}	
}
