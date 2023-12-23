package com.oodProject.library.pojo;

import java.util.List;
import java.util.ArrayList;

public class Member extends Person {
	
	int memberId; 
	
	List<Book> booksBorrowed;
	
	List<PrivateRoom> roomsBooked;
	
	long dueAmount;
	
	List<Book> borrowRequests;
	
	List<Book> returnRequests;
	
	public Member() {
		
		booksBorrowed = new ArrayList<>();
		roomsBooked = new ArrayList<>();
		borrowRequests = new ArrayList<>();
		returnRequests = new ArrayList<>();
		
		dueAmount = 0;
		
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public List<Book> getBooksBorrowed() {
		return booksBorrowed;
	}

	public void setBooksBorrowed(List<Book> booksBorrowed) {
		this.booksBorrowed = booksBorrowed;
	}

	public List<PrivateRoom> getRoomsBooked() {
		return roomsBooked;
	}

	public void setRoomsBooked(List<PrivateRoom> roomsBooked) {
		this.roomsBooked = roomsBooked;
	}

	public long getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(long dueAmount) {
		this.dueAmount = dueAmount;
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
