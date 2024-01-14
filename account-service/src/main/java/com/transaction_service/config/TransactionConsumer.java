package com.transaction_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.transaction_service.dtos.AccountResponseDto;
import com.transaction_service.services.AccountService;

@Component
public class TransactionConsumer {
    private Logger log = LoggerFactory.getLogger(TransactionConsumer.class);
    private final AccountService accountService;

    public TransactionConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

    @RabbitListener(queues = "${account.amqp.queue.balance}", errorHandler = "rabbitRetryHandler")
    public void receive(AccountResponseDto dto){
        log.info("Consumer> {}" + dto);
        accountService.updateAccountBalance(dto.getAccountId(),dto.getBalance());
    }



}
