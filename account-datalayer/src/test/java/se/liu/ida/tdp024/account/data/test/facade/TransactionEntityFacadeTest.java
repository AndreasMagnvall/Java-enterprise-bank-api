package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static se.liu.ida.tdp024.account.data.impl.db.util.EMF.getEntityManager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade.AccountTransferResult;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;

public class TransactionEntityFacadeTest {
	
    private TransactionEntityFacade transactionEntityFacade;
    private AccountEntityFacadeDB   accountEntityFacade;

    private final static int NON_EXISTING_ACCOUNT_KEY = -1;
    
    @Before
    public void initialize() {
    	transactionEntityFacade = new TransactionEntityFacadeDB();
    	accountEntityFacade     = new AccountEntityFacadeDB();
    	
    }
    
    private AccountTransferResult debitAccountHelper(int accountId) {
    	
    	AccountTransferResult result =  accountEntityFacade.debitAccount(accountId, 100);
    	return result;
    }
    
    private int createAccountHelper() {
    	String personKey = "1";
    	
    	{
	    	String bankKey   = "1";
	    	boolean created  = accountEntityFacade.createAccount("SAVINGS", personKey, bankKey);
	    	assertTrue(created);
    	}
    	
    	
       	List<Account> accountList = accountEntityFacade.getAccounts(personKey);
    	assertTrue(accountList.size() > 0);
    	int accountId = accountList.get(accountList.size()-1).getId();
    	return accountId;
    }
    
    @Test
    public void testTransactionDBData() {

    	TransactionDB transactionDB = new TransactionDB();
    	
    	final String ACCOUNT_TYPE = "SAVINGS";
    	final String STATUS = "OK";
    	
    	final Date DATE = Calendar.getInstance().getTime();
    	
    	final AccountDB ACCOUNT = new AccountDB();
    	final int AMOUNT = 10;
    	
    	transactionDB.setAccount(ACCOUNT);
    	transactionDB.setCreated(DATE);
    	transactionDB.setStatus(STATUS);
    	transactionDB.setType(ACCOUNT_TYPE);
    	transactionDB.setAmount(AMOUNT);
    	
     	
    	assertEquals(ACCOUNT, transactionDB.getAccount());
    	assertEquals(DATE, transactionDB.getCreated());
    	assertEquals(STATUS, transactionDB.getStatus());
    	assertEquals(ACCOUNT_TYPE, transactionDB.getType());
    	assertEquals(AMOUNT, transactionDB.getAmount());
    	
    	
    }
   	
    private AccountDB getAccountHelper(int accountId) {
    	EntityManager entitymanager = getEntityManager();
        AccountDB accountdb = entitymanager.find(AccountDB.class, accountId);
        Assert.assertNotNull(accountdb);
        return accountdb;
    }
    
    private void createOKTransactionHelper(int accountId) {
       	AccountTransferResult result = debitAccountHelper(accountId);
    	Date date = Calendar.getInstance().getTime();
    	boolean didCreate = transactionEntityFacade.createTransaction("DEBIT", 200, date, result);
    	Assert.assertTrue(didCreate);
    }
    
    private void createFailedTransaction(int accountId) {
    	//AccountTransferResult result = creditAccountHelper(accountId);
        
    	AccountDB account = getAccountHelper(accountId);
    	Assert.assertEquals(accountId, account.getId());
    	int amount = account.getHoldings() + 1000;
    	AccountTransferResult result = accountEntityFacade.creditAccount(accountId, amount);
    	Assert.assertFalse(result.getStatus().getSuccess());
    	Date date = Calendar.getInstance().getTime();
    	boolean didCreate = transactionEntityFacade.createTransaction("CREDIT", amount, date, result);
    	Assert.assertTrue(didCreate);
    }
    
    private boolean createInvalidTransactionTypeHelper(int accountId) {
    	AccountTransferResult result = debitAccountHelper(accountId);
    	Date date = Calendar.getInstance().getTime();
    	boolean didCreate = transactionEntityFacade.createTransaction("POTATO", 200, date, result);
    	return didCreate;
    }
    
    @Test
    public void testCreate() {
    	int accountId = createAccountHelper();
    	createOKTransactionHelper(accountId);
    }
    
    @Test
    public void testCreateWithWrongTransactionType() {
    	int accountId = createAccountHelper();
    	boolean didCreate = createInvalidTransactionTypeHelper(accountId);
    	Assert.assertFalse(didCreate);
    }
    
    @Test 
    public void getTransactionForUnexistingAccount() {
    	List<Transaction> transactions = transactionEntityFacade.getTransactions(NON_EXISTING_ACCOUNT_KEY);
    	assertEquals(0, transactions.size());
    }
    
    
    @Test
    public void testGetAllTransactions() {
    	int accountId = createAccountHelper();
    	createOKTransactionHelper(accountId);
    	createOKTransactionHelper(accountId);
    	createOKTransactionHelper(accountId);
    	createOKTransactionHelper(accountId);
    	createOKTransactionHelper(accountId);
    	createOKTransactionHelper(accountId);
    	createFailedTransaction(accountId);

    	List<Transaction> transactions = transactionEntityFacade.getTransactions(accountId);
    	
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t, Transaction t1) {
                if (t.getId() > t1.getId()) {
                    return -1;
                } else if (t.getId() < t1.getId()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });	
        
        // TODO: Testa med FAILED
        Assert.assertEquals("OK", transactions.get(5).getStatus()); 
        Assert.assertEquals("OK", transactions.get(4).getStatus());
        Assert.assertEquals("OK", transactions.get(3).getStatus());
        Assert.assertEquals("OK", transactions.get(2).getStatus());
        Assert.assertEquals("OK", transactions.get(1).getStatus());
        Assert.assertEquals("FAILED", transactions.get(0).getStatus());
        
        /* TODO: Paramateisera debit och jämför siffrorna du skickade in
        Assert.assertEquals(200, transactions.get(5).getAmount());
        Assert.assertEquals(50, transactions.get(4).getAmount());
        Assert.assertEquals(25, transactions.get(3).getAmount());
        Assert.assertEquals(100, transactions.get(2).getAmount());
        Assert.assertEquals(75, transactions.get(1).getAmount());
        Assert.assertEquals(10, transactions.get(0).getAmount());
         */
        // Alla är DEBIT, TODO: tester för mix
        Assert.assertEquals("DEBIT", transactions.get(5).getType());
        Assert.assertEquals("DEBIT", transactions.get(4).getType());
        Assert.assertEquals("DEBIT", transactions.get(3).getType());
        Assert.assertEquals("DEBIT", transactions.get(2).getType());
        Assert.assertEquals("DEBIT", transactions.get(1).getType());
        Assert.assertEquals("CREDIT", transactions.get(0).getType());
    	
    }
    
}