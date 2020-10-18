package com.sda.registration.controller;

import lombok.Data;

import java.util.List;

@Data
public class RateResponse {

    private String table;

    private String currency;

    private String code;

    private List<Rates> rates;

}
