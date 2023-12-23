package com.oodProject.library.pojo;

import java.util.List;

import java.util.ArrayList;

public class Librarian extends Person {

	private int id;

	private double hoursWorked;

	private List<Book> borrowRequests;

	private List<Book> returnRequests;

	public Librarian() {

		borrowRequests = new ArrayList<>();
		returnRequests = new ArrayList<>();
		hoursWorked = 0;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(double newHoursWorked) {
		this.hoursWorked = newHoursWorked;
	}

	public List<Book> getBorrowRequests() {
		return borrowRequests;
	}

	public void setBorrowRequests(List<Book> borrowRequests) {
		this.borrowRequests = borrowRequests;
	}

	public List<Book> getReturnRequests() {
		return returnRequests;
	}

	public void setReturnRequests(List<Book> returnRequests) {
		this.returnRequests = returnRequests;
	}

}
