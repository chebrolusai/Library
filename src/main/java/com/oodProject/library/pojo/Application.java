package com.oodProject.library.pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Application {
	

    private int id;

	private String firstName;
	
	private String lastName; 
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date DoB;
	
	private String Reason; 
	
	private String Experience;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Date getDoB() {
		return DoB;
	}

	public void setDoB(Date doB) {
		DoB = doB;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

	public String getExperience() {
		return Experience;
	}

	public void setExperience(String experience) {
		Experience = experience;
	}
	


}
