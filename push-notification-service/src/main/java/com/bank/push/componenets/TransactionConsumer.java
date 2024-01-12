package com.bank.push.componenets;

import com.bank.push.entities.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
    private Logger log = LoggerFactory.getLogger(TransactionConsumer.class);

    @RabbitListener(queues = "${transaction.amqp.queue}")
    public void receive(Transaction transaction){
        log.info("Transaction> {}" + transaction);

    }



}