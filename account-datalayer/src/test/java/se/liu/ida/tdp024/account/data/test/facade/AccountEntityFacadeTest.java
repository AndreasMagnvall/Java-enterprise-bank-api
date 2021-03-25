package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade.AccountTransferResult;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;

public class AccountEntityFacadeTest {
    
    //---- Unit under test ----//
    private AccountEntityFacade accountEntityFacade;

    private final static int NON_EXISTING_ACCOUNT_ID = -1;
    
    private final static String PERSON_KEY = "1";
       
    
    @Before
    public void initialize() {
    	accountEntityFacade = new AccountEntityFacadeDB();
    }
    
    private int createAccountHelper() {
    	boolean created = accountEntityFacade.createAccount("SAVINGS", PERSON_KEY, "1");
    	assertTrue(created);
    	List<Account> accountList = accountEntityFacade.getAccounts(PERSON_KEY);
    	assertTrue(accountList.size() > 0);
    	int accountId = accountList.get(accountList.size()-1).getId();
		return accountId;
    }
    
    @Test
    public void testCreate() {
    	boolean created = accountEntityFacade.createAccount("SAVINGS", PERSON_KEY, "1");
    	assertTrue(created);
    	
    	
    	
    	
    	List<Account> accountList = accountEntityFacade.getAccounts(PERSON_KEY);
    	assertTrue(accountList.size() > 0);      	
    }  
    
    @Test
    public void testTransfer() {

    	int accountId = createAccountHelper();
    	AccountTransferResult result1 =  accountEntityFacade.creditAccount(accountId, 100); 
    	AccountTransferResult result2 =  accountEntityFacade.debitAccount(accountId, 100);	
    	AccountTransferResult result3 =  accountEntityFacade.creditAccount(accountId, 50);
    	
    	assertFalse(result1.getStatus().getSuccess());
    	assertTrue(result2.getStatus().getSuccess());
    	assertTrue(result3.getStatus().getSuccess());
    }
    
    @Test
    public void testAccountDBData() {

    	AccountDB accountDB = new AccountDB();
    	
    	final String ACCOUNT_TYPE = "SAVINGS";
    	final String BANK_KEY = "1";
    	final int HOLDINGS = 10;  	
    	
    	accountDB.setAccountType(ACCOUNT_TYPE);
    	accountDB.setBankKey(BANK_KEY);
    	accountDB.setHoldings(10);
    	accountDB.setPersonKey(PERSON_KEY);
     	
    	assertEquals(ACCOUNT_TYPE, accountDB.getAccountType());
    	assertEquals(BANK_KEY,     accountDB.getBankKey());
    	assertEquals(HOLDINGS,     accountDB.getHoldings());
    	assertEquals(PERSON_KEY,   accountDB.getPersonKey());
   	
    }
    
    @Test
    public void testNonExistingAccountCredit() {
    	AccountTransferResult result =  accountEntityFacade.creditAccount(NON_EXISTING_ACCOUNT_ID, 50);
    	assertFalse(result.getStatus().getSuccess());
    }
    
    @Test
    public void testNonExistingAccountDebit() {
       	AccountTransferResult result =  accountEntityFacade.debitAccount(NON_EXISTING_ACCOUNT_ID, 50);
    	assertFalse(result.getStatus().getSuccess());
    }
    
    @Test
    public void testUtil() {    	
     	boolean fail1 = accountEntityFacade.isValidAccountType("hej");
    	boolean success1 = accountEntityFacade.isValidAccountType("SAVINGS");
    	boolean success2 = accountEntityFacade.isValidAccountType("CHECK");  	
    	
    	assertFalse(fail1);
    	assertTrue(success1);
    	assertTrue(success2);
    }
    
  
}