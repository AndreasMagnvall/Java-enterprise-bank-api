package se.liu.ida.tdp024.account.data.impl.network.facade;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import se.liu.ida.tdp024.account.data.api.entity.Bank;
import se.liu.ida.tdp024.account.data.api.facade.BankEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.RemoteService;
import se.liu.ida.tdp024.account.data.impl.network.entity.BankAPI;

public class BankEntityFacadeAPI implements BankEntityFacade {

	private RemoteService remoteService;
	
	public BankEntityFacadeAPI() {
		remoteService = new RemoteService(4000);
	}

	@Override
	public Bank getBankByName(String name) {

		String pathWithParams = "/find?name="+name;
		 
		Bank bank;
		try {
			bank = remoteService.fetch(pathWithParams, BankAPI.class);
			return bank;
		} catch (IOException e) {
			Logger.getRootLogger().error(e.getMessage());
		} catch (URISyntaxException e) {
			Logger.getRootLogger().error(e.getMessage());
		}
		
		return null;
	} 
	
	
}