package se.liu.ida.tdp024.account.data.impl.db.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import se.liu.ida.tdp024.account.data.api.entity.Account;


@Entity
public class AccountDB implements Account {

	public AccountDB(String personKey, String accountType, String bankKey, int holdings) {
		super();
		this.personKey = personKey;
		this.accountType = accountType;
		this.bankKey = bankKey;
		this.holdings = holdings;
	}

	public AccountDB() {}
	
	@Id
	@GeneratedValue
    int id;
    String personKey;
    String accountType;
    String bankKey;
    int holdings;


    @Override
    public void setHoldings(int value) {
        holdings = value;
    }

    @Override
    public int getHoldings() {
        return holdings;
    }

	@Override
	public void setPersonKey(String personKey) {
		// TODO Auto-generated method stub
		this.personKey= personKey;
	}

	@Override
	public String getPersonKey() {
		// TODO Auto-generated method stub
		return personKey;
	}

	@Override
	public void setAccountType(String accountType) {
		// TODO Auto-generated method stub
		this.accountType = accountType;
	}

	@Override
	public String getAccountType() {
		// TODO Auto-generated method stub
		return accountType;
	}

	@Override
	public void setBankKey(String bankKey) {
		// TODO Auto-generated method stub
		this.bankKey = bankKey;
	}

	@Override
	public String getBankKey() {
		// TODO Auto-generated method stub
		return bankKey;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}
}
