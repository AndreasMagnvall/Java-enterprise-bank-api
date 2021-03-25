package se.liu.ida.tdp024.account.data.api.facade;

import java.util.List;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.impl.db.entity.AccountDB;

public interface AccountEntityFacade {

	public class AccountTransferResult {
			
		public class TransferStatusMessage 
		{
			private final boolean success;
			private final String msg;
			
			TransferStatusMessage(boolean ok) {
				success = ok;
				if (ok) 
					msg = "OK";
				else 
					msg = "FAILED";
			}
			
			public String getMessage() {
				return msg;
			}

			public boolean getSuccess() {
				return success;
			}		
		}
		
		public AccountDB getAccount() {
			return account;
		}
		
		public TransferStatusMessage getStatus() {
			return status;
		}

		public AccountTransferResult(AccountDB account, boolean status) {
			super();
			this.account = account;
			this.status = new TransferStatusMessage(status);
		}
		
		private final AccountDB account;
		private final TransferStatusMessage status;
	}
	
	List<Account> getAccounts(String personKey);
    boolean createAccount(String accountType, String personKey, String bankKey);
    boolean isValidAccountType(String accountType);
    AccountTransferResult debitAccount(int id, int amount);
    AccountTransferResult creditAccount(int accountId, int amount);
}
