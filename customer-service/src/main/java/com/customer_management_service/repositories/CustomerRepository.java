package com.customer_management_service.repositories;

import com.customer_management_service.entites.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

   Optional<Customer> findByGsmNumber(String gsmNumber);

}
