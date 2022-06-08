package net.ldauvilaire.sample.jms.configuration;

import java.util.Arrays;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.CachingConnectionFactory;

@Configuration
@PropertySource({ "classpath:application-test.properties" })
@ComponentScan(
	basePackages = { "net.ldauvilaire.sample" },
	excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = ApplicationConfiguration.class)
)
@EnableJms
public class ApplicationTestConfiguration {

	private static final String PROPERTY_NAME_JMS_BROKER_URL = "jms.broker.url";

	@Bean
	public CachingConnectionFactory connectionFactory(Environment env) {

		CachingConnectionFactory cachingConnectionFactory =  new CachingConnectionFactory();
		{
			ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
			connectionFactory.setBrokerURL(env.getProperty(PROPERTY_NAME_JMS_BROKER_URL));
			connectionFactory.setTrustedPackages(Arrays.asList("net.ldauvilaire.sample"));
			cachingConnectionFactory.setTargetConnectionFactory(connectionFactory);
		}

		return cachingConnectionFactory;
	}
}
