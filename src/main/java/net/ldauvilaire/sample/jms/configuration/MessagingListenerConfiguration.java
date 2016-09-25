package net.ldauvilaire.sample.jms.configuration;

import javax.jms.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;

import net.ldauvilaire.sample.jms.MessagingConstants;
import net.ldauvilaire.sample.jms.messaging.converter.JsonPersonMessageConverter;
import net.ldauvilaire.sample.jms.messaging.converter.XmlPersonMessageConverter;

@Configuration
@PropertySource("classpath:application.properties")
public class MessagingListenerConfiguration {

	private static final String PROPERTY_NAME_JMS_CONTAINER_MIN_THREAD = "jms.container.min.thread";
	private static final String PROPERTY_NAME_JMS_CONTAINER_MAX_THREAD = "jms.container.max.thread";

	@Autowired
	private ConnectionFactory connectionFactory;

	@Autowired
	private JsonPersonMessageConverter jsonPersonMessageConverter;

	@Autowired
	private XmlPersonMessageConverter xmlPersonMessageConverter;

	@Bean(name=MessagingConstants.JMS_CONTAINER_FIRST_FACTORY)
	public DefaultJmsListenerContainerFactory jmsFirstContainerFactory(Environment env) {

		Integer minThread = env.getProperty(PROPERTY_NAME_JMS_CONTAINER_MIN_THREAD, Integer.class);
		Integer maxThread = env.getProperty(PROPERTY_NAME_JMS_CONTAINER_MAX_THREAD, Integer.class);

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		{
			factory.setConnectionFactory(connectionFactory);
			factory.setConcurrency(minThread.toString() + "-" + maxThread.toString());
			factory.setMessageConverter(jsonPersonMessageConverter);
			factory.setSessionTransacted(false);
		}

		return factory;
	}

	@Bean(name=MessagingConstants.JMS_CONTAINER_SECOND_FACTORY)
	public DefaultJmsListenerContainerFactory jmsSecondContainerFactory(Environment env) {

		Integer minThread = env.getProperty(PROPERTY_NAME_JMS_CONTAINER_MIN_THREAD, Integer.class);
		Integer maxThread = env.getProperty(PROPERTY_NAME_JMS_CONTAINER_MAX_THREAD, Integer.class);

		DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		{
			factory.setConnectionFactory(connectionFactory);
			factory.setConcurrency(minThread.toString() + "-" + maxThread.toString());
			factory.setMessageConverter(xmlPersonMessageConverter);
			factory.setSessionTransacted(false);
		}

		return factory;
	}
}
