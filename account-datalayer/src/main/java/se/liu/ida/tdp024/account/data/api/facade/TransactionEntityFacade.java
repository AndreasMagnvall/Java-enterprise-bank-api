package se.liu.ida.tdp024.account.data.api.facade;

import java.util.Date;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade.AccountTransferResult;

public interface TransactionEntityFacade {
	List<Transaction> getTransactions(int accountId);
	boolean createTransaction(String transactionType, int amount, Date created, AccountTransferResult res);
}
