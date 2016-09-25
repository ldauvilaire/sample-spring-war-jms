package net.ldauvilaire.sample.jms.messaging;

import java.util.Arrays;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import net.ldauvilaire.sample.jms.configuration.ApplicationTestConfiguration;
import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationTestConfiguration.class)
public class PersonMessageReceiverTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonMessageReceiverTest.class);

	protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	protected static final XmlMapper XML_MAPPER = new XmlMapper();

	protected static BrokerService broker;
	protected static ActiveMQConnectionFactory connectionFactory;

	@BeforeClass
	public static void startup() throws Exception {

		LOGGER.info("*** Starting Broker ...");

		broker = new BrokerService();
		broker.setPersistent(false);
		broker.addConnector("tcp://localhost:61617");
		broker.start();

		connectionFactory = new ActiveMQConnectionFactory();
		{
			connectionFactory.setBrokerURL("tcp://localhost:61617");
			connectionFactory.setTrustedPackages(Arrays.asList("net.ldauvilaire.sample"));
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		LOGGER.info("*** Stopping Broker ...");
		broker.stop();
	}

	@Test
	public void testReceiverJsonMessage() throws Exception {

		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory);

		PersonDTO dto = new PersonDTO("testFirst", "testLast");
		String message = JSON_MAPPER.writeValueAsString(dto);
		String destination = "jms/queue/First";

		LOGGER.info("*** Sending JMS Message [{}] to Queue [{}] ...", message, destination);
		jmsTemplate.convertAndSend(destination, message);

		//-- Given son time to process --
		LOGGER.info("*** Waiting ...");
		Thread.sleep(10000);

		LOGGER.info("*** Test is over..");
	}

	@Test
	public void testReceiverXmlMessage() throws Exception {

		JmsTemplate jmsTemplate = new JmsTemplate();
		jmsTemplate.setConnectionFactory(connectionFactory);

		PersonDTO dto = new PersonDTO("testFirst", "testLast");
		String message = XML_MAPPER.writeValueAsString(dto);
		String destination = "jms/queue/Second";

		LOGGER.info("*** Sending JMS Message [{}] to Queue [{}] ...", message, destination);
		jmsTemplate.convertAndSend(destination, message);

		//-- Given son time to process --
		LOGGER.info("*** Waiting ...");
		Thread.sleep(10000);

		LOGGER.info("*** Test is over..");
	}
}
