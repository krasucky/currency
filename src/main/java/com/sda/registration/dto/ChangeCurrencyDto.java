package com.sda.registration.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class ChangeCurrencyDto {

    @NotNull
    private String currencyFrom;

    @NotNull
    private String currencyTo;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.00")
    private BigDecimal amount;

}
