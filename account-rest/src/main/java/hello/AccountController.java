package hello;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import se.liu.ida.tdp024.account.data.api.entity.Account;
import se.liu.ida.tdp024.account.data.api.entity.Transaction;
import se.liu.ida.tdp024.account.data.api.util.RemoteLogger;
import se.liu.ida.tdp024.account.data.api.util.RemoteLogger.Topic;
import se.liu.ida.tdp024.account.data.impl.db.facade.AccountEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.db.facade.TransactionEntityFacadeDB;
import se.liu.ida.tdp024.account.data.impl.network.facade.BankEntityFacadeAPI;
import se.liu.ida.tdp024.account.data.impl.network.facade.PersonEntityFacadeAPI;
import se.liu.ida.tdp024.account.logic.api.facade.AccountLogicFacade;
import se.liu.ida.tdp024.account.logic.impl.facade.AccountLogicFacadeImpl;


//@ControllerAdvice extends ResponseEntityExceptionHandler
@RestController
@RequestMapping("/account-rest/account")
public class AccountController  { 

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleResourceNotFoundException() {
	    return "notFoundJSPPage";
	}
	
    AccountLogicFacade logic = new AccountLogicFacadeImpl(
    								new AccountEntityFacadeDB(),
    								new BankEntityFacadeAPI(),
    								new PersonEntityFacadeAPI(),
    								new TransactionEntityFacadeDB());
    
    
    RemoteLogger logger = RemoteLogger.getInstance();

    @GetMapping("/create")
    public String create(
    					 @RequestParam(value="person") String personKey,          
                         @RequestParam(value="bank") String bankName,
                         @RequestParam(value="accounttype") String accountType) {
    	
    	logger.log(Topic.RESTREQUEST, 
    			String.format("GET %s %s %s %s",
    					"/create", personKey, bankName, accountType));
    	try {
            boolean success = logic.createAccount(accountType, personKey, bankName);
            if(success) {
                return "OK";
            } else {
                return "FAILED";
            }
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}

    }

    @GetMapping("/find/person")
    public List<Account> person(@RequestParam(value="person") String personKey) {	
    	logger.log(Topic.RESTREQUEST, 
    			String.format("GET %s %s",
				"/find/person", personKey));
    	
       return logic.findAccounts(personKey);
    }

    @GetMapping("/debit")
    public String debit(@RequestParam(value="id") Integer accountid,
			    		@RequestParam(value="amount") Integer amount) {
    	
    	logger.log(Topic.RESTREQUEST, 
    			String.format("GET %s %s %s",
				"/debit", accountid, amount));	
    	
    	  boolean success = logic.debitAccount(accountid, amount);
          if(success) {
              return "OK";
          } else {
              return "FAILED";
          }
    }
    
    @GetMapping("/credit")
    public String credit(@RequestParam(value="id") Integer accountid,
    					 @RequestParam(value="amount") Integer amount) {
    	logger.log(Topic.RESTREQUEST, 
    			String.format("GET %s %s %s",
				"/credit", accountid, amount));	
     	   	   	
    	  boolean success = logic.creditAccount(accountid, amount);
          if(success) {
              return "OK";
          } else {
              return "FAILED";
          }
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions(@RequestParam(value="id") Integer accountId) {
    	logger.log(Topic.RESTREQUEST, 
			String.format("GET %s %s",
			"/transactions", accountId));	
   
    	return logic.findTransactions(accountId);
    }
    

}
