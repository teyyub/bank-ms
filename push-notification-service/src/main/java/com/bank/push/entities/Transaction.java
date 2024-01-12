package com.bank.push.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Entity
//@Table(name="customer_transactions")
public class Transaction {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;
    @DateTimeFormat
    Date lastActivity;

}
