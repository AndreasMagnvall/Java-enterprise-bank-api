package se.liu.ida.tdp024.account.data.test.facade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import se.liu.ida.tdp024.account.data.api.entity.Person;
import se.liu.ida.tdp024.account.data.api.facade.PersonEntityFacade;
import se.liu.ida.tdp024.account.data.impl.network.entity.PersonAPI;
import se.liu.ida.tdp024.account.data.impl.network.facade.PersonEntityFacadeAPI;

public class PersonEntityFacadeTest {

	// ---- Unit under test ----//
	private PersonEntityFacade personEntityFacade;

	@Before
	public void initialize() {
		personEntityFacade = new PersonEntityFacadeAPI();
	}

	@Test
	public void testPerson() {
		Person person = personEntityFacade.getPersonByKey("5");
		assertEquals("Q", person.getName());
		assertEquals("5", person.getKey());
	}

	@Test
	public void testPersonData() {
		PersonAPI personAPI = new PersonAPI();

		assertNull(personAPI.getName());
		assertNull(personAPI.getKey());
	}

	@Test
	public void testWrongPersonKey() {
		Person person = personEntityFacade.getPersonByKey("98321");
		assertNull(person);
	}

}
