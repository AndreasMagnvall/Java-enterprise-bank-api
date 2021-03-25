package se.liu.ida.tdp024.account.logic.impl.facade;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Bank;
import se.liu.ida.tdp024.account.data.api.entity.Person;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade.AccountTransferResult;
import se.liu.ida.tdp024.account.data.api.facade.BankEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.PersonEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.RemoteLogger;
import se.liu.ida.tdp024.account.data.api.util.RemoteLogger.Topic;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;


public class AccountLogicFacadeImpl implements AccountLogicFacade {
    
    private AccountEntityFacade     accountEntityFacade;
    private BankEntityFacade        bankEntityFacade;
    private PersonEntityFacade      personEntityFacade;
    private TransactionEntityFacade transactionEntityFacade;
    
    RemoteLogger logger = RemoteLogger.getInstance();

    
    public AccountLogicFacadeImpl(AccountEntityFacade accountEntityFacade, 
    							  BankEntityFacade bankEntityFacade, 
    							  PersonEntityFacade personEntityFacade, 
    							  TransactionEntityFacade transactionEntityFacade) {
        this.accountEntityFacade = accountEntityFacade;
        this.bankEntityFacade    = bankEntityFacade;
        this.personEntityFacade  = personEntityFacade;
        this.transactionEntityFacade = transactionEntityFacade;
    }

    public boolean createAccount(String accountType, String personKey, String bankName) {
        Person person            = personEntityFacade.getPersonByKey(personKey);
        
        Bank bank                = bankEntityFacade.getBankByName(bankName);
        boolean validAccountType = accountEntityFacade.isValidAccountType(accountType);
        

        boolean ok = (person != null && bank != null && validAccountType);
        
        if(!ok) { return false; }
        
        
        return accountEntityFacade.createAccount(accountType, personKey, bankName);
    }
    
    public List<Account> findAccounts(String personKey) {
    	
    	//TODO check if personKey exist
    	
    	return accountEntityFacade.getAccounts(personKey);
    }

    @Override
    public boolean debitAccount(int accountId, int amount) {
    	// SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	Date date = Calendar.getInstance().getTime();
    	
    	//AccountDB account = accountEntityFacade.findAccount(accountId);
  
    	//boolean ok = accountEntityFacade.debitAccount(accountId, amount);
    	AccountTransferResult result = accountEntityFacade.debitAccount(accountId, amount);
    	String transactionType = "DEBIT";
    	
    	transactionEntityFacade.createTransaction(transactionType, amount, date, result);
    	logger.log(Topic.TRANSACTION, 
    			String.format("TRANSACTION %s %s %s %s %s",
    					accountId, date, transactionType, amount, result.getStatus().getMessage()));
    	
        return result.getStatus().getSuccess();
    }

    @Override
    public boolean creditAccount(int accountId, int amount) {
    	//SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    	//String created = sdfDateFormat.format(Calendar.getInstance().getTime());
    	
    	
    	AccountTransferResult result = accountEntityFacade.creditAccount(accountId, amount);
    	Date date = Calendar.getInstance().getTime();
    	String transactionType = "CREDIT";
    	transactionEntityFacade.createTransaction(transactionType, amount, date, result);
    	logger.log(Topic.TRANSACTION, 
    			String.format("TRANSACTION %s %s %s %s %s",
    					accountId, date, transactionType, amount, result.getStatus().getMessage()));
    	
        return result.getStatus().getSuccess();
    }

    @Override
    public List<Transaction> findTransactions(int accountId) {
    	return transactionEntityFacade.getTransactions(accountId);
    }
}
