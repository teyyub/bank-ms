package com.transaction_service.dtos;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountDto {

//    @NotNull
    private Long customerId;
    private String gsmNumber;
    private BigDecimal balance;
}
