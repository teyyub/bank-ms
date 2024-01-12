package com.account_service.dtos;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDto {

//    @NotNull
    private Long customerId;
    private BigDecimal balance;
}
