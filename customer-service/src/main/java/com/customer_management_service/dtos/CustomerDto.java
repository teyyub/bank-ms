package com.customer_management_service.dtos;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomerDto {

    private Long customerId;
    @NotNull
    private String name;
    @NotNull
    private String surname;
    @JsonFormat(pattern = "dd.MM.yyyy",shape = JsonFormat.Shape.STRING)
    private LocalDate birthDate;
    @NotNull
    private String gsmNumber;

}
