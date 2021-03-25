package se.liu.ida.tdp024.account.data.impl.network.facade;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

import se.liu.ida.tdp024.account.data.api.entity.Person;
import se.liu.ida.tdp024.account.data.api.facade.PersonEntityFacade;
import se.liu.ida.tdp024.account.data.api.util.RemoteService;
import se.liu.ida.tdp024.account.data.impl.network.entity.PersonAPI;

public class PersonEntityFacadeAPI implements PersonEntityFacade {
	
	private RemoteService remoteService;
	
	public PersonEntityFacadeAPI() {
		remoteService = new RemoteService(3000);
	}

	@Override
	public Person getPersonByKey(String key) {
		String pathWithParams = "/find?key="+key;
		 
		Person p;
		try {
			p = remoteService.fetch(pathWithParams, PersonAPI.class);
			return p;
		} catch (IOException e) {
			Logger.getRootLogger().error(e.getMessage());

		} catch (URISyntaxException e) {
			Logger.getRootLogger().error(e.getMessage());
		}
		
		return null;
	}

	

}