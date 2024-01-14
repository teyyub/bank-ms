package com.transaction_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
//@Table(name="accounts")
@Data
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", length = 20, nullable = false)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", length = 20, nullable = false)
    private AccountStatus accountStatus;

    LocalDateTime accountOpeningDate;


    LocalDateTime accountCloseDate;


    LocalDateTime lastActivity;
    private BigDecimal balance;

    @Column(nullable = false)
    private Long customerId;

    @Column(name = "gsm_number", unique = true, nullable = false)
    private String gsmNumber;

//    @Transient
//    Customer customer;

}
