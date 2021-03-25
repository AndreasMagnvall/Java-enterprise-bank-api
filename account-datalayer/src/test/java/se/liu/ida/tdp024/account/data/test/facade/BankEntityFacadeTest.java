package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import se.liu.ida.tdp024.account.data.api.entity.Bank;
import se.liu.ida.tdp024.account.data.api.facade.BankEntityFacade;
import se.liu.ida.tdp024.account.data.impl.network.entity.BankAPI;
import se.liu.ida.tdp024.account.data.impl.network.facade.BankEntityFacadeAPI;

public class BankEntityFacadeTest {

	    private BankEntityFacade bankEntityFacade;

	    @Before
	    public void initialize() {
	    	bankEntityFacade = new BankEntityFacadeAPI();
	    }
	    
	    @Test
	    public void testBank() {
	    	Bank bank = bankEntityFacade.getBankByName("HANDELSBANKEN");
	    	assertEquals("HANDELSBANKEN", bank.getName());
	    	assertEquals("6", bank.getKey());
	    }
	    
	    @Test
	    public void testBankData() {
	    	BankAPI bankAPI = new BankAPI();
	    	
	    	assertNull(bankAPI.getName());
	    	assertNull(bankAPI.getKey());  	
	    }
	    
	    
	    @Test
	    public void testWrongBankName() {
	    	Bank bank = bankEntityFacade.getBankByName("HANDELSBANAN");
	    	assertNull(bank);
	    }

}
