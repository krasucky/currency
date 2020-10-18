package com.sda.registration.controller;

import com.sda.registration.domain.Account;
import com.sda.registration.dto.ChangeCurrencyDto;
import com.sda.registration.dto.CreateAccountDto;
import com.sda.registration.dto.UpdateAccountDto;
import com.sda.registration.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RestController
@RequestMapping(value = "/account")
public class AccountController {

    private final RestTemplate restTemplate;

    private final AccountService service;

    @Autowired
    public AccountController(RestTemplate restTemplate, AccountService service) {
        this.restTemplate = restTemplate;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAllAccount() {
        List<Account> accounts = service.findAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{pesel}")
    public ResponseEntity<Account> findAccountByPesel(@PathVariable String pesel) {
        Account accountByPesel = service.findAccountByPesel(pesel);

        return ResponseEntity.ok(accountByPesel);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody @Valid CreateAccountDto dto) {
        Account acc = service.create(dto);
        return new ResponseEntity<>(acc, HttpStatus.CREATED);
    }

    @PutMapping("/{pesel}")
    public ResponseEntity<Account> updateAccount(@RequestBody UpdateAccountDto dto, @PathVariable String pesel) {

        Account account = service.updateAccount(dto, pesel);
        return ResponseEntity.ok(account);
    }


    @DeleteMapping("/{pesel}")
    public ResponseEntity<Void> deleteAccountByPesel(@PathVariable(value = "pesel") String pesel) {
        service.deleteAccountByPesel(pesel);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/test")
    public RateResponse getResponse() {

        RateResponse response = restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/a/usd?format=json",
                RateResponse.class);

        System.out.println(response);
        return response;
    }

    @GetMapping("/exchange")
    public BigDecimal changeCurrency(@Valid ChangeCurrencyDto dto) {

        if (dto.getCurrencyFrom().equalsIgnoreCase("PLN")) {
            String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + dto.getCurrencyTo() + "?format=json";
            RateResponse response = restTemplate.getForObject(url, RateResponse.class);
            BigDecimal rate = BigDecimal.valueOf(response.getRates().get(0).getMid())
                    .setScale(2, RoundingMode.HALF_UP);

            return dto.getAmount().divide(rate, RoundingMode.HALF_DOWN);
        } else {
            String url = "http://api.nbp.pl/api/exchangerates/rates/a/" + dto.getCurrencyFrom() + "?format=json";
            RateResponse response = restTemplate.getForObject(url, RateResponse.class);
            BigDecimal rate = BigDecimal.valueOf(response.getRates().get(0).getMid())
                    .setScale(2, RoundingMode.HALF_UP);

            return dto.getAmount().multiply(rate);
        }
    }

}
