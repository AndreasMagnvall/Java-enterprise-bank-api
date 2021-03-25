package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Bank;

public interface BankEntityFacade {
    Bank getBankByName(String name);
}
