package net.ldauvilaire.sample.jms.configuration;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import net.ldauvilaire.sample.jms.domain.model.Person;

@Configuration
public class DatabaseConfiguration {

	private static final String PROPERTY_NAME_DB_DRIVER_CLASS = "db.driver";
	private static final String PROPERTY_NAME_DB_PASSWORD = "db.password";
	private static final String PROPERTY_NAME_DB_URL = "db.url";
	private static final String PROPERTY_NAME_DB_USER = "db.username";

	private static final String PROPERTY_NAME_DB_MAX_POOL_SIZE = "db.max.pool.size";

	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
	private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	private static final String PROPERTY_NAME_HIBERNATE_BATCH_SIZE = "hibernate.jdbc.batch_size";
	private static final String PROPERTY_NAME_HIBERNATE_ORDER_INSERTS = "hibernate.order_inserts";
	private static final String PROPERTY_NAME_HIBERNATE_ORDER_UPDATES = "hibernate.order_updates";

	@Bean(destroyMethod = "close")
	DataSource dataSource(Environment env) throws IllegalStateException, PropertyVetoException {

		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		{
			dataSource.setDriverClass(env.getRequiredProperty(PROPERTY_NAME_DB_DRIVER_CLASS));
			dataSource.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DB_URL));
			dataSource.setUser(env.getRequiredProperty(PROPERTY_NAME_DB_USER));
			dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));

			Integer maxPoolSize = env.getRequiredProperty(PROPERTY_NAME_DB_MAX_POOL_SIZE, Integer.class);
			if (maxPoolSize != null) {
				dataSource.setMaxPoolSize(maxPoolSize);
			}
		}

		return dataSource;
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Environment env) {

		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		{
			entityManagerFactoryBean.setDataSource(dataSource);
			entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			entityManagerFactoryBean.setPackagesToScan(Person.class.getPackage().getName());

			Properties jpaProperties = new Properties();
			{
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_DIALECT, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_BATCH_SIZE, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_BATCH_SIZE));
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_ORDER_INSERTS, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ORDER_INSERTS));
				jpaProperties.put(PROPERTY_NAME_HIBERNATE_ORDER_UPDATES, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_ORDER_UPDATES));
			}
			entityManagerFactoryBean.setJpaProperties(jpaProperties);
		}

		return entityManagerFactoryBean;
	}

	@Bean
	JpaTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
