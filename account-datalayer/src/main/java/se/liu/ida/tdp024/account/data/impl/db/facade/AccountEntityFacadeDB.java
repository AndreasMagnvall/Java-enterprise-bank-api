package se.liu.ida.tdp024.account.data.impl.db.facade;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import static se.liu.ida.tdp024.account.data.impl.db.util.EMF.getEntityManager;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class AccountEntityFacadeDB implements AccountEntityFacade {

	ReentrantLock lock = new ReentrantLock();
	
    @Override
    public List<Account> getAccounts(String personKey) {
 	   EntityManager em = getEntityManager(); 

 	   em.getTransaction().begin();
 	   Query q = em
	               .createQuery
	               ("SELECT a FROM AccountDB a WHERE a.personKey = :personKey", AccountDB.class);
       q.setParameter("personKey", personKey);
       List<Account> resultList = null;
       
	   try {
		    resultList = (List<Account>)(List<?>)q.getResultList();
			em.getTransaction().commit();
	   	} catch (Exception ex) {
	   		Logger.getRootLogger().error(ex.getMessage());
	   		em.getTransaction().rollback();
	   	} finally {
	        if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
	        em.close();
	    }
       
       return resultList;
    }       

    @Override
    public synchronized boolean createAccount(String accountType, String personKey, String bankKey) {
        
    	//TODO verify that keys don't already exist
    	
    	
       boolean result = true;
       AccountDB account = new AccountDB(personKey, accountType, bankKey, 0);
       
	   EntityManager em = getEntityManager(); 
	   em.getTransaction().begin();

	   try {
		    em.persist(account);
			em.getTransaction().commit();
	   	} catch (Exception ex) {
	   		Logger.getRootLogger().error(ex.getMessage());
	   		em.getTransaction().rollback();
	   		result = false;
	   	} finally {
	        if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
	        em.close();
	    }
	   
    	
		return result;
    }

    @Override
    public boolean isValidAccountType(String accountType) {
    	String check = "CHECK";
    	String savings = "SAVINGS";
    	if(accountType.equals(check) || accountType.equals(savings))
    	{
    		return true;
    	} 
    	
        return false;
    }

    @Override
    public synchronized AccountTransferResult debitAccount(int id, int amount) {
    	AccountTransferResult result;
    	
    	lock.lock();
        EntityManager em = getEntityManager();	
	    em.getTransaction().begin();
	    
	    boolean status = false;
	    
        AccountDB account = findAccount(id);
        if (account != null) {
            account.setHoldings(account.getHoldings() + amount);
            em.merge(account);
            em.getTransaction().commit();
            status = true;
        } else {
            em.getTransaction().rollback();
            status = false;
        }
        
        lock.unlock();
        result = new AccountTransferResult(account, status);

        return result; 
    }

    
    @Override
    public synchronized AccountTransferResult creditAccount(int id, int amount) {
    	
    	AccountTransferResult result;
   
    	lock.lock();
        EntityManager em = getEntityManager();   
	    em.getTransaction().begin();
	    
	    boolean status;
	    
        AccountDB account = findAccount(id);
        if (account != null) {
        	int availableHoldings = account.getHoldings();
        	if(availableHoldings < amount) { 
        		status = false;
        	} else {
                account.setHoldings(availableHoldings - amount);
                em.merge(account);
                em.getTransaction().commit();
                status = true;
        	}
        } else {
            em.getTransaction().rollback();
            status = false;
        }

	    em.close();  
	    
	    lock.unlock();
	        
	    result = new AccountTransferResult(account, status);
	        
 
        return result; 
    }
    
    private AccountDB findAccount(int id) {
        EntityManager entitymanager = getEntityManager();
        AccountDB accountdb = entitymanager.find(AccountDB.class, id);
        return accountdb;
    }
}
