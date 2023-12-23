package com.oodProject.library.pojo;

import java.util.Date;

public class Person {
	
	
	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private String password;
	
	private String role;
	
	public Person()
	{
		
	}
	public Person(String firstname, String lastname, String username, String password, String role)
	{
		this.firstName=firstname;
		this.lastName=lastname;
		this.username=username;
		this.password=password;
		this.role=role;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}	
	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	


}
