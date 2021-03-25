package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;
import java.util.Date;

import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;

public interface Transaction extends Serializable {  
	int getId();
	
	String getType();
	void   setType(String type);

	void setAmount(int value);
    int getAmount();
	
	void setCreated(Date date);
	Date getCreated();
	
	void   setStatus(String status);
	String getStatus();
   
	void setAccount(AccountDB account);
	AccountDB getAccount();
}