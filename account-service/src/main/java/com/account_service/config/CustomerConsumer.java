package com.account_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.account_service.dtos.AccountDto;
import com.account_service.entity.Customer;
import com.account_service.services.AccountService;

@Component
public class CustomerConsumer {
    private Logger log = LoggerFactory.getLogger(CustomerConsumer.class);
    private final AccountService accountService;

    public CustomerConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @RabbitListener(queues = "${customer.amqp.queue}")
    public void receive(Customer customer){
        log.info("Consumer> {}" + customer);
        AccountDto dto = new AccountDto();
        dto.setCustomerId(customer.getCustomerId());
        accountService.create(dto);
    }



}
