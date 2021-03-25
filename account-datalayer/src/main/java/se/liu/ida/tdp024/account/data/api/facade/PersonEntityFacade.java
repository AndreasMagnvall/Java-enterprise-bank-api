package se.liu.ida.tdp024.account.data.api.facade;

import se.liu.ida.tdp024.account.data.api.entity.Person;

public interface PersonEntityFacade {
    Person getPersonByKey(String key);
}
