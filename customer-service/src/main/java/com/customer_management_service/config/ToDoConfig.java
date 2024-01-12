package com.customer_management_service.config;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class ToDoConfig {
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(new Jackson2JsonMessageConverter());
//        return factory;
//    }
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate template = new RabbitTemplate(connectionFactory);
//        template.setMessageConverter(new Jackson2JsonMessageConverter());
//        return template;
//    }
//
//    @Bean
//    public Queue queueCreation(@Value("${customer.amqp.queue}") String queue){
//        return new Queue(queue,true,false,false);
//    }

//    @Bean
//    public Queue queueCreation(){
//        return new Queue("TodoQueue",true,false,false);
//    }

//    @Bean
//    Queue marketingQueue() {
//        return new Queue("marketingQueue", false);
//    }

//    @Bean
//    Queue financeQueue() {
//        return new Queue("financeQueue", false);
//    }

//    @Bean
//    Queue adminQueue() {
//        return new Queue("adminQueue", false);
//    }


//    @Bean
//    DirectExchange exchange() {
//        return new DirectExchange("direct-exchange");
//    }

//    @Bean
//    Binding marketingBinding(Queue marketingQueue, DirectExchange exchange) {
//        return BindingBuilder
//                .bind(marketingQueue)
//                .to(exchange).with("marketing");
//    }

//    @Bean
//    Binding financeBinding(Queue financeQueue, DirectExchange exchange) {
//        return BindingBuilder
//                .bind(financeQueue)
//                .to(exchange).with("finance");
//    }

//    @Bean
//    Binding adminBinding(Queue adminQueue, DirectExchange exchange) {
//        return BindingBuilder
//                .bind(adminQueue)
//                .to(exchange).with("admin");
//    }
//
//
//    @Bean
//    public Declarables createPostRegistartionSchema(){
//
//        return new Declarables(
//                new FanoutExchange("x.post-registration"),
//                new Queue("q.send-email" ),
//                new Queue("q.send-sms"),
//                new Binding("q.send-email",
//                        Binding.DestinationType.QUEUE, "x.post-registration", "send-email", null),
//                new Binding("q.send-sms",
//                        Binding.DestinationType.QUEUE, "x.post-registration", "send-sms", null));
//
//    }
}
