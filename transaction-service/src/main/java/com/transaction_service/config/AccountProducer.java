package com.transaction_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.transaction_service.dtos.AccountResponseDto;

@Component
public class AccountProducer {
    private static final Logger log = LoggerFactory.getLogger(AccountProducer. class);
//    @Autowired
    private RabbitTemplate template;
    public AccountProducer(RabbitTemplate template){

        this.template = template;
    }
    public void sendTo(String queue, AccountResponseDto dto){
        this.template.convertAndSend(queue,dto);
        log.info("Producer> Message Sent");
    }
}
