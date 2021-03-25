package se.liu.ida.tdp024.account.logic.api.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;

public interface AccountLogicFacade {
    boolean createAccount(String accountType, String personKey, String bankName);
    List<Account> findAccounts(String personKey);
    boolean debitAccount(int accountId, int amount);
    boolean creditAccount(int accountId, int amount);
    List<Transaction> findTransactions(int accountId);
}