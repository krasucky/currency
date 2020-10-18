package com.sda.registration.repository;

import com.sda.registration.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

    Account findAccountByPesel(String pesel);

    boolean existsAccountByPesel(String pesel);
}
