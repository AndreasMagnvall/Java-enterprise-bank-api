package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertFalse;

import javax.persistence.EntityManager;

import org.junit.Test;


import se.liu.ida.tdp024.account.data.impl.db.util.EMF;


public class EMFTest {
    	
    @Test 
    public void testCloseEM() {
    	 EntityManager entityManager = EMF.getEntityManager();
    	 entityManager.close();
    	 
    	 assertFalse(entityManager.isOpen());
    }

}
