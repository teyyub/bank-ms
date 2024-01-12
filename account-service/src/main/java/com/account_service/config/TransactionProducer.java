package com.account_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.account_service.entity.Transaction;

@Component
public class TransactionProducer {
    private static final Logger log = LoggerFactory.getLogger(
            TransactionProducer. class);
//    @Autowired
    private RabbitTemplate template;
    public TransactionProducer(RabbitTemplate template){
        this.template = template;
    }
    public void sendTo(String queue, Transaction transaction){
        this.template.convertAndSend(queue,transaction);
        log.info("Producer> Message Sent");
    }
}
