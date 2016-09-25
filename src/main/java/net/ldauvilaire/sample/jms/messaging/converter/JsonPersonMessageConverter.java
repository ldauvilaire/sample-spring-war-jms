package net.ldauvilaire.sample.jms.messaging.converter;

import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.ldauvilaire.sample.jms.domain.dto.PersonDTO;

@Component
public class JsonPersonMessageConverter implements MessageConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(JsonPersonMessageConverter.class);

	protected static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
		TextMessage message = null;
		if ((object != null) && (object instanceof PersonDTO)) {
			PersonDTO dto = (PersonDTO) object;
			String payload = null;
			try {
				payload = JSON_MAPPER.writeValueAsString(dto);
				message = session.createTextMessage();
				message.setText(payload);
			} catch (JsonProcessingException ex) {
				LOGGER.error("A JsonProcessingException has occurred", ex);
			}
		}
		return message;
	}

	public Object fromMessage(Message message) throws JMSException, MessageConversionException {
		PersonDTO dto = null;
		if ((message != null) && (message instanceof TextMessage)) {
			TextMessage textMessage = (TextMessage) message;
			String payload = textMessage.getText();

			LOGGER.info("Payload to decode = [{}].", payload);

			try {
				dto = JSON_MAPPER.readValue(payload, PersonDTO.class);
			} catch (JsonParseException ex) {
				LOGGER.error("A JsonProcessingException has occurred", ex);
			} catch (JsonMappingException ex) {
				LOGGER.error("A JsonMappingException has occurred", ex);
			} catch (IOException ex) {
				LOGGER.error("A IOException has occurred", ex);
			}
		}
		return dto;
	}
}
