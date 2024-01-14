package com.transaction_service.exceptions;

import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

@Component
public class RabbitRetryHandler implements RabbitListenerErrorHandler {

    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message,
                              ListenerExecutionFailedException exception) throws Exception {
        System.out.println("Redelivered: " + amqpMessage.getMessageProperties().isRedelivered());
        if (amqpMessage.getMessageProperties().isRedelivered()) {
            throw new AmqpRejectAndDontRequeueException(exception);
        }
        else {
            throw exception;
        }
    }


}