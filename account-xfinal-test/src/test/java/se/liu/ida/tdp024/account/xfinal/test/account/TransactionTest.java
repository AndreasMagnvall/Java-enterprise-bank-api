package se.liu.ida.tdp024.account.xfinal.test.account;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import se.liu.ida.tdp024.account.util.http.HTTPHelper;
import se.liu.ida.tdp024.account.util.http.HTTPHelperImpl;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializer;
import se.liu.ida.tdp024.account.util.json.AccountJsonSerializerImpl;
import se.liu.ida.tdp024.account.xfinal.test.util.AccountDTO;
import se.liu.ida.tdp024.account.xfinal.test.util.FinalConstants;
import se.liu.ida.tdp024.account.xfinal.test.util.TransactionDTO;

public class TransactionTest {

    private static final HTTPHelper httpHelper = new HTTPHelperImpl();
    private static final AccountJsonSerializer jsonSerializer = new AccountJsonSerializerImpl();

    @Test
    public void testFind() {


        {
            String person = "3";
            String bank = "SWEDBANK";
            String accountType = "SAVINGS";
            String response = httpHelper.get(FinalConstants.ENDPOINT + "account/create/", "person", person, "bank", bank, "accounttype", accountType);
            Assert.assertEquals("OK", response);
        }

        String accountJson = httpHelper.get(FinalConstants.ENDPOINT + "account/find/person", "person", "3");
        AccountDTO[] accountDTos = jsonSerializer.fromJson(accountJson, AccountDTO[].class);
        AccountDTO accountDTO = accountDTos[0];

        httpHelper.get(FinalConstants.ENDPOINT + "account/credit", "id", accountDTO.getId() + "", "amount", 200 + "");
        httpHelper.get(FinalConstants.ENDPOINT + "account/debit", "id", accountDTO.getId() + "", "amount", 50 + "");
        httpHelper.get(FinalConstants.ENDPOINT + "account/credit", "id", accountDTO.getId() + "", "amount", 25 + "");
        httpHelper.get(FinalConstants.ENDPOINT + "account/debit", "id", accountDTO.getId() + "", "amount", 100 + "");
        httpHelper.get(FinalConstants.ENDPOINT + "account/debit", "id", accountDTO.getId() + "", "amount", 75 + "");
        httpHelper.get(FinalConstants.ENDPOINT + "account/debit", "id", accountDTO.getId() + "", "amount", 10 + "");


        String transactionJson = httpHelper.get(FinalConstants.ENDPOINT + "account/transactions", "id", accountDTO.getId() + "");

        TransactionDTO[] transactionsArray = jsonSerializer.fromJson(transactionJson, TransactionDTO[].class);
        List<TransactionDTO> transactions = new ArrayList<TransactionDTO>();
        for (TransactionDTO t : transactionsArray) {
            transactions.add(t);
        }

        Collections.sort(transactions, new Comparator<TransactionDTO>() {
            @Override
            public int compare(TransactionDTO t, TransactionDTO t1) {
                if (t.getId() > t1.getId()) {
                    return -1;
                } else if (t.getId() < t1.getId()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });	    
        //FAILED transaction is the first of the 6 ones, not the the last one. Moved to first place to correct the test.
        Assert.assertEquals("OK", transactions.get(0).getStatus()); 
        Assert.assertEquals("OK", transactions.get(1).getStatus());
        Assert.assertEquals("OK", transactions.get(2).getStatus());
        Assert.assertEquals("OK", transactions.get(3).getStatus());
        Assert.assertEquals("OK", transactions.get(4).getStatus());
        Assert.assertEquals("FAILED", transactions.get(5).getStatus());

        Assert.assertEquals(200, transactions.get(5).getAmount());
        Assert.assertEquals(50, transactions.get(4).getAmount());
        Assert.assertEquals(25, transactions.get(3).getAmount());
        Assert.assertEquals(100, transactions.get(2).getAmount());
        Assert.assertEquals(75, transactions.get(1).getAmount());
        Assert.assertEquals(10, transactions.get(0).getAmount());

        Assert.assertEquals("CREDIT", transactions.get(5).getType());
        Assert.assertEquals("DEBIT", transactions.get(4).getType());
        Assert.assertEquals("CREDIT", transactions.get(3).getType());
        Assert.assertEquals("DEBIT", transactions.get(2).getType());
        Assert.assertEquals("DEBIT", transactions.get(1).getType());
        Assert.assertEquals("DEBIT", transactions.get(0).getType());

        Assert.assertEquals(accountDTO.getId(), transactions.get(5).getAccount().getId());
        Assert.assertEquals(accountDTO.getId(), transactions.get(4).getAccount().getId());
        Assert.assertEquals(accountDTO.getId(), transactions.get(3).getAccount().getId());
        Assert.assertEquals(accountDTO.getId(), transactions.get(2).getAccount().getId());
        Assert.assertEquals(accountDTO.getId(), transactions.get(1).getAccount().getId());
        Assert.assertEquals(accountDTO.getId(), transactions.get(0).getAccount().getId());

        Assert.assertNotNull(transactions.get(5).getCreated());
        Assert.assertNotNull(transactions.get(4).getCreated());
        Assert.assertNotNull(transactions.get(3).getCreated());
        Assert.assertNotNull(transactions.get(2).getCreated());
        Assert.assertNotNull(transactions.get(1).getCreated());
        Assert.assertNotNull(transactions.get(0).getCreated());

    }

    @Test
    public void testCreditConcurrency() {

        {
            String person = "1";
            String bank = "SWEDBANK";
            String accountType = "SAVINGS";
            String response = httpHelper.get(FinalConstants.ENDPOINT + "account/create/", "person", person, "bank", bank, "accounttype", accountType);
            Assert.assertEquals("OK", response);
        }


        String accountJson = httpHelper.get(FinalConstants.ENDPOINT + "account/find/person", "person", "1");
        AccountDTO[] accountDTos = jsonSerializer.fromJson(accountJson, AccountDTO[].class);
        final AccountDTO accountDTO = accountDTos[0];


        //Initialize the account with a random amount
        long initialAmount = (long) (Math.random() * 100);
        httpHelper.get(FinalConstants.ENDPOINT + "account/debit", "id", accountDTO.getId() + "", "amount", initialAmount + "");

        //Create lots of small removals
        int size = 1000;
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < size; i++) {
            threads.add(new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep((long)(Math.random() * 100));
                        long amount = (long) (1 + Math.random() *  10);
                        
                        httpHelper.get(FinalConstants.ENDPOINT + "account/credit", "id", accountDTO.getId() + "", "amount", amount + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //Run the removals
        for (Thread thread : threads) {
            thread.start();
        }

        //Assume that it take 20 seconds to complete
        try {
            Thread.currentThread().sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Now check the balance of the account
        {
            String checkJson = httpHelper.get(FinalConstants.ENDPOINT + "account/find/person", "person", "1");
            AccountDTO refreshedAccountDTO = jsonSerializer.fromJson(checkJson, AccountDTO[].class)[0];
//            Assert.assertEquals(0, refreshedAccountDTO.getHoldings());
            Assert.assertTrue(refreshedAccountDTO.getHoldings() < 10);
            Assert.assertFalse(refreshedAccountDTO.getHoldings() < 0);
        }


    }
    
    @Test
    public void testDebitConcurrency() {
        
         {
            String person = "4";
            String bank = "SWEDBANK";
            String accountType = "SAVINGS";
            String response = httpHelper.get(FinalConstants.ENDPOINT + "account/create/", "person", person, "bank", bank, "accounttype", accountType);
            Assert.assertEquals("OK", response);
        }


        String accountJson = httpHelper.get(FinalConstants.ENDPOINT + "account/find/person", "person", "4");
        AccountDTO[] accountDTos = jsonSerializer.fromJson(accountJson, AccountDTO[].class);
        final AccountDTO accountDTO = accountDTos[0];


        
        //Create lots of small credits
        final int size = 1000;
        final int amount = 10;
        List<Thread> threads = new ArrayList<Thread>();
        for (int i = 0; i < size; i++) {
            threads.add(new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep((long)(Math.random() * 100));
                        httpHelper.get(FinalConstants.ENDPOINT + "account/debit", "id", accountDTO.getId() + "", "amount", amount + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        //Run the removals
        for (Thread thread : threads) {
            thread.start();
        }

        //Assume that it take 20 seconds to complete
        try {
            Thread.currentThread().sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Now check the balance of the account
        {
            String checkJson = httpHelper.get(FinalConstants.ENDPOINT + "account/find/person", "person", "4");
            AccountDTO refreshedAccountDTO = jsonSerializer.fromJson(checkJson, AccountDTO[].class)[0];
            Assert.assertEquals(size * amount, refreshedAccountDTO.getHoldings());
        }
        
    }
}
