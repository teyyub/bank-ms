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
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerProducer rabbitProducer;

    private Logger logger = LoggerFactory.getLogger(CustomerService.class);


    public Customer create(CustomerDto customerDto) {
         Optional<Customer>  findCustomer =
                customerRepository.findByGsmNumber(customerDto.getGsmNumber());
         if(findCustomer.isPresent()) {
             throw new AlreadyExistException("aaa");
         }
         Customer customer = new Customer();
         customer.setName(customerDto.getName());
         customer.setSurname(customerDto.getSurname());
         customer.setGsmNumber(customerDto.getGsmNumber());
         customer.setBirthDate(customerDto.getBirthDate());
         customer.setBalance(BigDecimal.valueOf(100));

         customerRepository.save(customer);
         logger.info("Producer> Message Sent");
         rabbitProducer.sendTo(customerQueue,customer);
        return customer;
    }

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer get(String id) {

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with given id not found"));

        return customer;
    }

    public Customer update(String id, Customer customer) {
        Customer customer1 = this.customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with given id not found"));
        customer1.setName(customer.getName());
//        customer1.setPhone(customer.getPhone());
//        customer1.setEmail(customer.getEmail());
//        customer1.setAddress(customer.getAddress());


        return customerRepository.save(customer1);
    }

    public void delete(String id) {

        Customer customer = this.customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer with given id not found"));

        // Deleting Accounts from ACCOUNT-SERVICE
        // http://localhost:8083/account/user/d79beee9-de29-4633-91f7-6be276e6e3c4

        restTemplate.delete("http://ACCOUNT-SERVICE/account/user/" + customer.getCustomerId());

        this.customerRepository.delete(customer);
    }
}
