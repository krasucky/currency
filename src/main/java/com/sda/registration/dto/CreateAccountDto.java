package com.sda.registration.dto;

import lombok.Data;
import org.hibernate.validator.constraints.pl.PESEL;

import javax.validation.constraints.NotNull;

@Data
public class CreateAccountDto {

    @PESEL
    @NotNull
    private String pesel;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;


}
