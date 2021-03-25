package se.liu.ida.tdp024.account.logic.test.facade;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.util.StorageFacade;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.network.facade.BankEntityFacadeAPI;
import se.liu.ida.tdp024.account.data.impl.network.facade.PersonEntityFacadeAPI;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;

public class AccountLogicFacadeTest {


    //--- Unit under test ---//
    public AccountLogicFacade accountLogicFacade;
    public StorageFacade storageFacade;
    private static final String PERSON_KEY = "1";
    private static final String INVALID_PERSON_KEY = "-1";
    @After
    public void tearDown() {
      if (storageFacade != null)
        storageFacade.emptyStorage();
    }

    @Before
    public void initialize() {
    	accountLogicFacade = new AccountLogicFacadeImpl(
				new AccountEntityFacadeDB(),
				new BankEntityFacadeAPI(),
				new PersonEntityFacadeAPI(),
				new TransactionEntityFacadeDB());
    }

    private void createAccountHelper() {
    	boolean created = accountLogicFacade.createAccount("SAVINGS", PERSON_KEY, "HANDELSBANKEN");
    	assertTrue(created);
    }
    
    @Test
    public void testCreate() {
    	boolean result1 = accountLogicFacade.createAccount("SAVINGS", PERSON_KEY, "HANDELSBANKEN");
    	boolean result2 = accountLogicFacade.createAccount("CHECK", PERSON_KEY, "HANDELSBANKEN");
    	
    	assertTrue(result1);	
    	assertTrue(result2);
    }
    
    @Test
    public void testFailCreate() {
    	boolean result1 = accountLogicFacade.createAccount("SAVINGS", INVALID_PERSON_KEY, "HANDELSBANKEN");
    	boolean result2 = accountLogicFacade.createAccount("SAVINGS", INVALID_PERSON_KEY, "HANDELSBANEN");
    	boolean result3 = accountLogicFacade.createAccount("BANANKONTO", INVALID_PERSON_KEY, "HANDELSBANEN");
    	assertFalse(result1);	
    	assertFalse(result2);	
    	assertFalse(result3);	
    }
    
    @Test
    public void testTransfer() {
    	createAccountHelper();
    	List<Account> accounts =  accountLogicFacade.findAccounts(PERSON_KEY);
    	assertTrue(accounts.size() > 0);
    	
    	int lastIndex = accounts.size() - 1;
    	
    	int accountId = accounts.get(lastIndex).getId();
    	boolean res1 = accountLogicFacade.creditAccount(accountId, 100); 	
    	boolean res2 = accountLogicFacade.debitAccount(accountId, 100 );
    	boolean res3 = accountLogicFacade.debitAccount(accountId, 100);
    
    	assertFalse(res1);
    	assertTrue(res2);
    	assertTrue(res3);
    }
    
    @Test
    public void testTransactions() {
    	createAccountHelper();
    	List<Account> accounts =  accountLogicFacade.findAccounts(PERSON_KEY);
    	assertTrue(accounts.size() > 0);
    	
    	int lastIndex = accounts.size() - 1;
	
    	int accountId = accounts.get(lastIndex).getId();
    	
    	accountLogicFacade.findTransactions(accountId);
    }
}
