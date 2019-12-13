package application.configs;

import java.io.IOException;
import java.sql.Driver;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages= {"application.dao"})
@PropertySource("classpath:jdbc.properties")
@EnableTransactionManagement
public class DBServiceConfig {
	
	@Value(value = "${jdbc.driverClassName}")
	private String driverClassName;
	@Value(value = "${jdbc.url}")
	private String url;
	@Value(value = "${jdbc.username}")
	private String username;
	@Value(value = "${jdbc.password}")
	private String password;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public DataSource dataSource() {
		try {
			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			@SuppressWarnings("unchecked")
			Class<? extends Driver> driver = (Class<? extends Driver>) Class.forName(driverClassName);
			dataSource.setDriverClass(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		} catch (Exception e) {
			return null;
		}
	}

	private Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put("dialect", "org.hibernate.dialect.PostgreSQLDialect");
		hibernateProp.put("max_fetch_depth", 3);
		hibernateProp.put("jdbc.fetch_size", 50);
		hibernateProp.put("jdbc.batch_size", 10);
		hibernateProp.put("show_sql", false);
		hibernateProp.put("formate_sql", false);
		hibernateProp.put("use_sql_comments", false);
		hibernateProp.put("hibernate.hbm2ddl.auto","update");	//dziêki temu automatycznie przy tworzeniu fabryki sesji, tworzone s¹ tabele
		return hibernateProp;
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

	@Bean
	public PlatformTransactionManager transactionManager() throws IOException {
		return new HibernateTransactionManager(sessionFactory());
	}
}
