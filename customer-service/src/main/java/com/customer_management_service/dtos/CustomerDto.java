package com.customer_management_service.dtos;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerDto {

    private String customerId;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    private LocalDate birthDate;
    @NotNull
    private String gsmNumber;

}
