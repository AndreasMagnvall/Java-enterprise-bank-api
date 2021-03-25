package se.liu.ida.tdp024.account.data.impl.network.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import se.liu.ida.tdp024.account.data.api.entity.Person;

public class PersonAPI implements Person {

	private static final long serialVersionUID = 1L;
	String name;
	String key;
	
	@JsonProperty("Name")
	@Override
	public String getName() {
		return name;
	}
	
	@JsonProperty("Key")
	@Override
	public String getKey() {
		return key;
	}
}