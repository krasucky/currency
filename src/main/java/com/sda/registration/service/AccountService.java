package com.sda.registration.service;

import com.sda.registration.domain.Account;
import com.sda.registration.dto.CreateAccountDto;
import com.sda.registration.dto.UpdateAccountDto;
import com.sda.registration.exception.ResourceAlreadyExistsException;
import com.sda.registration.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public Account create(CreateAccountDto dto) {
        assertAccountWithGivenPeselDoesNotExist(dto.getPesel());
        Account account = Account.builder()
                .pesel(dto.getPesel())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();

        repository.save(account);
        return account;
    }

    public List<Account> findAllAccounts() {
        return repository.findAll();
    }

    public Account findAccountByPesel(String pesel) {
        return repository.findAccountByPesel(pesel);
    }

    public Account updateAccount(UpdateAccountDto dto, String pesel) {
        Account account = repository.getOne(pesel);
        account.setPesel(dto.getPesel());
        account.setFirstName(dto.getFirstName());
        account.setLastName(dto.getLastName());
        return repository.save(account);
    }

    public void deleteAccountByPesel(String pesel) {
        repository.deleteById(pesel);
    }

    private void assertAccountWithGivenPeselDoesNotExist(String pesel) {
        boolean exist = repository.existsAccountByPesel(pesel);
        if (exist) {
            throw new ResourceAlreadyExistsException("Account with given pesel already exists!");
        }
    }
}
