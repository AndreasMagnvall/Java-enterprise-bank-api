package se.liu.ida.tdp024.account.data.api.util;

import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoteLogger {

	public enum Topic
	{
		TRANSACTION, 
		RESTREQUEST;		
		
		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}
	}

	
	private static RemoteLogger logger = new RemoteLogger();

	static final String BROKERS  = KafkaConstants.BROKERS; 
	static final String PASSWORD = KafkaConstants.PASSWORD;
	static final String USERNAME = KafkaConstants.USERNAME;

	Producer<String, String> producer;
    private final Properties producerProps;
	
	private RemoteLogger() {
		String serializer    = StringSerializer.class.getName();
		
	    producerProps = new Properties();
	    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKERS);
	    producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer);
	    producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer);
		producer = new KafkaProducer<>(producerProps);
	
	}
	
	public static RemoteLogger getInstance() 
	{
		return logger;
	}
	
	public Future<RecordMetadata> log(Topic t, Object obj)
	{
	   String retval = "";
	   ObjectMapper objectMapper = new ObjectMapper();
	   try {
	     retval = objectMapper.writeValueAsString(obj);
	     return producer.send(new ProducerRecord<>(t.toString(), null, retval));
	   } catch (Exception e) {
		 Logger.getRootLogger().error("Object mapper failed to write");
	   }
	   return null;
	}
		
}
