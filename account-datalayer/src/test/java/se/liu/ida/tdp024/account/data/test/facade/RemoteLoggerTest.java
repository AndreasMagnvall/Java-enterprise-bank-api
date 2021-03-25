package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.log4j.Logger;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.util.RemoteLogger;
import se.liu.ida.tdp024.account.data.api.util.RemoteLogger.Topic;

public class RemoteLoggerTest {
	
	@Test 
	public void testSend() {
		RemoteLogger logger = RemoteLogger.getInstance();
		
		assertNotNull(logger);
		
		Future<RecordMetadata> future = logger.log(Topic.TRANSACTION, 
    			String.format("TRANSACTION %s %s %s %s %s",
				"1", Calendar.getInstance().getTime(), "DEBIT", "10", "OK"));


		RecordMetadata record = null;
		try {
			record = (RecordMetadata)future.get();
		} catch (InterruptedException e) {
			Logger.getRootLogger().error(e.getMessage());
		} catch (ExecutionException e) {
			Logger.getRootLogger().error(e.getMessage());
		}
	
		assertNotNull(record);
		assertEquals(Topic.TRANSACTION.toString(), record.topic());		
	}
}
