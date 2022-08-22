package com.okan.bankingmanagement.config.kafka;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.kafka.support.KafkaHeaders;

@Component
public class Consumer {
	
	private static final Logger logger = LogManager.getLogger(Consumer.class);
	
    
	@KafkaListener(topics = {"logs"}, groupId = "logs_group")
    public void listenTransfer(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
    ){
		
		logger.info(message);
		
    }
}