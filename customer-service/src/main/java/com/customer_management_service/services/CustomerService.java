package com.customer_management_service.services;

import com.customer_management_service.config.CustomerProducer;
import com.customer_management_service.dtos.CustomerDto;
import com.customer_management_service.entites.Customer;
import com.customer_management_service.exceptions.AlreadyExistException;
import com.customer_management_service.exceptions.ResourceNotFoundException;
import com.customer_management_service.repositories.CustomerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class CustomerService   {
    @Value("${customer.amqp.queue}")
    private String customerQueue;
//    @Value("${account.service.base-url}")
//    private String accountServiceBaseUrl;
    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
//    private RestTemplate restTemplate;

    @Autowired
    private CustomerProducer rabbitProducer;

    private Logger logger = LoggerFactory.getLogger(CustomerService.class);


    public CustomerDto create(CustomerDto customerDto) {
         Optional<Customer>  findCustomer =
                customerRepository.findByGsmNumber(customerDto.getGsmNumber());
         if(findCustomer.isPresent()) {
             throw new AlreadyExistException("Customer with GSM number " + customerDto.getGsmNumber() + " already exists");
    }
         Customer customer = toCustomer(new Customer(),customerDto);
         customerRepository.save(customer);
         customerDto.setCustomerId(customer.getCustomerId());
         logger.info("Producer> Message Sent");
         //bunlari ile convert bas vermedi ona gore helelik bele edirem
         customerDto.setBirthDate(null);
         rabbitProducer.sendTo(customerQueue,customerDto);
        return customerDto;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public CustomerDto get(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with given id not found"));
        return toCustomerDto(customer);
    }

    public Customer update(String id, CustomerDto dto) {
        Customer customerUpdate = this.customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with given id not found"));
        toCustomer(customerUpdate,dto);
        return customerRepository.save(customerUpdate);
    }

    public void delete(String id) {
        Customer customer = this.customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with given id not found"));
        this.customerRepository.delete(customer);
    }

    private static Customer toCustomer(Customer customer,CustomerDto dto){
           customer = Customer.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .gsmNumber(dto.getGsmNumber())
                .birthDate(dto.getBirthDate()).build();
        return customer;
    }

    private static CustomerDto toCustomerDto(Customer customer){
        CustomerDto dto = CustomerDto.builder()
                .customerId(customer.getCustomerId())
                .name(customer.getName())
                .surname(customer.getSurname())
                .gsmNumber(customer.getGsmNumber())
                .birthDate(customer.getBirthDate()).build();
        return dto;
    }
}
