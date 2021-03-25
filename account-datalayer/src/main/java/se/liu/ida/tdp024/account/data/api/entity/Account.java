package se.liu.ida.tdp024.account.data.api.entity;

import java.io.Serializable;

public interface Account extends Serializable {
	int getId();
	
    void setHoldings(int value);
    int getHoldings();
    
    void setPersonKey(String personKey);
    String getPersonKey();
    
    void setAccountType(String accountType);
    String getAccountType();
    
    void setBankKey(String bankKey);
    String getBankKey();
}