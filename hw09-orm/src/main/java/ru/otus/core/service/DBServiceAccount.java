package ru.otus.core.service;

import ru.otus.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

  long saveAccount(Account account);

  long updateAccount(Account account);

  Optional<Account> getAccount(long id);

}
