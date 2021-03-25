package se.liu.ida.tdp024.account.data.impl.db.facade;

import static se.liu.ida.tdp024.account.data.impl.db.util.EMF.getEntityManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.facade.AccountEntityFacade.AccountTransferResult;
import se.liu.ida.tdp024.account.data.api.facade.TransactionEntityFacade;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;
import se.liu.ida.tdp024.account.data.impl.db.entity.TransactionDB;

public class TransactionEntityFacadeDB implements TransactionEntityFacade {

	@Override
	public boolean createTransaction(String transactionType, int amount, Date created, AccountTransferResult res) {
	       String status         = res.getStatus().getMessage();
	       AccountDB accountDB   = res.getAccount();
	       
	       boolean validTransType = transactionType.equals("DEBIT") || transactionType.equals("CREDIT");
	       
	       if(!validTransType) {
	    	   return false; // TODO: throw error
	       }
	       
	       TransactionDB transaction = new TransactionDB(transactionType, amount,
	    		   							created, status, accountDB);
	           
		   EntityManager em = getEntityManager(); 
		   em.getTransaction().begin();
		   
		   boolean result;
		   try {
			    em.persist(transaction);
				em.getTransaction().commit();
				result = true;
		   	} catch (Exception ex) {
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

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Transaction> getTransactions(int accountId) {
	 	   EntityManager em = getEntityManager(); 

	 	   em.getTransaction().begin();
	 	   Query q = em
           .createQuery("SELECT t FROM TransactionDB t INNER JOIN t.account a WHERE a.id = :accountId",
        		   TransactionDB.class);
	 	   q.setParameter("accountId", accountId);
	 	   /*
	 	      SELECT t FROM TransactionDB t INNER JOIN AccountDB.account a WHERE t.account_id = :accountId"   
	 	       // "SELECT t FROM TransactionDB t WHERE t.account = :accountId", // SELECT t FROM TransactionDB t INNER JOIN AccountDB a WHERE t.account_id = :accountId" 
	 	    
	 	    */
	 	  
	 	   
	       List<Transaction> resultList = new ArrayList<Transaction>();
	       
		   try {
			    resultList = (List<Transaction>)(List<?>)q.getResultList();
				em.getTransaction().commit();
		   	} catch (Exception ex) {
		   		em.getTransaction().rollback();
		   	} finally {
		        if (em.getTransaction().isActive()) {
	                em.getTransaction().rollback();
	            }
		        em.close();
		    }
	       
	       return resultList;
	}
}
