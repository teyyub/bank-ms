package com.customer_management_service.config;

import com.customer_management_service.entites.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustomerProducer {
    private static final Logger log = LoggerFactory.getLogger(
            CustomerProducer. class);
//    @Autowired
    private RabbitTemplate template;
    public CustomerProducer(RabbitTemplate template){
        this.template = template;
    }
    public void sendTo(String queue, Customer customer){
        this.template.convertAndSend(queue,customer);
        log.info("Producer> Message Sent");
    }
}
