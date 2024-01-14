package com.transaction_service.dtos;

import java.math.BigDecimal;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountResponseDto {
    private Long accountId;
    private BigDecimal balance;
}
