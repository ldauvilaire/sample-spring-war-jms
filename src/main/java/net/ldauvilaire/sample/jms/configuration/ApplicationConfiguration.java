package net.ldauvilaire.sample.jms.configuration;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jndi.JndiObjectFactoryBean;

@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "net.ldauvilaire.sample" })
@EnableJms
public class ApplicationConfiguration {

	@Bean
	PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	private static final String PROPERTY_NAME_JMS_CONNECTION_FACTORY_JNDI = "jms.jndi.connection.factory";

	@Bean
	public JndiObjectFactoryBean connectionFactory(Environment env) {
		JndiObjectFactoryBean connectionFactoryBean = new JndiObjectFactoryBean();
		connectionFactoryBean.setJndiName(env.getProperty(PROPERTY_NAME_JMS_CONNECTION_FACTORY_JNDI));
		return connectionFactoryBean;
	}

	@Bean
	public CachingConnectionFactory cachingConnectionFactory(Environment env, JndiObjectFactoryBean connectionFactoryBean) {
		CachingConnectionFactory cachingConnectionFactory =  new CachingConnectionFactory();
		ConnectionFactory connectionFactory = (ConnectionFactory) connectionFactoryBean.getObject();
		cachingConnectionFactory.setTargetConnectionFactory(connectionFactory);
		return cachingConnectionFactory;
	}
}
